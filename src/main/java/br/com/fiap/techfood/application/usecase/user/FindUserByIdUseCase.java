package br.com.fiap.techfood.application.usecase.user;

import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.mapper.UserMapper;
import br.com.fiap.techfood.model.User;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class FindUserByIdUseCase {

    private final UserGateway userGateway;
    private final UserMapper mapper;

    public FindUserByIdUseCase(UserGateway userGateway, UserMapper mapper) {
        this.userGateway = userGateway;
        this.mapper = mapper;
    }

    public UserResponseDTO execute(UUID id) {
        User user = userGateway.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        return mapper.toResponse(user);
    }
}
