package com.sistema.financeiro.identity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * O que o módulo identity oferece aos outros módulos: quem é o usuário da requisição atual.
 * Os outros módulos usam só o id, nunca a entidade User, que é interna do identity (ADR 0004).
 */
@Component
public class CurrentUser {

    public Long id() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new IllegalStateException("Nenhum usuário autenticado na requisição");
        }

        return user.id();
    }
}
