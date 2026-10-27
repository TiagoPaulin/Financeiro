package com.sistema.financeiro.identity.internal;

import com.sistema.financeiro.shared.ConflictException;

public class EmailAlreadyInUseException extends ConflictException {

    public EmailAlreadyInUseException() {
        super("Este email já está cadastrado");
    }
}
