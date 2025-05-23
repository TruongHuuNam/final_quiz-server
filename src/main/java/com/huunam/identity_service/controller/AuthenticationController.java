package com.huunam.identity_service.controller;

import com.huunam.identity_service.dto.request.*;
import com.huunam.identity_service.dto.response.AuthenticationResponse;
import com.huunam.identity_service.dto.response.IntrospectResponse;
import com.huunam.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor // autowired cac bean
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
        AuthenticationService authenticationService;

        @PostMapping("/token")
        ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
                var result = authenticationService.authenticate(request);
                return ApiResponse.<AuthenticationResponse>builder()
                                .result(result) // AuthenticationResponse.builder().authenticated(result).build()
                                .build();
        }

        @PostMapping("/introspect")
        ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
                        throws ParseException, JOSEException {
                var result = authenticationService.introspect(request);
                return ApiResponse.<IntrospectResponse>builder()
                                .result(result) // AuthenticationResponse.builder().authenticated(result).build()
                                .build();
        }

        @PostMapping("/refresh")
        ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
                        throws ParseException, JOSEException {
                var result = authenticationService.refreshToken(request);
                return ApiResponse.<AuthenticationResponse>builder()
                                .result(result) // AuthenticationResponse.builder().authenticated(result).build()
                                .build();
        }

        @PostMapping("/logout")
        ApiResponse<Void> logout(@RequestBody LogoutRequest request)
                        throws ParseException, JOSEException {
                authenticationService.logout(request);
                return ApiResponse.<Void>builder()
                                .build();
        }

}
