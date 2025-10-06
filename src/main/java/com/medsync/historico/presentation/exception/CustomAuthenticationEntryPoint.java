package com.medsync.historico.presentation.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String errorMessage = getErrorMessage(authException);

        GraphQLError error = GraphqlErrorBuilder.newError()
                .errorType(ErrorType.UNAUTHORIZED)
                .message(errorMessage)
                .build();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("errors", Collections.singletonList(error.toSpecification()));
        responseBody.put("data", null);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    private static String getErrorMessage(AuthenticationException authException) {
        String errorMessage = "Unauthorized Access - Invalid or missing token.";

        if (authException.getCause() instanceof JwtException jwtException &&
            jwtException.getMessage().toLowerCase().contains("expired")
        ) {
            errorMessage = "Expired Token - Please refresh your token and try again.";
        }

        return errorMessage;
    }

}
