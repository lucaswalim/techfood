package br.com.fiap.techfood.controller;

import br.com.fiap.techfood.dto.request.ChangePasswordDTO;
import br.com.fiap.techfood.dto.request.LoginRequestDTO;
import br.com.fiap.techfood.dto.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.service.AuthService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController controller;
    @Mock
    private AuthService service;

    @Nested
    class LoginTests {
        @Test
        void shouldLoginSuccessfully() {
            LoginRequestDTO dto = new LoginRequestDTO("user", "senha");
            doNothing().when(service).login(dto);

            ResponseEntity<ApiSuccessResponse<String>> response = controller.login(dto);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode().value()).isEqualTo(200);

            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().message()).isEqualTo("Usuário autenticado com sucesso");

            verify(service, times(1)).login(dto);
        }
    }

    @Nested
    class ChangePasswordTests {
        @Test
        void shouldChangePasswordSuccessfully() {
            UUID userId = UUID.randomUUID();
            ChangePasswordDTO dto = new ChangePasswordDTO("user", "oldPass", "newPass");
            doNothing().when(service).changePassword(userId, dto);

            ResponseEntity<ApiSuccessResponse<String>> response = controller.changePassword(userId, dto);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode().value()).isEqualTo(200);

            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().message()).isEqualTo("Senha do usuário user alterada com sucesso");

            verify(service, times(1)).changePassword(userId, dto);
        }
    }
}