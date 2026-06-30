package br.com.fiap.techfood.service;

import br.com.fiap.techfood.application.dto.UserCreateCommand;
import br.com.fiap.techfood.application.dto.UserPatchCommand;
import br.com.fiap.techfood.application.dto.UserResult;
import br.com.fiap.techfood.application.usecase.createuser.CreateUserUseCase;
import br.com.fiap.techfood.application.usecase.user.DeleteUserUseCase;
import br.com.fiap.techfood.application.usecase.user.FindUserByIdUseCase;
import br.com.fiap.techfood.application.usecase.user.PatchUserUseCase;
import br.com.fiap.techfood.application.usecase.user.SearchUsersByNameUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final CreateUserUseCase createUserUseCase;
    private final PatchUserUseCase patchUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final SearchUsersByNameUseCase searchUsersByNameUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserServiceImpl(CreateUserUseCase createUserUseCase,
                           PatchUserUseCase patchUserUseCase,
                           FindUserByIdUseCase findUserByIdUseCase,
                           SearchUsersByNameUseCase searchUsersByNameUseCase,
                           DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.patchUserUseCase = patchUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.searchUsersByNameUseCase = searchUsersByNameUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    public UserResult create(UserCreateCommand command) {
        return createUserUseCase.execute(command);
    }

    public UserResult patch(UUID id, UserPatchCommand command) {
        return patchUserUseCase.execute(id, command);
    }

    public UserResult findById(UUID id) {
        return findUserByIdUseCase.execute(id);
    }

    public Page<UserResult> searchByName(String name, Pageable pageable) {
        return searchUsersByNameUseCase.execute(name, pageable);
    }

    public void delete(UUID id) {
        deleteUserUseCase.execute(id);
    }
}
