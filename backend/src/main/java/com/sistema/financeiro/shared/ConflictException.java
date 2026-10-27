package com.sistema.financeiro.shared;

/**
 * Erro de quando a operação conflita com um dado que já existe, como um email repetido.
 * Vira uma resposta 409 (ADR 0009).
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
