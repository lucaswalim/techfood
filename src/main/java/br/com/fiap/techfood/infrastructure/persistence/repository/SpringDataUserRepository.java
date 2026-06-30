package br.com.fiap.techfood.infrastructure.persistence.repository;

import br.com.fiap.techfood.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {

    Page<UserEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<UserEntity> findByLogin(String login);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);
}
