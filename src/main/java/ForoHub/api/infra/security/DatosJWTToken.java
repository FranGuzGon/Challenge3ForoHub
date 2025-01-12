package ForoHub.api.infra.security;

public record DatosJWTToken(String Token, String type) {
    public DatosJWTToken(String token){
        this(token, "Bearer");
    }
}
