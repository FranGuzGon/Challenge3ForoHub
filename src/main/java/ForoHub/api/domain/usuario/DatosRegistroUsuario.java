package ForoHub.api.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroUsuario (
        @NotBlank
        String nombre,
        @NotBlank
        String mail,
        @NotBlank
        String contrasena
){

}


