package br.com.fiap.techfood.application.gateway;

import br.com.fiap.techfood.domain.user.User;

public interface TokenGateway {

    String generateToken(User user);
}
