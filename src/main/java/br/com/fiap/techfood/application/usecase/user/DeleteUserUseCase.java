package br.com.fiap.techfood.application.usecase.user;

import br.com.fiap.techfood.application.gateway.UserGateway;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class DeleteUserUseCase {

    private final UserGateway userGateway;

    public DeleteUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void execute(UUID id) {
        if (!userGateway.existsById(id)) {
            throw new NoSuchElementException("Usuário não encontrado");
        }

        userGateway.deleteById(id);
    }
}
