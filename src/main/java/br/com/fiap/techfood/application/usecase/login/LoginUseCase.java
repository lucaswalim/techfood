package br.com.fiap.techfood.application.usecase.login;

import br.com.fiap.techfood.application.gateway.PasswordEncoderGateway;
import br.com.fiap.techfood.application.gateway.TokenGateway;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.dto.request.LoginRequestDTO;
import br.com.fiap.techfood.dto.response.TokenResponseDTO;
import br.com.fiap.techfood.exceptions.InvalidCredentialsException;
import br.com.fiap.techfood.model.User;
import org.springframework.stereotype.Component;

@Component
public class LoginUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;
    private final TokenGateway tokenGateway;

    public LoginUseCase(UserGateway userGateway,
                        PasswordEncoderGateway passwordEncoderGateway,
                        TokenGateway tokenGateway) {
        this.userGateway = userGateway;
        this.passwordEncoderGateway = passwordEncoderGateway;
        this.tokenGateway = tokenGateway;
    }

    public TokenResponseDTO execute(LoginRequestDTO dto) {
        String login = dto.login().trim();

        User user = userGateway.findByLogin(login)
                .orElseThrow(() -> new InvalidCredentialsException("Usuário ou senha incorreta"));

        if (!passwordEncoderGateway.matches(dto.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Usuário ou senha incorreta");
        }

        String token = tokenGateway.generateToken(user);
        return new TokenResponseDTO(token);
    }
}
