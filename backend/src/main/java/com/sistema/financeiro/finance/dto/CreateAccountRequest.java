package com.sistema.financeiro.finance.dto;

import com.sistema.financeiro.finance.model.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String name,

        @NotNull(message = "O tipo é obrigatório")
        AccountType type,

        // Em centavos: 150000 = R$ 1.500,00 (ADR 0011). Pode ser negativo, como num cheque especial
        @NotNull(message = "O saldo inicial é obrigatório")
        Long initialBalanceInCents) {
}
