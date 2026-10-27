package com.sistema.financeiro.finance.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.financeiro.finance.exception.AccountNameAlreadyInUseException;
import com.sistema.financeiro.finance.model.Account;
import com.sistema.financeiro.finance.model.AccountType;
import com.sistema.financeiro.finance.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Cadastro de conta (RF06)
    @Transactional
    public Account create(Long userId, String name, AccountType type, long initialBalanceCents) {
        String trimmedName = name.trim();

        // O mesmo usuário não pode ter duas contas com o mesmo nome (RN10)
        if (accountRepository.existsByUserIdAndName(userId, trimmedName)) {
            throw new AccountNameAlreadyInUseException(trimmedName);
        }

        return accountRepository.save(new Account(userId, trimmedName, type, initialBalanceCents));
    }

    // Lista de contas do usuário (RF09). O saldo atual entra junto com os lançamentos (ADR 0012)
    @Transactional(readOnly = true)
    public List<Account> list(Long userId) {
        return accountRepository.findByUserIdOrderByNameAsc(userId);
    }
}
