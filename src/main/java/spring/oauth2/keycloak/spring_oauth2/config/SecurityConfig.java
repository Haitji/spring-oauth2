package spring.oauth2.keycloak.spring_oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http.csrf(csrf-> csrf.disable())
        .authorizeHttpRequests(au -> au.anyRequest().authenticated())
        .oauth2ResourceServer(oauth -> {
            oauth.jwt(jwt -> {});
        })
        .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }

}
