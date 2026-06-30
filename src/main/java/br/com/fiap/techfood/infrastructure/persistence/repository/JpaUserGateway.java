package br.com.fiap.techfood.infrastructure.persistence.repository;

import br.com.fiap.techfood.application.gateway.PageQuery;
import br.com.fiap.techfood.application.gateway.PageResult;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.infrastructure.persistence.entity.UserEntity;
import br.com.fiap.techfood.infrastructure.persistence.mapper.PersistenceUserMapper;
import br.com.fiap.techfood.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JpaUserGateway implements UserGateway {

    private final SpringDataUserRepository repository;
    private final PersistenceUserMapper mapper;

    public JpaUserGateway(SpringDataUserRepository repository, PersistenceUserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login)
                .map(mapper::toModel);
    }

    @Override
    public User save(User user) {
        UserEntity saved = repository.save(mapper.toEntity(user));
        return mapper.toModel(saved);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public PageResult<User> searchByName(String name, PageQuery query) {
        Page<UserEntity> page = repository.findByNameContainingIgnoreCase(
                name,
                PageRequest.of(query.page(), query.size())
        );

        return new PageResult<>(
                page.getContent().stream().map(mapper::toModel).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
