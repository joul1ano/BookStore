package com.bookstore.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "Operations related to authentication / authorization")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthenticationResponse> register(@Validated @RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));

    }

    @PostMapping("/login")
    @Operation(summary = "Log in")
    public ResponseEntity<AuthenticationResponse> authenticate(@Validated @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }


}
