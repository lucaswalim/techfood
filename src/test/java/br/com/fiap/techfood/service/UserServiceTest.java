package br.com.fiap.techfood.service;

import br.com.fiap.techfood.dto.request.AddressDTO;
import br.com.fiap.techfood.dto.request.UserPatchDTO;
import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.exceptions.ResourceAlreadyExistsException;
import br.com.fiap.techfood.mapper.UserMapper;
import br.com.fiap.techfood.model.Address;
import br.com.fiap.techfood.model.Customer;
import br.com.fiap.techfood.model.User;
import br.com.fiap.techfood.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl service;
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

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

            when(repository.existsByEmail("test@email.com")).thenReturn(false);
            when(repository.existsByLogin("testlogin")).thenReturn(false);

            when(mapper.toEntity(dto)).thenReturn(user);
            when(passwordEncoder.encode("123456")).thenReturn("hashed-password");
            when(repository.save(user)).thenReturn(savedUser);
            when(mapper.toResponse(savedUser)).thenReturn(responseDTO);

            UserResponseDTO result = service.create(dto);

            assertThat(result).isNotNull().isEqualTo(responseDTO);
            verify(repository).existsByEmail("test@email.com");
            verify(repository).existsByLogin("testlogin");
            verify(passwordEncoder).encode("123456");
            verify(repository).save(user);
            verify(mapper).toResponse(savedUser);
        }

        @Test
        void shouldThrowWhenEmailAlreadyExists() {
            when(dto.email()).thenReturn("test@email.com");
            when(repository.existsByEmail("test@email.com")).thenReturn(true);

            assertThatThrownBy(() -> service.create(dto))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Email já cadastrado");

            verify(repository, never()).save(any());
            verify(repository, never()).existsByLogin(any());
        }

        @Test
        void shouldThrowWhenLoginAlreadyExists() {
            when(dto.email()).thenReturn("test@email.com");
            when(dto.login()).thenReturn("testlogin");

            when(repository.existsByEmail("test@email.com")).thenReturn(false);
            when(repository.existsByLogin("testlogin")).thenReturn(true);

            assertThatThrownBy(() -> service.create(dto))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Login já cadastrado");

            verify(repository, never()).save(any());
        }

        @Test
        void shouldEncodePasswordBeforeSaving() {
            when(dto.email()).thenReturn("test@email.com");
            when(dto.login()).thenReturn("testlogin");
            when(dto.password()).thenReturn("123456");

            when(repository.existsByEmail(any())).thenReturn(false);
            when(repository.existsByLogin(any())).thenReturn(false);
            when(mapper.toEntity(dto)).thenReturn(user);
            when(passwordEncoder.encode("123456")).thenReturn("encrypted");
            when(repository.save(user)).thenReturn(savedUser);
            when(mapper.toResponse(savedUser)).thenReturn(responseDTO);

            service.create(dto);

            verify(passwordEncoder).encode("123456");
            verify(user).setPassword("encrypted");
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void shouldFindUserById() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            when(repository.findById(id)).thenReturn(Optional.of(customer));
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            UserResponseDTO result = service.findById(id);

            assertThat(result).isEqualTo(responseDTO);
            verify(repository).findById(id);
            verify(mapper).toResponse(customer);
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            UUID id = UUID.randomUUID();
            when(repository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findById(id))
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
            Page<User> userPage = new PageImpl<>(List.of(customer), pageable, 1);

            when(repository.findByNameContainingIgnoreCase("João", pageable)).thenReturn(userPage);
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            Page<UserResponseDTO> result = service.searchByName("João", pageable);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getTotalElements()).isEqualTo(1);
            verify(repository).findByNameContainingIgnoreCase("João", pageable);
        }

        @Test
        void shouldReturnEmptyPageWhenNoMatch() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<User> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(repository.findByNameContainingIgnoreCase("Desconhecido", pageable)).thenReturn(emptyPage);

            Page<UserResponseDTO> result = service.searchByName("Desconhecido", pageable);

            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isZero();
        }
    }

    @Nested
    class PatchUserTests {
        @Test
        void shouldPatchNameSuccessfully() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            UserPatchDTO patchDTO = new UserPatchDTO("Novo Nome", null, null, null);

            when(repository.findById(id)).thenReturn(Optional.of(customer));
            when(repository.save(customer)).thenReturn(customer);
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            UserResponseDTO result = service.patch(id, patchDTO);

            assertThat(result).isEqualTo(responseDTO);
            assertThat(customer.getName()).isEqualTo("Novo Nome");
            verify(repository).save(customer);
        }

        @Test
        void shouldPatchLoginSuccessfullyWhenLoginIsDiferent() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            UserPatchDTO patchDTO = new UserPatchDTO(null, null, "novoLogin", null);

            when(repository.findById(id)).thenReturn(Optional.of(customer));
            when(repository.existsByLogin("novoLogin")).thenReturn(false);
            when(repository.save(customer)).thenReturn(customer);
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            service.patch(id, patchDTO);

            assertThat(customer.getLogin()).isEqualTo("novoLogin");
            verify(repository).existsByLogin("novoLogin");
            verify(repository).save(customer);
        }

        @Test
        void shouldNotCheckLoginConflictWhenLoginIsSame() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            UserPatchDTO patchDTO = new UserPatchDTO(null, null, "joao123", null);

            when(repository.findById(id)).thenReturn(Optional.of(customer));
            when(repository.save(customer)).thenReturn(customer);
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            service.patch(id, patchDTO);

            assertThat(customer.getLogin()).isEqualTo("joao123");
            verify(repository, never()).existsByLogin(any());
            verify(repository).save(customer);
        }

        @Test
        void shouldPatchEmailSuccessfully() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            UserPatchDTO patchDTO = new UserPatchDTO(null, "novo@email.com", null, null);

            when(repository.findById(id)).thenReturn(Optional.of(customer));
            when(repository.existsByEmail("novo@email.com")).thenReturn(false);
            when(repository.save(customer)).thenReturn(customer);
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            service.patch(id, patchDTO);

            assertThat(customer.getEmail()).isEqualTo("novo@email.com");
            verify(repository).existsByEmail("novo@email.com");
        }

        @Test
        void shouldNotCheckEmailConflictWhenEmailUnchanged() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            UserPatchDTO patchDTO = new UserPatchDTO(null, "joao@email.com", null, null);

            when(repository.findById(id)).thenReturn(Optional.of(customer));
            when(repository.save(customer)).thenReturn(customer);
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            service.patch(id, patchDTO);

            verify(repository, never()).existsByEmail(any());
        }

        @Test
        void shouldThrowWhenNewEmailAlreadyExists() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            UserPatchDTO patchDTO = new UserPatchDTO(null, "existente@email.com", null, null);

            when(repository.findById(id)).thenReturn(Optional.of(customer));
            when(repository.existsByEmail("existente@email.com")).thenReturn(true);

            assertThatThrownBy(() -> service.patch(id, patchDTO))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Email já cadastrado");

            verify(repository, never()).save(any());
        }

        @Test
        void shouldThrowWhenNewLoginAlreadyExists() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            UserPatchDTO patchDTO = new UserPatchDTO(null, null, "loginexistente", null);

            when(repository.findById(id)).thenReturn(Optional.of(customer));
            when(repository.existsByLogin("loginexistente")).thenReturn(true);

            assertThatThrownBy(() -> service.patch(id, patchDTO))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Login já cadastrado");

            verify(repository, never()).save(any());
        }

        @Test
        void shouldThrowWhenUserNotFoundOnPatch() {
            UUID id = UUID.randomUUID();
            UserPatchDTO patchDTO = new UserPatchDTO("Nome", null, null, null);
            when(repository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.patch(id, patchDTO))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("Usuário não encontrado");
        }

        @Test
        void shouldPatchAddressSuccessfully() {
            UUID id = UUID.randomUUID();
            Customer customer = buildCustomer(id, "joao@email.com", "joao123");
            AddressDTO newAddress = new AddressDTO("Nova Rua", "456", "Rio de Janeiro", "20040020");
            UserPatchDTO patchDTO = new UserPatchDTO(null, null, null, newAddress);
            Address mappedAddress = new Address("Nova Rua", "456", "Rio de Janeiro", "20040020");

            when(repository.findById(id)).thenReturn(Optional.of(customer));
            when(mapper.toAddress(newAddress)).thenReturn(mappedAddress);
            when(repository.save(customer)).thenReturn(customer);
            when(mapper.toResponse(customer)).thenReturn(responseDTO);

            service.patch(id, patchDTO);

            assertThat(customer.getAddress()).isEqualTo(mappedAddress);
            verify(mapper).toAddress(newAddress);
        }
    }

    @Nested
    class DeleteUserTests {

        UUID userId = UUID.randomUUID();

        @Test
        void shouldDeleteUserSuccessfully() {
            when(repository.existsById(userId)).thenReturn(true);

            service.delete(userId);

            verify(repository).deleteById(userId);
        }

        @Test
        void shouldThrowWhenUserNotFoundOnDelete() {
            when(repository.existsById(userId)).thenReturn(false);

            assertThatThrownBy(() -> service.delete(userId))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("Usuário não encontrado");

            verify(repository, never()).deleteById(any());
        }
    }
}
