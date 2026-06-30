package br.com.fiap.techfood.adapters.mapper;

import br.com.fiap.techfood.adapters.request.AddressRequest;
import br.com.fiap.techfood.adapters.request.ChangePasswordRequest;
import br.com.fiap.techfood.adapters.request.LoginRequest;
import br.com.fiap.techfood.adapters.request.UserPatchRequest;
import br.com.fiap.techfood.adapters.request.UserRequest;
import br.com.fiap.techfood.adapters.response.TokenResponse;
import br.com.fiap.techfood.adapters.response.UserResponse;
import br.com.fiap.techfood.application.dto.AddressData;
import br.com.fiap.techfood.application.dto.ChangePasswordCommand;
import br.com.fiap.techfood.application.dto.LoginCommand;
import br.com.fiap.techfood.application.dto.TokenResult;
import br.com.fiap.techfood.application.dto.UserCreateCommand;
import br.com.fiap.techfood.application.dto.UserPatchCommand;
import br.com.fiap.techfood.application.dto.UserResult;

public class AdapterMapper {

    public UserCreateCommand toCommand(UserRequest request) {
        return new UserCreateCommand(
                request.name(),
                request.email(),
                request.login(),
                request.password(),
                toAddressData(request.address()),
                request.type()
        );
    }

    public UserPatchCommand toCommand(UserPatchRequest request) {
        return new UserPatchCommand(
                request.name(),
                request.email(),
                request.login(),
                toAddressData(request.address())
        );
    }

    public LoginCommand toCommand(LoginRequest request) {
        return new LoginCommand(request.login(), request.password());
    }

    public ChangePasswordCommand toCommand(ChangePasswordRequest request) {
        return new ChangePasswordCommand(request.login(), request.currentPassword(), request.newPassword());
    }

    public UserResponse toResponse(UserResult result) {
        return new UserResponse(
                result.id(),
                result.name(),
                result.email(),
                result.login(),
                result.type(),
                toAddressRequest(result.address()),
                result.lastUpdatedAt()
        );
    }

    public TokenResponse toResponse(TokenResult result) {
        return new TokenResponse(result.token(), result.type());
    }

    private AddressData toAddressData(AddressRequest request) {
        if (request == null) {
            return null;
        }
        return new AddressData(request.street(), request.number(), request.city(), request.zipCode());
    }

    private AddressRequest toAddressRequest(AddressData data) {
        if (data == null) {
            return null;
        }
        return new AddressRequest(data.street(), data.number(), data.city(), data.zipCode());
    }
}
