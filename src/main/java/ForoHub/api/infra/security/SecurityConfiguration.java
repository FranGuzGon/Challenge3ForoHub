package ForoHub.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {



    @Autowired
    private SecurityFilter securityFilter;

    // Este método tiene por finalidad indicarle a Spring que utilice el método de autenticacion Stateless

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  //Le indicamos a Spring el tipo de sesion
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll(); //Autorizamos los request POST a /login
                    req.requestMatchers(HttpMethod.GET, "/topicos").permitAll(); //Autorizamos los request GET a /login
                    req.requestMatchers(HttpMethod.GET, "/topicos/{id}").permitAll(); //Autorizamos los request GET a /login
                    req.anyRequest().authenticated(); //Cualquier otro request requerirá autenticación
                })
                //Con la siguiente línea le indicamos a Spring que agregue nuestro filtro antes del de Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
