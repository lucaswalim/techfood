package br.com.fiap.techfood.application.gateway;

import br.com.fiap.techfood.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserGateway {

    boolean existsById(UUID id);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    Optional<User> findById(UUID id);

    Optional<User> findByLogin(String login);

    User save(User user);

    void deleteById(UUID id);

    PageResult<User> searchByName(String name, PageQuery query);
}
