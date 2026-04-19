package br.com.fiap.techfood.service;

import br.com.fiap.techfood.dto.request.ChangePasswordDTO;
import br.com.fiap.techfood.dto.request.LoginRequestDTO;
import br.com.fiap.techfood.exceptions.InvalidCredentialsException;
import br.com.fiap.techfood.model.User;
import br.com.fiap.techfood.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.passwordEncoder = encoder;
    }

    /**
     * Realiza o login de um usuário.
     *
     * <p>O método verifica se o login informado existe no sistema e se a senha fornecida
     * corresponde à senha armazenada no banco de dados codificada com BCrypt.</p>
     *
     * @param dto DTO contendo login e senha do usuário
     * @throws InvalidCredentialsException se o login não existir ou a senha estiver incorreta
     */
    public void login(LoginRequestDTO dto) {
        String login = dto.login().trim();

        User user = repository.findByLogin(login)
                .orElseThrow(() -> new InvalidCredentialsException("Usuário ou senha incorreta"));

        boolean matches = passwordEncoder.matches(dto.password(), user.getPassword());

        if (!matches) {
            throw new InvalidCredentialsException("Usuário ou senha incorreta");
        }
    }

    /**
     * Altera a senha de um usuário específico, validando o login do DTO.
     *
     * @param id  UUID do usuário
     * @param dto DTO contendo login, senha atual e nova senha
     * @throws InvalidCredentialsException se o usuário não existir, login não corresponder ou senha atual estiver incorreta
     */
    public void changePassword(UUID id, ChangePasswordDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new InvalidCredentialsException("Usuário ou senha inválidos"));

        if (!user.getLogin().equals(dto.login())) {
            throw new InvalidCredentialsException("Login informado não corresponde ao usuário");
        }

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Usuário ou senha inválidos");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        repository.save(user);
    }
}