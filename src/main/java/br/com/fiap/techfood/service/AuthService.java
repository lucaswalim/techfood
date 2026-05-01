package br.com.fiap.techfood.service;

import br.com.fiap.techfood.dto.request.ChangePasswordDTO;
import br.com.fiap.techfood.dto.request.LoginRequestDTO;
import br.com.fiap.techfood.dto.response.TokenResponseDTO;

import java.util.UUID;

public interface AuthService {

    TokenResponseDTO login(LoginRequestDTO dto);

    void changePassword(UUID id, ChangePasswordDTO dto);

}
