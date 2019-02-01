package com.battleship.boundary.filter;

import com.battleship.boundary.Secured;
import com.battleship.control.AuthService;
import org.apache.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

@Provider
@Secured
public class AuthFilter implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(AuthFilter.class);
    private AuthService authService = AuthService.getInstance();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        try {
            String authHeaderVal = requestContext.getHeaderString(AuthService.AUTH_HEADER);
            String subject = validateToken(authHeaderVal);
            if (subject != null) {
                final SecurityContext securityContext = requestContext.getSecurityContext();
                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return new Principal() {
                            @Override
                            public String getName() {
                                return subject;
                            }
                        };
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return AuthService.AUTH_HEADER;
                    }
                });
            }
        } catch (Exception e) {
            logger.error("Incorrect player token!", e);
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build());
        }
    }

    private String validateToken(String token) {
        String playerId = authService.validateToken(token);
        return playerId;
    }
}
