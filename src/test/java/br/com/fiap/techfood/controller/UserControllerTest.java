package br.com.fiap.techfood.controller;

import br.com.fiap.techfood.dto.request.AddressDTO;
import br.com.fiap.techfood.dto.request.UserPatchDTO;
import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.dto.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.model.UserType;
import br.com.fiap.techfood.service.UserService;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController controller;
    @Mock
    private UserService service;

    private AddressDTO buildAddress() {
        return new AddressDTO("Rua das Flores", "123", "São Paulo", "12345678");
    }

    private UserRequestDTO buildRequest() {
        return new UserRequestDTO("João Silva", "joao@email.com", "joao123", "123456",
                buildAddress(), UserType.RESTAURANT_OWNER);
    }

    private UserResponseDTO buildResponse() {
        return new UserResponseDTO(UUID.randomUUID(), "João Silva", "joao@email.com", "joao123",
                UserType.RESTAURANT_OWNER, buildAddress(), LocalDateTime.now());
    }

    @Nested
    class FindByIdTests {
        @Test
        void shouldReturnUserById() {
            UUID id = UUID.randomUUID();
            UserResponseDTO responseDTO = buildResponse();
            when(service.findById(id)).thenReturn(responseDTO);

            ResponseEntity<ApiSuccessResponse<UserResponseDTO>> response = controller.findById(id);

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody().data()).isEqualTo(responseDTO);
            verify(service).findById(id);
        }
    }

    @Nested
    class SearchByNameTests {
        @Test
        void shouldReturnSearchResults() {
            UserResponseDTO responseDTO = buildResponse();
            Page<UserResponseDTO> page = new PageImpl<>(List.of(responseDTO), PageRequest.of(0, 10), 1);
            when(service.searchByName(eq("João"), any())).thenReturn(page);

            ResponseEntity<ApiSuccessResponse<List<UserResponseDTO>>> response =
                    controller.searchByName("João", 0, 10);

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody().data()).hasSize(1);
            assertThat(response.getBody().meta().totalElements()).isEqualTo(1);
            verify(service).searchByName(eq("João"), any());
        }

        @Test
        void shouldReturnEmptyListWhenNoMatch() {
            Page<UserResponseDTO> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
            when(service.searchByName(eq("Desconhecido"), any())).thenReturn(emptyPage);

            ResponseEntity<ApiSuccessResponse<List<UserResponseDTO>>> response =
                    controller.searchByName("Desconhecido", 0, 10);

            assertThat(response.getBody().data()).isEmpty();
            assertThat(response.getBody().meta().totalElements()).isZero();
        }
    }

    @Nested
    class CreateUserTests {
        @Test
        void shouldCreateUserSuccessfully() {
            UserRequestDTO request = buildRequest();
            UserResponseDTO responseDTO = buildResponse();
            when(service.create(request)).thenReturn(responseDTO);

            ResponseEntity<ApiSuccessResponse<UserResponseDTO>> response = controller.create(request);

            assertThat(response.getStatusCode().value()).isEqualTo(201);
            assertThat(response.getBody().message()).isEqualTo("Usuário criado com sucesso");
            assertThat(response.getBody().data()).isEqualTo(responseDTO);
            verify(service, times(1)).create(request);
        }
    }

    @Nested
    class PatchUserTests {
        @Test
        void shouldPatchUserSuccessfully() {
            UUID id = UUID.randomUUID();
            UserPatchDTO patchDTO = new UserPatchDTO("Novo Nome", null, null, null);
            UserResponseDTO responseDTO = buildResponse();
            when(service.patch(id, patchDTO)).thenReturn(responseDTO);

            ResponseEntity<ApiSuccessResponse<UserResponseDTO>> response = controller.patch(id, patchDTO);

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody().data()).isEqualTo(responseDTO);
            verify(service).patch(id, patchDTO);
        }
    }

    @Nested
    class DeleteUserTests {
        @Test
        void shouldDeleteUserSuccessfully() {
            UUID id = UUID.randomUUID();
            doNothing().when(service).delete(id);

            ResponseEntity<Void> response = controller.delete(id);

            assertThat(response.getStatusCode().value()).isEqualTo(204);
            verify(service, times(1)).delete(id);
        }
    }
}
