package br.com.fiap.techfood.application.usecase.createuser;

import br.com.fiap.techfood.application.gateway.PasswordEncoderGateway;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.exceptions.ResourceAlreadyExistsException;
import br.com.fiap.techfood.mapper.UserMapper;
import br.com.fiap.techfood.model.User;
import org.springframework.stereotype.Component;

@Component
public class CreateUserUseCase {

    private final UserGateway userGateway;
    private final UserMapper mapper;
    private final PasswordEncoderGateway passwordEncoderGateway;

    public CreateUserUseCase(UserGateway userGateway, UserMapper mapper, PasswordEncoderGateway passwordEncoderGateway) {
        this.userGateway = userGateway;
        this.mapper = mapper;
        this.passwordEncoderGateway = passwordEncoderGateway;
    }

    public UserResponseDTO execute(UserRequestDTO dto) {
        if (userGateway.existsByEmail(dto.email())) {
            throw new ResourceAlreadyExistsException("Email já cadastrado");
        }

        if (userGateway.existsByLogin(dto.login())) {
            throw new ResourceAlreadyExistsException("Login já cadastrado");
        }

        User user = mapper.toEntity(dto);
        user.setPassword(passwordEncoderGateway.encode(dto.password()));
        user = userGateway.save(user);

        return mapper.toResponse(user);
    }
}
