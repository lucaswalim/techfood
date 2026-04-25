package br.com.fiap.techfood.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Endereço do usuário")
public record AddressDTO(

        @NotBlank(message = "Rua é obrigatória")
        @Size(min = 3, max = 255)
        @Schema(example = "Rua das Flores")
        String street,

        @NotBlank(message = "Número é obrigatório")
        @Pattern(regexp = "\\d{1,20}", message = "O número deve conter apenas dígitos (máx. 20)")
        @Schema(example = "123")
        String number,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(min = 3, max = 255)
        @Schema(example = "São Paulo")
        String city,

        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 números")
        @Schema(example = "01310100")
        String zipCode
) {
}
