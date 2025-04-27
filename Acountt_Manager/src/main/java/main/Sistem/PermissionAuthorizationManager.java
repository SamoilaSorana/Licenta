package main.Sistem;

import main.DAO.PermisiuniDAO;
import main.Objects.Permisiune;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Component
public class PermissionAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        String requestURI = context.getRequest().getRequestURI();

        if (doesNotRequireLogin(requestURI)) {
            return new AuthorizationDecision(true);
        }

        System.out.println("🔍 PermissionAuthorizationManager executing for: " + requestURI);
        System.out.println("🔍 Authentication BEFORE role extraction: " + auth);

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            System.out.println(":warning: Authentication is still ANONYMOUS, skipping authorization check.");
            return new AuthorizationDecision(false);
        }

        // Extract role from Authentication principal (Stored in JWT token)
        String role = extractRoleFromAuthentication(auth);
        System.out.println("✅ Extracted Role: " + role);

        // Fetch permissions for this role from the database
        List<Permisiune> permisiuneList = PermisiuniDAO.getPermisionsByRol(role);
        List<String> permissions = new ArrayList<>();
        for (Permisiune permisiune : permisiuneList) {
            permissions.add(permisiune.getPermisiune_name());
        }

        System.out.println("🔍 Permissions found for role [" + role + "]: " + permissions);

        // Get the required permission for this endpoint
        String requiredPermission = getRequiredPermissionForEndpoint(requestURI);
        System.out.println("🔍 Required permission for this request: " + requiredPermission);

        if (requiredPermission == null) {
            System.out.println("✅ No specific permission required. Access GRANTED.");
            return new AuthorizationDecision(true);
        }

        boolean hasPermission = permissions.contains(requiredPermission);
        System.out.println(hasPermission ? "✅ Access GRANTED" : "❌ Access DENIED (403)");

        return new AuthorizationDecision(hasPermission);
    }

    private String extractRoleFromAuthentication(Authentication auth) {
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.getAuthority().startsWith("ROLE_")) {
                return authority.getAuthority().replace("ROLE_", ""); // Remove "ROLE_" prefix
            }
        }
        return "ANONYMOUS"; // If no valid role found, default to ANONYMOUS
    }

    private boolean doesNotRequireLogin(String requestURI) {
        return requestURI.startsWith("/user/login") || requestURI.startsWith("/user/register");
    }

    // Example: Define permissions based on URL
    private String getRequiredPermissionForEndpoint(String uri) {
        if (uri.startsWith("/admin/accounts")) return "GET_ACCOUNTS";
        return null; // No permission required
    }
}