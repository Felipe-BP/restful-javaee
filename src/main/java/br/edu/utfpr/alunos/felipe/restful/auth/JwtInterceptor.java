/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.alunos.felipe.restful.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author felipe
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
@JwtAnnotation
public class JwtInterceptor implements ContainerRequestFilter {

    private static final String LOGIN_PATH = "user/login";
    private static final String REGISTER_PATH = "user/register";
    private static final String PRIVATE_KEY = "Authorization";
    private JwtTools jwtTools = JwtTools.getInstance();

    private Response getUnAuthorizeResponse(String message) {
        return Response
                .ok(message)
                .status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        String path = request.getUriInfo().getPath();

        if (!path.equals(LOGIN_PATH) && !path.equals(REGISTER_PATH)) {
            String authorizationHeader
                    = request.getHeaderString(HttpHeaders.AUTHORIZATION);

            // Check if the HTTP Authorization header is present and formatted correctly 
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new NotAuthorizedException("Authorization header must be provided");
            }

            // Extract the token from the HTTP Authorization header
            String token = authorizationHeader.substring("Bearer".length()).trim();

            try {
                // Validate the token
                jwtTools.verifyKey(token);

            } catch (ExpiredJwtException | MalformedJwtException e) {
                if (e instanceof ExpiredJwtException) {
                    throw new WebApplicationException(getUnAuthorizeResponse(PRIVATE_KEY + "is expired"));
                } else if (e instanceof MalformedJwtException) {
                    throw new WebApplicationException(getUnAuthorizeResponse(PRIVATE_KEY + "isn't correct"));
                }
            }
        }
    }

}
