package com.sistema.financeiro.finance.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.sistema.financeiro.finance.dto.AccountResponse;
import com.sistema.financeiro.finance.dto.CreateAccountRequest;
import com.sistema.financeiro.finance.model.Account;
import com.sistema.financeiro.finance.service.AccountService;
import com.sistema.financeiro.identity.CurrentUser;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
class AccountController {

    private final AccountService accountService;

    // Vem do módulo identity: é a única coisa dele que o finance usa
    private final CurrentUser currentUser;

    AccountController(AccountService accountService, CurrentUser currentUser) {
        this.accountService = accountService;
        this.currentUser = currentUser;
    }

    @PostMapping
    ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request, UriComponentsBuilder uriBuilder) {
        Account account = accountService.create(
                currentUser.id(), request.name(), request.type(), request.initialBalanceInCents());

        URI location = uriBuilder.path("/api/accounts/{id}").buildAndExpand(account.getId()).toUri();
        return ResponseEntity.created(location).body(AccountResponse.from(account));
    }

    @GetMapping
    List<AccountResponse> list() {
        return accountService.list(currentUser.id()).stream()
                .map(AccountResponse::from)
                .toList();
    }
}
