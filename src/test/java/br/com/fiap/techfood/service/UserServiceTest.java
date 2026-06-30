package br.com.fiap.techfood.service;

import br.com.fiap.techfood.application.dto.UserCreateCommand;
import br.com.fiap.techfood.application.dto.UserPatchCommand;
import br.com.fiap.techfood.application.dto.UserResult;
import br.com.fiap.techfood.application.usecase.createuser.CreateUserUseCase;
import br.com.fiap.techfood.application.usecase.user.DeleteUserUseCase;
import br.com.fiap.techfood.application.usecase.user.FindUserByIdUseCase;
import br.com.fiap.techfood.application.usecase.user.PatchUserUseCase;
import br.com.fiap.techfood.application.usecase.user.SearchUsersByNameUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl service;
    @Mock
    private CreateUserUseCase createUserUseCase;
    @Mock
    private PatchUserUseCase patchUserUseCase;
    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;
    @Mock
    private SearchUsersByNameUseCase searchUsersByNameUseCase;
    @Mock
    private DeleteUserUseCase deleteUserUseCase;

    @Test
    void shouldDelegateCreate() {
        UserCreateCommand command = new UserCreateCommand("João", "joao@email.com", "joao123", "123456", null, null);
        UserResult response = new UserResult(UUID.randomUUID(), "João", "joao@email.com", "joao123", null, null, null);
        when(createUserUseCase.execute(command)).thenReturn(response);

        UserResult result = service.create(command);

        assertThat(result).isEqualTo(response);
        verify(createUserUseCase).execute(command);
    }

    @Test
    void shouldDelegatePatch() {
        UUID id = UUID.randomUUID();
        UserPatchCommand command = new UserPatchCommand("Novo Nome", null, null, null);
        UserResult response = new UserResult(id, "Novo Nome", "joao@email.com", "joao123", null, null, null);
        when(patchUserUseCase.execute(id, command)).thenReturn(response);

        UserResult result = service.patch(id, command);

        assertThat(result).isEqualTo(response);
        verify(patchUserUseCase).execute(id, command);
    }

    @Test
    void shouldDelegateFindById() {
        UUID id = UUID.randomUUID();
        UserResult response = new UserResult(id, "João", "joao@email.com", "joao123", null, null, null);
        when(findUserByIdUseCase.execute(id)).thenReturn(response);

        UserResult result = service.findById(id);

        assertThat(result).isEqualTo(response);
        verify(findUserByIdUseCase).execute(id);
    }

    @Test
    void shouldDelegateSearchByName() {
        Page<UserResult> page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(searchUsersByNameUseCase.execute("João", PageRequest.of(0, 10))).thenReturn(page);

        Page<UserResult> result = service.searchByName("João", PageRequest.of(0, 10));

        assertThat(result).isEqualTo(page);
        verify(searchUsersByNameUseCase).execute("João", PageRequest.of(0, 10));
    }

    @Test
    void shouldDelegateDelete() {
        UUID id = UUID.randomUUID();
        doNothing().when(deleteUserUseCase).execute(id);

        service.delete(id);

        verify(deleteUserUseCase).execute(id);
    }
}
