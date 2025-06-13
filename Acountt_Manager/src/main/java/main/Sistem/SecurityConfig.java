package main.Sistem;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;



import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final PermissionAuthorizationManager permissionAuthorizationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter,
                          PermissionAuthorizationManager permissionAuthorizationManager,
                          JwtTokenUtil jwtTokenUtil) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.permissionAuthorizationManager = permissionAuthorizationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @Bean
    public HttpFirewall allowUrlEncodedPercentHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowBackSlash(true);
        firewall.setAllowSemicolon(true);
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(HttpFirewall firewall) {
        return (web) -> web.httpFirewall(firewall);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println(" SecurityConfig is active");

        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter, SecurityContextPersistenceFilter.class)
                .authorizeHttpRequests(authz -> authz

                        .requestMatchers("/user/register", "/user/login", "/api/auth/**").permitAll()


                        .requestMatchers("/admin/**").access(permissionAuthorizationManager)
                        .requestMatchers("/user/**").access(permissionAuthorizationManager)


                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                    if (!response.isCommitted()) {
                        System.out.println(" Access denied! 403 returned.");
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    }
                }));

        return http.build();
    }
}
