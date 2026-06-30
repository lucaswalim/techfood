package br.com.fiap.techfood.application.usecase.auth;

import br.com.fiap.techfood.application.dto.ChangePasswordCommand;
import br.com.fiap.techfood.application.gateway.PasswordEncoderGateway;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.exceptions.InvalidCredentialsException;
import br.com.fiap.techfood.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ChangePasswordUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;

    public ChangePasswordUseCase(UserGateway userGateway, PasswordEncoderGateway passwordEncoderGateway) {
        this.userGateway = userGateway;
        this.passwordEncoderGateway = passwordEncoderGateway;
    }

    public void execute(UUID id, ChangePasswordCommand command) {
        User user = userGateway.findById(id)
                .orElseThrow(() -> new InvalidCredentialsException("Usuário ou senha inválidos"));

        if (!user.getLogin().equals(command.login())) {
            throw new InvalidCredentialsException("Login informado não corresponde ao usuário");
        }

        if (!passwordEncoderGateway.matches(command.currentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Usuário ou senha inválidos");
        }

        user.setPassword(passwordEncoderGateway.encode(command.newPassword()));
        userGateway.save(user);
    }
}
