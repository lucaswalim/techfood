package br.com.fiap.techfood.service;

import br.com.fiap.techfood.application.dto.ChangePasswordCommand;
import br.com.fiap.techfood.application.dto.LoginCommand;
import br.com.fiap.techfood.application.dto.TokenResult;

import java.util.UUID;

public interface AuthService {

    TokenResult login(LoginCommand command);

    void changePassword(UUID id, ChangePasswordCommand command);
}
