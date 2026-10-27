package com.sistema.financeiro.finance.dto;

import java.time.Instant;

import com.sistema.financeiro.finance.model.Account;
import com.sistema.financeiro.finance.model.AccountType;

public record AccountResponse(
        Long id,
        String name,
        AccountType type,
        long initialBalanceInCents,
        boolean archived,
        Instant createdAt) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getType(),
                account.getInitialBalanceCents(),
                account.isArchived(),
                account.getCreatedAt());
    }
}
