package br.com.fiap.techfood.application.usecase.user;

import br.com.fiap.techfood.application.dto.UserResult;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.application.mapper.ApplicationUserMapper;
import br.com.fiap.techfood.domain.user.User;

import java.util.NoSuchElementException;
import java.util.UUID;

public class FindUserByIdUseCase {

    private final UserGateway userGateway;
    private final ApplicationUserMapper mapper;

    public FindUserByIdUseCase(UserGateway userGateway, ApplicationUserMapper mapper) {
        this.userGateway = userGateway;
        this.mapper = mapper;
    }

    public UserResult execute(UUID id) {
        User user = userGateway.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        return mapper.toResult(user);
    }
}
