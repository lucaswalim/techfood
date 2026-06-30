package br.com.fiap.techfood.application.usecase.createuser;

import br.com.fiap.techfood.application.dto.UserCreateCommand;
import br.com.fiap.techfood.application.dto.UserResult;
import br.com.fiap.techfood.application.gateway.PasswordEncoderGateway;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.application.mapper.ApplicationUserMapper;
import br.com.fiap.techfood.application.exceptions.ResourceAlreadyExistsException;
import br.com.fiap.techfood.domain.user.User;

public class CreateUserUseCase {

    private final UserGateway userGateway;
    private final ApplicationUserMapper mapper;
    private final PasswordEncoderGateway passwordEncoderGateway;

    public CreateUserUseCase(UserGateway userGateway, ApplicationUserMapper mapper, PasswordEncoderGateway passwordEncoderGateway) {
        this.userGateway = userGateway;
        this.mapper = mapper;
        this.passwordEncoderGateway = passwordEncoderGateway;
    }

    public UserResult execute(UserCreateCommand command) {
        if (userGateway.existsByEmail(command.email())) {
            throw new ResourceAlreadyExistsException("Email já cadastrado");
        }

        if (userGateway.existsByLogin(command.login())) {
            throw new ResourceAlreadyExistsException("Login já cadastrado");
        }

        User user = mapper.toModel(command);
        user.setPassword(passwordEncoderGateway.encode(command.password()));
        user = userGateway.save(user);

        return mapper.toResult(user);
    }
}
