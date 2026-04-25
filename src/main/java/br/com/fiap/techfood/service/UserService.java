package br.com.fiap.techfood.service;

import br.com.fiap.techfood.dto.request.UserPatchDTO;
import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    UserResponseDTO create(UserRequestDTO dto);

    UserResponseDTO patch(UUID id, UserPatchDTO dto);

    UserResponseDTO findById(UUID id);

    Page<UserResponseDTO> searchByName(String name, Pageable pageable);

    void delete(UUID id);
}
