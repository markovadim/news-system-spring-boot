package ru.clevertec.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.clevertec.security.user.model.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers(
                        HttpMethod.GET,
                        "/swagger-ui.html",
                        "/api/v1/news",
                        "/api/v1/comments"
                )
                .permitAll()

                .requestMatchers("/api/v1/**").hasRole(Role.ADMIN.name())
                .requestMatchers("/api/v1/users/**").hasRole(Role.ADMIN.name())

                .requestMatchers("/api/v1/news/**").hasRole(Role.JOURNALIST.name())
                .requestMatchers("/api/v1/comments/**").hasRole(Role.SUBSCRIBER.name())
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
