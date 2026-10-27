package com.sistema.financeiro.identity.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.sistema.financeiro.identity.internal.User;
import com.sistema.financeiro.identity.internal.UserRegistrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
class UserController {

    private final UserRegistrationService registration;

    UserController(UserRegistrationService registration) {
        this.registration = registration;
    }

    // Cadastro de usuário (RF02)
    @PostMapping
    ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder) {
        User user = registration.register(request.name(), request.email(), request.password());

        URI location = uriBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(UserResponse.from(user));
    }
}
