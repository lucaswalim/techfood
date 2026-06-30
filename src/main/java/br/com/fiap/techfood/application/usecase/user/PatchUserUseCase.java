package br.com.fiap.techfood.application.usecase.user;

import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.dto.request.UserPatchDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.exceptions.ResourceAlreadyExistsException;
import br.com.fiap.techfood.mapper.UserMapper;
import br.com.fiap.techfood.model.User;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class PatchUserUseCase {

    private final UserGateway userGateway;
    private final UserMapper mapper;

    public PatchUserUseCase(UserGateway userGateway, UserMapper mapper) {
        this.userGateway = userGateway;
        this.mapper = mapper;
    }

    public UserResponseDTO execute(UUID id, UserPatchDTO dto) {
        User user = userGateway.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (dto.email() != null && !dto.email().equals(user.getEmail())) {
            if (userGateway.existsByEmail(dto.email())) {
                throw new ResourceAlreadyExistsException("Email já cadastrado");
            }
            user.setEmail(dto.email());
        }

        if (dto.login() != null && !dto.login().equals(user.getLogin())) {
            if (userGateway.existsByLogin(dto.login())) {
                throw new ResourceAlreadyExistsException("Login já cadastrado");
            }
            user.setLogin(dto.login());
        }

        if (dto.name() != null) {
            user.setName(dto.name());
        }

        if (dto.address() != null) {
            user.setAddress(mapper.toAddress(dto.address()));
        }

        user = userGateway.save(user);
        return mapper.toResponse(user);
    }
}
