package br.com.fiap.techfood.application.usecase.user;

import br.com.fiap.techfood.application.gateway.PageQuery;
import br.com.fiap.techfood.application.gateway.PageResult;
import br.com.fiap.techfood.application.gateway.PasswordEncoderGateway;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.application.usecase.createuser.CreateUserUseCase;
import br.com.fiap.techfood.dto.request.AddressDTO;
import br.com.fiap.techfood.dto.request.UserPatchDTO;
import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.exceptions.ResourceAlreadyExistsException;
import br.com.fiap.techfood.mapper.UserMapper;
import br.com.fiap.techfood.model.Address;
import br.com.fiap.techfood.model.Customer;
import br.com.fiap.techfood.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCasesTest {

    @InjectMocks
    private CreateUserUseCase createUserUseCase;
    @InjectMocks
    private PatchUserUseCase patchUserUseCase;
    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;
    @InjectMocks
    private SearchUsersByNameUseCase searchUsersByNameUseCase;
    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;
    @Mock
    private UserGateway userGateway;
    @Mock
    private UserMapper mapper;
    @Mock
    private PasswordEncoderGateway passwordEncoderGateway;

    private UserRequestDTO dto;
    private User user;
    private User savedUser;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        dto = mock(UserRequestDTO.class);
        user = mock(User.class);
        savedUser = mock(User.class);
        responseDTO = mock(UserResponseDTO.class);
    }

    private Customer buildCustomer(UUID id, String email, String login) {
        return Customer.builder()
                .id(id)
                .name("João Silva")
                .email(email)
                .login(login)
                .password("hashed-password")
                .address(new Address("Rua das Flores", "123", "São Paulo", "01310100"))
                .lastUpdatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    class CreateUserTests {
        @Test
        void shouldCreateUserSuccessfully() {
            when(dto.email()).thenReturn("test@email.com");
            when(dto.login()).thenReturn("testlogin");
            when(dto.password()).thenReturn("123456");
            when(userGateway.existsByEmail("test@email.com")).thenReturn(false);
            when(userGateway.existsByLogin("testlogin")).thenReturn(false);
            when(mapper.toEntity(dto)).thenReturn(user);
            when(passwordEncoderGateway.encode("123456")).thenReturn("hashed-password");
            when(userGateway.save(user)).thenReturn(savedUser);
            when(mapper.toResponse(savedUser)).thenReturn(responseDTO);

            UserResponseDTO result = createUserUseCase.execute(dto);

            assertThat(result).isEqualTo(responseDTO);
            verify(userGateway).save(user);
        }

        @Test
        void shouldThrowWhenEmailAlreadyExists() {
            when(dto.email()).thenReturn("test@email.com");
            when(userGateway.existsByEmail("test@email.com")).thenReturn(true);

            assertThatThrownBy(() -> createUserUseCase.execute(dto))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Email já cadastrado");
        }

        @Test
        void shouldThrowWhenLoginAlreadyExists() {
            when(dto.email()).thenReturn("test@email.com");
            when(dto.login()).thenReturn("testlogin");
            when(userGateway.existsByEmail("test@email.com")).thenReturn(false);
            when(userGateway.existsByLogin("testlogin")).thenReturn(true);

            assertThatThrownBy(() -> createUserUseCase.execute(dto))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Login já cadastrado");
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void shouldFindUserById() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            when(userGateway.findById(id)).thenReturn(Optional.of(customer));
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            UserResponseDTO result = findUserByIdUseCase.execute(id);

            assertThat(result).isEqualTo(responseDTO);
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            UUID id = UUID.randomUUID();
            when(userGateway.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> findUserByIdUseCase.execute(id))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("Usuário não encontrado");
        }
    }

    @Nested
    class SearchByNameTests {
        @Test
        void shouldReturnPageOfMatchingUsers() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            Pageable pageable = PageRequest.of(0, 10);
            PageResult<User> pageResult = new PageResult<>(List.of(customer), 1, 1, 0, 10, false, false);
            when(userGateway.searchByName(eq("João"), any(PageQuery.class))).thenReturn(pageResult);
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            Page<UserResponseDTO> result = searchUsersByNameUseCase.execute("João", pageable);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getTotalElements()).isEqualTo(1);
        }
    }

    @Nested
    class PatchUserTests {
        @Test
        void shouldPatchNameSuccessfully() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            UserPatchDTO patchDTO = new UserPatchDTO("Novo Nome", null, null, null);
            when(userGateway.findById(id)).thenReturn(Optional.of(customer));
            when(userGateway.save(customer)).thenReturn(customer);
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            UserResponseDTO result = patchUserUseCase.execute(id, patchDTO);

            assertThat(result).isEqualTo(responseDTO);
            assertThat(customer.getName()).isEqualTo("Novo Nome");
        }

        @Test
        void shouldThrowWhenNewEmailAlreadyExists() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            UserPatchDTO patchDTO = new UserPatchDTO(null, "existente@email.com", null, null);
            when(userGateway.findById(id)).thenReturn(Optional.of(customer));
            when(userGateway.existsByEmail("existente@email.com")).thenReturn(true);

            assertThatThrownBy(() -> patchUserUseCase.execute(id, patchDTO))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Email já cadastrado");
        }

        @Test
        void shouldPatchAddressSuccessfully() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            AddressDTO newAddress = new AddressDTO("Nova Rua", "456", "Rio de Janeiro", "20040020");
            UserPatchDTO patchDTO = new UserPatchDTO(null, null, null, newAddress);
            Address mappedAddress = new Address("Nova Rua", "456", "Rio de Janeiro", "20040020");
            when(userGateway.findById(id)).thenReturn(Optional.of(customer));
            when(mapper.toAddress(newAddress)).thenReturn(mappedAddress);
            when(userGateway.save(customer)).thenReturn(customer);
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            patchUserUseCase.execute(id, patchDTO);

            assertThat(customer.getAddress()).isEqualTo(mappedAddress);
        }
    }

    @Nested
    class DeleteUserTests {
        @Test
        void shouldDeleteUserSuccessfully() {
            UUID userId = UUID.randomUUID();
            when(userGateway.existsById(userId)).thenReturn(true);

            deleteUserUseCase.execute(userId);

            verify(userGateway).deleteById(userId);
        }

        @Test
        void shouldThrowWhenUserNotFoundOnDelete() {
            UUID userId = UUID.randomUUID();
            when(userGateway.existsById(userId)).thenReturn(false);

            assertThatThrownBy(() -> deleteUserUseCase.execute(userId))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("Usuário não encontrado");

            verify(userGateway, never()).deleteById(any());
        }
    }
}
