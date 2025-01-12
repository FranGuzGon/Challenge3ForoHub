package ForoHub.api.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404(){
        return ResponseEntity.notFound().build();

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e){
        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity tratarError401() {
        // Error por credenciales incorrectas
        return ResponseEntity.status(401).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 401,
                "error", "Unauthorized",
                "message", "Credenciales incorrectas",
                "path", "/login"
        ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity tratarError403() {
        // Error por falta de permisos
        return ResponseEntity.status(403).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 403,
                "error", "Forbidden",
                "message", "Access Denied",
                "path", "/topicos"
        ));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity tratarErrorAutenticacion(AuthenticationException e) {
        // Error genérico de autenticación
        return ResponseEntity.status(401).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 401,
                "error", "Unauthorized",
                "message", e.getMessage(),
                "path", "/login"
        ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> manejarRuntimeException(RuntimeException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("Ya existe un topico con el mismo título y mensaje")) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Tópico duplicado",
                    "Ya existe un tópico con el mismo título y mensaje.",
                    HttpStatus.CONFLICT.value()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "Error interno",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> manejarNullPointerException(NullPointerException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("com.auth0.jwt.interfaces.DecodedJWT.getSubject")) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Token inválido",
                    "El token ingresado es incorrecto o no se pudo procesar.",
                    HttpStatus.UNAUTHORIZED.value()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "Error interno",
                "Ocurrió un error inesperado. Por favor, contacte al administrador.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // Clase reutilizable para estructurar la respuesta de errores
    public static class ErrorResponse {
        private String error;
        private String mensaje;
        private int codigo;

        public ErrorResponse(String error, String mensaje, int codigo) {
            this.error = error;
            this.mensaje = mensaje;
            this.codigo = codigo;
        }

        public String getError() {
            return error;
        }

        public String getMensaje() {
            return mensaje;
        }

        public int getCodigo() {
            return codigo;
        }
    }

    private record DatosErrorValidacion(String campo, String error){
        public DatosErrorValidacion(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
