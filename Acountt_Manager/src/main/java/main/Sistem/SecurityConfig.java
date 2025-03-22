package main.Sistem;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final PermissionAuthorizationManager permissionAuthorizationManager;
    private final JwtTokenUtil jwtTokenUtil;
    public SecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter, PermissionAuthorizationManager permissionAuthorizationManager, JwtTokenUtil jwtTokenUtil) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.permissionAuthorizationManager = permissionAuthorizationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000")); // Frontend URL
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter, SecurityContextPersistenceFilter.class)
                .anonymous(anonymous -> anonymous.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/user/register").permitAll()
                        .requestMatchers("/admin/**").access(permissionAuthorizationManager)  // Secure only `/admin/**`
                        .requestMatchers("/user/**").access(permissionAuthorizationManager)



                        .anyRequest().authenticated()


                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                    if (!response.isCommitted()) {
                        System.out.println(":x: Authentication failed! Returning 403.");
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    } else {
                        System.out.println(":warning: Response was already committed, skipping sendError.");
                    }

                }));

        return http.build();
    }
}
