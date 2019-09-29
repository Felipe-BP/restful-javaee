/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.alunos.felipe.restful.auth;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author felipe
 */
@Provider
@JwtAnnotation
@Priority(Priorities.AUTHENTICATION)
public class JwtInterceptor implements ContainerRequestFilter {

    private static final String AUTH_PATH = "authorization";
    private static final String PRIVATE_KEY = "Authorization";
    private JwtTools jwtTools = JwtTools.getInstance();
    private Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    private Response getUnAuthorizeResponse(String message) {
        return Response
                .ok(message)
                .status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        String path = request.getPath();
        logger.trace(path);
        if (path.equals(AUTH_PATH)) {
            return request;
        }

        String keyOfHeader = request.getHeaderValue(PRIVATE_KEY);
        if (keyOfHeader == null || keyOfHeader.isEmpty()) {
            throw new WebApplicationException(getUnAuthorizeResponse(PRIVATE_KEY + "is missing header"));
        }

        try {
            jwtTools.verifyKey(keyOfHeader);
        } catch (ExpiredJwtException | MalformedJwtException e) {
            if (e instanceof ExpiredJwtException) {
                throw new WebApplicationException(getUnAuthorizeResponse(PRIVATE_KEY + "is expired"));
            } else if (e instanceof MalformedJwtException) {
                throw new WebApplicationException(getUnAuthorizeResponse(PRIVATE_KEY + "isn't correct"));
            }
        }
        return request;
    }

}
