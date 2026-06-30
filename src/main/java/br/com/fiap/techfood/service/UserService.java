package br.com.fiap.techfood.service;

import br.com.fiap.techfood.application.dto.UserCreateCommand;
import br.com.fiap.techfood.application.dto.UserPatchCommand;
import br.com.fiap.techfood.application.dto.UserResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    UserResult create(UserCreateCommand command);

    UserResult patch(UUID id, UserPatchCommand command);

    UserResult findById(UUID id);

    Page<UserResult> searchByName(String name, Pageable pageable);

    void delete(UUID id);
}
