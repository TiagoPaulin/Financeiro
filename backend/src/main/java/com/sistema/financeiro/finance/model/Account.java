package com.sistema.financeiro.finance.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Só o id do dono, e não a entidade User: ela pertence ao módulo identity (ADR 0004)
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType type;

    // Valor em centavos (RNF06)
    @Column(nullable = false)
    private long initialBalanceCents;

    @Column(nullable = false)
    private boolean archived;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    // Exigido pelo JPA
    protected Account() {
    }

    public Account(Long userId, String name, AccountType type, long initialBalanceCents) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.initialBalanceCents = initialBalanceCents;
        this.archived = false;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    public long getInitialBalanceCents() {
        return initialBalanceCents;
    }

    public boolean isArchived() {
        return archived;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
