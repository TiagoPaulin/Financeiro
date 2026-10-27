package com.sistema.financeiro.identity.internal;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String name, String email, String rawPassword) {
        // "Tiago@Email.com" e "tiago@email.com" são o mesmo email (RN02)
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        if (users.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyInUseException();
        }

        User user = new User(name.trim(), normalizedEmail, passwordEncoder.encode(rawPassword));
        return users.save(user);
    }
}
