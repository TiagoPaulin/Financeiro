package com.sistema.financeiro.finance.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;
import com.sistema.financeiro.TestcontainersConfiguration;
import com.sistema.financeiro.identity.AuthenticatedUser;

/**
 * Testa as contas de ponta a ponta, com PostgreSQL de verdade (ADR 0007).
 * Como o login com JWT ainda não existe, o teste coloca o usuário logado direto no Spring Security.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Transactional
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createsAccountForLoggedUser() throws Exception {
        RequestPostProcessor tiago = loggedUser("tiago@example.com");

        mockMvc.perform(post("/api/accounts")
                        .with(tiago)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Nubank", "type": "CHECKING", "initialBalanceInCents": 150000}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/accounts/")))
                .andExpect(jsonPath("$.name").value("Nubank"))
                .andExpect(jsonPath("$.type").value("CHECKING"))
                .andExpect(jsonPath("$.initialBalanceInCents").value(150000))
                .andExpect(jsonPath("$.archived").value(false));
    }

    // Um usuário nunca vê as contas de outro (RNF03 e ADR 0002)
    @Test
    void listsOnlyAccountsOfLoggedUser() throws Exception {
        RequestPostProcessor tiago = loggedUser("tiago@example.com");
        RequestPostProcessor maria = loggedUser("maria@example.com");
        createAccount(tiago, "Nubank");
        createAccount(maria, "Itau");

        mockMvc.perform(get("/api/accounts").with(tiago))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Nubank"));
    }

    @Test
    void rejectsDuplicateNameForSameUser() throws Exception {
        RequestPostProcessor tiago = loggedUser("tiago@example.com");
        createAccount(tiago, "Nubank");

        mockMvc.perform(post("/api/accounts")
                        .with(tiago)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Nubank", "type": "SAVINGS", "initialBalanceInCents": 0}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Já existe uma conta com o nome 'Nubank'"));
    }

    @Test
    void allowsSameNameForDifferentUsers() throws Exception {
        createAccount(loggedUser("tiago@example.com"), "Nubank");
        createAccount(loggedUser("maria@example.com"), "Nubank");
    }

    @Test
    void requiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isUnauthorized());
    }

    // Cadastra o usuário pela API do módulo identity e devolve as requisições "logadas" como ele
    private RequestPostProcessor loggedUser(String email) throws Exception {
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Usuário de teste", "email": "%s", "password": "senha-segura"}
                                """.formatted(email)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long userId = ((Number) JsonPath.read(response, "$.id")).longValue();
        AuthenticatedUser user = new AuthenticatedUser(userId, email);

        return authentication(new UsernamePasswordAuthenticationToken(user, null, List.of()));
    }

    private void createAccount(RequestPostProcessor user, String name) throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .with(user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "%s", "type": "CHECKING", "initialBalanceInCents": 0}
                                """.formatted(name)))
                .andExpect(status().isCreated());
    }
}
