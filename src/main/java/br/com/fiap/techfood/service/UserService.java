package br.com.fiap.techfood.service;

import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.exceptions.ResourceAlreadyExistsException;
import br.com.fiap.techfood.mapper.UserMapper;
import br.com.fiap.techfood.model.User;
import br.com.fiap.techfood.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, UserMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * <p>O método realiza as seguintes operações:</p>
     * <ol>
     *     <li>Valida se o e-mail já está cadastrado e lança {@link ResourceAlreadyExistsException} se positivo.</li>
     *     <li>Valida se o login já está cadastrado e lança {@link ResourceAlreadyExistsException} se positivo.</li>
     *     <li>Converte o DTO {@link UserRequestDTO} para a entidade {@link User}.</li>
     *     <li>Codifica a senha utilizando BCrypt.</li>
     *     <li>Salva o usuário no banco de dados.</li>
     *     <li>Converte a entidade salva para {@link UserResponseDTO} e retorna.</li>
     * </ol>
     *
     * @param dto dados do usuário a serem criados
     * @return {@link UserResponseDTO} com os dados do usuário criado
     * @throws ResourceAlreadyExistsException se o e-mail ou login já estiverem cadastrados
     */
    public UserResponseDTO create(UserRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new ResourceAlreadyExistsException("Email já cadastrado");
        }

        if (repository.existsByLogin(dto.login())) {
            throw new ResourceAlreadyExistsException("Login já cadastrado");
        }

        User user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user = repository.save(user);

        return mapper.toResponse(user);
    }
}
