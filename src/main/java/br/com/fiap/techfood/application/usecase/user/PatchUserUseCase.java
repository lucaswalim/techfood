package br.com.fiap.techfood.application.usecase.user;

import br.com.fiap.techfood.application.dto.UserPatchCommand;
import br.com.fiap.techfood.application.dto.UserResult;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.application.mapper.ApplicationUserMapper;
import br.com.fiap.techfood.exceptions.ResourceAlreadyExistsException;
import br.com.fiap.techfood.model.User;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class PatchUserUseCase {

    private final UserGateway userGateway;
    private final ApplicationUserMapper mapper;

    public PatchUserUseCase(UserGateway userGateway, ApplicationUserMapper mapper) {
        this.userGateway = userGateway;
        this.mapper = mapper;
    }

    public UserResult execute(UUID id, UserPatchCommand command) {
        User user = userGateway.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (command.email() != null && !command.email().equals(user.getEmail())) {
            if (userGateway.existsByEmail(command.email())) {
                throw new ResourceAlreadyExistsException("Email já cadastrado");
            }
            user.setEmail(command.email());
        }

        if (command.login() != null && !command.login().equals(user.getLogin())) {
            if (userGateway.existsByLogin(command.login())) {
                throw new ResourceAlreadyExistsException("Login já cadastrado");
            }
            user.setLogin(command.login());
        }

        if (command.name() != null) {
            user.setName(command.name());
        }

        if (command.address() != null) {
            user.setAddress(mapper.toModel(command.address()));
        }

        user = userGateway.save(user);
        return mapper.toResult(user);
    }
}
