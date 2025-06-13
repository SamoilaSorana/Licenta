package main.Sistem;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;



@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;


    public JwtAuthorizationFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        System.out.println(":mag: JwtAuthorizationFilter executed for request: " + request.getRequestURI());

        String token = extractToken(request);


        if (token == null) {
            System.out.println(":warning: No JWT found. Skipping authentication.");
            chain.doFilter(request, response);
            return;
        }

        if (response.isCommitted()) {
            System.out.println(":warning: Response already committed. Skipping filter.");
            return;
        }

        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(":mag: BEFORE FILTER - Existing Authentication: " + existingAuth);

        if (jwtTokenUtil.validateToken(token)) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            String role = jwtTokenUtil.getRoleFromToken(token);

            System.out.println(":key: Extracted Role from JWT: " + role);

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            System.out.println(":key: Assigned Authorities: " + authorities);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println(":x: Token validation failed.");
        }

        System.out.println(":white_check_mark: AFTER FILTER - Security Context: " + SecurityContextHolder.getContext().getAuthentication());

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
    }
}
