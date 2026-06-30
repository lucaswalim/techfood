package br.com.fiap.techfood.application.gateway;

import br.com.fiap.techfood.model.User;

public interface TokenGateway {

    String generateToken(User user);
}
