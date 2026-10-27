package com.sistema.financeiro.finance.exception;

import com.sistema.financeiro.shared.ConflictException;

public class AccountNameAlreadyInUseException extends ConflictException {

    public AccountNameAlreadyInUseException(String name) {
        super("Já existe uma conta com o nome '" + name + "'");
    }
}
