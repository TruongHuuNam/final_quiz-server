package com.huunam.identity_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huunam.identity_service.dto.request.ApiResponse;
import com.huunam.identity_service.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.USER_UNAUTHENTICATED;

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build(); //type: Object response

        ObjectMapper objectMapper = new ObjectMapper(); //user ObjectMapper to change type Object

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse)); //method write return a String so have to change type of object into string
        response.flushBuffer();
    }
}
