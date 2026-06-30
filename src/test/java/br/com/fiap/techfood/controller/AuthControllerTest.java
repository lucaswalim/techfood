package br.com.fiap.techfood.controller;

import br.com.fiap.techfood.adapters.controller.AuthController;
import br.com.fiap.techfood.adapters.mapper.AdapterMapper;
import br.com.fiap.techfood.adapters.presenter.ApiResponseFactory;
import br.com.fiap.techfood.adapters.request.ChangePasswordRequest;
import br.com.fiap.techfood.adapters.request.LoginRequest;
import br.com.fiap.techfood.adapters.response.TokenResponse;
import br.com.fiap.techfood.adapters.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.application.dto.ChangePasswordCommand;
import br.com.fiap.techfood.application.dto.LoginCommand;
import br.com.fiap.techfood.application.dto.TokenResult;
import br.com.fiap.techfood.application.usecase.auth.ChangePasswordUseCase;
import br.com.fiap.techfood.application.usecase.login.LoginUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController controller;
    @Mock
    private LoginUseCase loginUseCase;
    @Mock
    private ChangePasswordUseCase changePasswordUseCase;
    @Mock
    private AdapterMapper adapterMapper;
    @Mock
    private ApiResponseFactory presenter;

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("user", "senha");
        LoginCommand command = new LoginCommand("user", "senha");
        TokenResult result = new TokenResult("fake-jwt-token");
        TokenResponse responseBody = new TokenResponse("fake-jwt-token", "Bearer");
        ResponseEntity<ApiSuccessResponse<TokenResponse>> response = ApiSuccessResponse.ok(responseBody, "Usuário autenticado com sucesso");
        when(adapterMapper.toCommand(request)).thenReturn(command);
        when(loginUseCase.execute(command)).thenReturn(result);
        when(adapterMapper.toResponse(result)).thenReturn(responseBody);
        when(presenter.ok(responseBody, "Usuário autenticado com sucesso")).thenReturn(response);

        ResponseEntity<ApiSuccessResponse<TokenResponse>> actual = controller.login(request);

        assertThat(actual.getStatusCode().value()).isEqualTo(200);
        verify(loginUseCase).execute(command);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest request = new ChangePasswordRequest("user", "oldPass", "newPass");
        ChangePasswordCommand command = new ChangePasswordCommand("user", "oldPass", "newPass");
        ResponseEntity<ApiSuccessResponse<String>> response = ApiSuccessResponse.okMessage("Senha alterada com sucesso");
        when(adapterMapper.toCommand(request)).thenReturn(command);
        doNothing().when(changePasswordUseCase).execute(userId, command);
        when(presenter.okMessage("Senha alterada com sucesso")).thenReturn(response);

        ResponseEntity<ApiSuccessResponse<String>> actual = controller.changePassword(userId, request);

        assertThat(actual.getStatusCode().value()).isEqualTo(200);
        verify(changePasswordUseCase).execute(userId, command);
    }
}
