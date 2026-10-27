package com.sistema.financeiro.finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.financeiro.finance.model.Account;

// Toda consulta filtra pelo dono, para um usuário nunca ver as contas de outro (RN01 e ADR 0002)
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUserIdAndName(Long userId, String name);

    List<Account> findByUserIdOrderByNameAsc(Long userId);
}
