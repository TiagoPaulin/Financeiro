package com.sistema.financeiro.identity;

/**
 * O usuário logado. Quando o login com JWT existir (ADR 0010), o filtro de segurança
 * vai colocar um objeto deste tipo no contexto do Spring Security a cada requisição autenticada.
 */
public record AuthenticatedUser(Long id, String email) {
}
