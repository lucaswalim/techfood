package br.com.fiap.techfood.controller;

import br.com.fiap.techfood.dto.request.AddressDTO;
import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.dto.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.model.UserType;
import br.com.fiap.techfood.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController controller;
    @Mock
    private UserService service;

    private UserRequestDTO buildRequest() {
        AddressDTO address = new AddressDTO(
                "Rua das Flores",
                "123",
                "São Paulo",
                "12345678"
        );

        return new UserRequestDTO(
                "João Silva",
                "joao@email.com",
                "joao123",
                "123456",
                address,
                UserType.RESTAURANT_OWNER
        );
    }

    private UserResponseDTO buildResponse() {
        return new UserResponseDTO(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "joao123",
                new AddressDTO(
                        "Rua das Flores",
                        "123",
                        "São Paulo",
                        "12345678"
                )
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserRequestDTO request = buildRequest();
        UserResponseDTO responseDTO = buildResponse();

        when(service.create(request)).thenReturn(responseDTO);

        ApiSuccessResponse<UserResponseDTO> response = controller.create(request);

        assertThat(response).isNotNull();
        assertThat(response.message()).isEqualTo("Usuário criado com sucesso");
        assertThat(response.data()).isEqualTo(responseDTO);
        verify(service, times(1)).create(request);
    }

}