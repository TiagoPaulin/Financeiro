package com.sistema.financeiro.identity.internal;

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
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    UserRepository users;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserRegistrationService registration;

    @Test
    void registersUserWithNormalizedEmailAndHashedPassword() {
        when(users.existsByEmail("tiago@example.com")).thenReturn(false);
        when(passwordEncoder.encode("senha-segura")).thenReturn("hash-da-senha");
        when(users.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = registration.register("  Tiago ", "  Tiago@Example.com ", "senha-segura");

        assertThat(user.getName()).isEqualTo("Tiago");
        assertThat(user.getEmail()).isEqualTo("tiago@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("hash-da-senha");
    }

    @Test
    void rejectsEmailAlreadyInUse() {
        when(users.existsByEmail("tiago@example.com")).thenReturn(true);

        assertThatThrownBy(() -> registration.register("Tiago", "tiago@example.com", "senha-segura"))
                .isInstanceOf(EmailAlreadyInUseException.class);

        verify(users, never()).save(any());
    }
}
