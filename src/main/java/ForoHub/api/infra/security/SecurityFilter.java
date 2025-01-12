package ForoHub.api.infra.security;

import ForoHub.api.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.sound.midi.Soundbank;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("El filtro está siendo llamado, es el inicio del filtro");

        //Obtener el Token del header
        var authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);

        if (authHeader !=null) {
            System.out.println("Validamos que token no es null");

            var token = authHeader.replace("Bearer ", "");
            System.out.println(token);

            //Hasta acá estamos llegando
            System.out.println(tokenService.getSubject(token)); //¿Este usuario tiene sesión?

            var subject = tokenService.getSubject(token);
            if (subject != null){
                // Token valido
                var usuario = usuarioRepository.findByMail(subject);
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                        usuario.getAuthorities()); //Forzamos un inicio se sesión
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
