package com.sistema.financeiro.identity.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String name,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email não é válido")
        @Size(max = 255, message = "O email deve ter no máximo 255 caracteres")
        String email,

        // Mínimo de 8 pelo RN03. O BCrypt só considera os primeiros 72 bytes da senha
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
        String password) {
}
