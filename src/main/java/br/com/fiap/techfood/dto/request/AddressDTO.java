package br.com.fiap.techfood.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDTO(

        @NotBlank
        @Schema(example = "Rua das Flores")
        @Size(min = 3, max = 255)
        String street,

        @NotBlank
        @Pattern(regexp = "\\d{1,20}", message = "O número deve conter apenas números e até 20 dígitos")
        @Schema(example = "123")
        String number,

        @NotBlank
        @Schema(example = "São Paulo")
        @Size(min = 3, max = 255)
        String city,

        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 números")
        String zipCode
) {
}