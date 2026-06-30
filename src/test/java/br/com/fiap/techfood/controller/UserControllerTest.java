package br.com.fiap.techfood.controller;

import br.com.fiap.techfood.adapters.controller.UserController;
import br.com.fiap.techfood.adapters.mapper.AdapterMapper;
import br.com.fiap.techfood.adapters.presenter.ApiResponseFactory;
import br.com.fiap.techfood.adapters.request.UserPatchRequest;
import br.com.fiap.techfood.adapters.request.UserRequest;
import br.com.fiap.techfood.adapters.response.UserResponse;
import br.com.fiap.techfood.adapters.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.application.dto.UserCreateCommand;
import br.com.fiap.techfood.application.dto.UserPatchCommand;
import br.com.fiap.techfood.application.dto.UserResult;
import br.com.fiap.techfood.application.usecase.createuser.CreateUserUseCase;
import br.com.fiap.techfood.application.usecase.user.DeleteUserUseCase;
import br.com.fiap.techfood.application.usecase.user.FindUserByIdUseCase;
import br.com.fiap.techfood.application.usecase.user.PatchUserUseCase;
import br.com.fiap.techfood.application.usecase.user.SearchUsersByNameUseCase;
import br.com.fiap.techfood.model.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController controller;
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
    @Mock
    private AdapterMapper adapterMapper;
    @Mock
    private ApiResponseFactory presenter;

    @Test
    void shouldReturnUserById() {
        UUID id = UUID.randomUUID();
        UserResult result = new UserResult(id, "João Silva", "joao@email.com", "joao123", UserType.RESTAURANT_OWNER, null, LocalDateTime.now());
        UserResponse responseBody = new UserResponse(id, "João Silva", "joao@email.com", "joao123", UserType.RESTAURANT_OWNER, null, result.lastUpdatedAt());
        ResponseEntity<ApiSuccessResponse<UserResponse>> response = ApiSuccessResponse.ok(responseBody);
        when(findUserByIdUseCase.execute(id)).thenReturn(result);
        when(adapterMapper.toResponse(result)).thenReturn(responseBody);
        when(presenter.ok(responseBody)).thenReturn(response);

        ResponseEntity<ApiSuccessResponse<UserResponse>> actual = controller.findById(id);

        assertThat(actual.getStatusCode().value()).isEqualTo(200);
        verify(findUserByIdUseCase).execute(id);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserRequest request = new UserRequest("João Silva", "joao@email.com", "joao123", "123456", null, UserType.RESTAURANT_OWNER);
        UserCreateCommand command = new UserCreateCommand("João Silva", "joao@email.com", "joao123", "123456", null, UserType.RESTAURANT_OWNER);
        UserResult result = new UserResult(UUID.randomUUID(), "João Silva", "joao@email.com", "joao123", UserType.RESTAURANT_OWNER, null, LocalDateTime.now());
        UserResponse responseBody = new UserResponse(result.id(), result.name(), result.email(), result.login(), result.type(), null, result.lastUpdatedAt());
        ResponseEntity<ApiSuccessResponse<UserResponse>> response = ApiSuccessResponse.created(responseBody, "Usuário criado com sucesso");
        when(adapterMapper.toCommand(request)).thenReturn(command);
        when(createUserUseCase.execute(command)).thenReturn(result);
        when(adapterMapper.toResponse(result)).thenReturn(responseBody);
        when(presenter.created(responseBody, "Usuário criado com sucesso")).thenReturn(response);

        ResponseEntity<ApiSuccessResponse<UserResponse>> actual = controller.create(request);

        assertThat(actual.getStatusCode().value()).isEqualTo(201);
        verify(createUserUseCase).execute(command);
    }

    @Test
    void shouldPatchUserSuccessfully() {
        UUID id = UUID.randomUUID();
        UserPatchRequest request = new UserPatchRequest("Novo Nome", null, null, null);
        UserPatchCommand command = new UserPatchCommand("Novo Nome", null, null, null);
        UserResult result = new UserResult(id, "Novo Nome", "joao@email.com", "joao123", UserType.RESTAURANT_OWNER, null, LocalDateTime.now());
        UserResponse responseBody = new UserResponse(id, "Novo Nome", "joao@email.com", "joao123", UserType.RESTAURANT_OWNER, null, result.lastUpdatedAt());
        ResponseEntity<ApiSuccessResponse<UserResponse>> response = ApiSuccessResponse.ok(responseBody);
        when(adapterMapper.toCommand(request)).thenReturn(command);
        when(patchUserUseCase.execute(id, command)).thenReturn(result);
        when(adapterMapper.toResponse(result)).thenReturn(responseBody);
        when(presenter.ok(responseBody)).thenReturn(response);

        ResponseEntity<ApiSuccessResponse<UserResponse>> actual = controller.patch(id, request);

        assertThat(actual.getStatusCode().value()).isEqualTo(200);
        verify(patchUserUseCase).execute(id, command);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        UUID id = UUID.randomUUID();
        ResponseEntity<Void> response = ApiSuccessResponse.noContent();
        doNothing().when(deleteUserUseCase).execute(id);
        when(presenter.noContent()).thenReturn(response);

        ResponseEntity<Void> actual = controller.delete(id);

        assertThat(actual.getStatusCode().value()).isEqualTo(204);
        verify(deleteUserUseCase).execute(id);
    }

    @Test
    void shouldReturnSearchResults() {
        UserResult result = new UserResult(UUID.randomUUID(), "João Silva", "joao@email.com", "joao123", UserType.RESTAURANT_OWNER, null, LocalDateTime.now());
        UserResponse responseItem = new UserResponse(result.id(), result.name(), result.email(), result.login(), result.type(), null, result.lastUpdatedAt());
        Page<UserResult> page = new PageImpl<>(List.of(result), PageRequest.of(0, 10), 1);
        Page<UserResponse> mappedPage = new PageImpl<>(List.of(responseItem), PageRequest.of(0, 10), 1);
        ResponseEntity<ApiSuccessResponse<List<UserResponse>>> response = ApiSuccessResponse.ok(mappedPage.getContent(), br.com.fiap.techfood.adapters.response.api.Meta.from(mappedPage));
        when(searchUsersByNameUseCase.execute("João", PageRequest.of(0, 10))).thenReturn(page);
        when(adapterMapper.toResponse(result)).thenReturn(responseItem);
        when(presenter.okPage(org.mockito.ArgumentMatchers.<Page<UserResponse>>any())).thenReturn(response);

        ResponseEntity<ApiSuccessResponse<List<UserResponse>>> actual = controller.searchByName("João", 0, 10);

        assertThat(actual.getStatusCode().value()).isEqualTo(200);
        verify(searchUsersByNameUseCase).execute("João", PageRequest.of(0, 10));
    }
}
