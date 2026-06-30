package br.com.fiap.techfood.infrastructure.security;

import br.com.fiap.techfood.application.gateway.PasswordEncoderGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderGateway implements PasswordEncoderGateway {

    private final BCryptPasswordEncoder passwordEncoder;

    public BCryptPasswordEncoderGateway(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
