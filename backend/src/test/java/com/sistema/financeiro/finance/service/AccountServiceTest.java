package com.sistema.financeiro.finance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sistema.financeiro.finance.exception.AccountNameAlreadyInUseException;
import com.sistema.financeiro.finance.model.Account;
import com.sistema.financeiro.finance.model.AccountType;
import com.sistema.financeiro.finance.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;

    @Test
    void createsAccountForUser() {
        when(accountRepository.existsByUserIdAndName(1L, "Nubank")).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account account = accountService.create(1L, "  Nubank ", AccountType.CHECKING, 150_000);

        assertThat(account.getUserId()).isEqualTo(1L);
        assertThat(account.getName()).isEqualTo("Nubank");
        assertThat(account.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(account.getInitialBalanceCents()).isEqualTo(150_000);
        assertThat(account.isArchived()).isFalse();
    }

    @Test
    void rejectsDuplicateNameForSameUser() {
        when(accountRepository.existsByUserIdAndName(1L, "Nubank")).thenReturn(true);

        assertThatThrownBy(() -> accountService.create(1L, "Nubank", AccountType.CHECKING, 0))
                .isInstanceOf(AccountNameAlreadyInUseException.class);

        verify(accountRepository, never()).save(any());
    }
}
