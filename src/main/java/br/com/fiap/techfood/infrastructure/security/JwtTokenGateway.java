package br.com.fiap.techfood.infrastructure.security;

import br.com.fiap.techfood.application.gateway.TokenGateway;
import br.com.fiap.techfood.domain.user.User;

public class JwtTokenGateway implements TokenGateway {

    private final JwtService jwtService;

    public JwtTokenGateway(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String generateToken(User user) {
        return jwtService.generateToken(user);
    }
}
