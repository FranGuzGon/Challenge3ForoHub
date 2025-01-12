package ForoHub.api.infra.security;

import ForoHub.api.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    @PostConstruct
    public void init() {
        System.out.println("apiSecret in @PostConstruct: " + apiSecret);
    }

    public String generarToken(Usuario usuario){

        System.out.println("apiSecret: " + apiSecret); // Verificar el valor de apiSecret
        System.out.println("Usuario Mail: " + usuario.getMail());
        System.out.println("Usuario ID: " + usuario.getId());
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("forohub")
                    .withSubject(usuario.getMail())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }



//Metodo para validar y consumir token
//Metodo para corroborar si usuario ha iniciado sesion en nuestra apiverificar si usuario ha iniciado sesión en nuestra api
    public String getSubject(String token) {
        if (token == null){
            throw new RuntimeException();
        }
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret); //Validando firma del token
            verifier = JWT.require(algorithm)
                    .withIssuer("forohub")
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.toString());
        }
      if (verifier.getSubject() == null) {
        throw new RuntimeException("Verifier invalido");
      }
        return verifier.getSubject();
    }

    private Instant generarFechaExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }

}
