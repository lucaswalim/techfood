package br.com.fiap.techfood.application.usecase.user;

import br.com.fiap.techfood.application.gateway.PageQuery;
import br.com.fiap.techfood.application.gateway.PageResult;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.mapper.UserMapper;
import br.com.fiap.techfood.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchUsersByNameUseCase {

    private final UserGateway userGateway;
    private final UserMapper mapper;

    public SearchUsersByNameUseCase(UserGateway userGateway, UserMapper mapper) {
        this.userGateway = userGateway;
        this.mapper = mapper;
    }

    public Page<UserResponseDTO> execute(String name, Pageable pageable) {
        PageResult<User> result = userGateway.searchByName(
                name,
                new PageQuery(pageable.getPageNumber(), pageable.getPageSize())
        );

        List<UserResponseDTO> content = result.content().stream()
                .map(mapper::toResponse)
                .toList();

        return new PageImpl<>(content, pageable, result.totalElements());
    }
}
