package com.sistema.financeiro.identity.web;

import java.time.Instant;

import com.sistema.financeiro.identity.internal.User;

// Nunca devolve a senha nem o hash dela
public record UserResponse(Long id, String name, String email, Instant createdAt) {

    static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }
}
