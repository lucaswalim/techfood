package br.com.fiap.techfood.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        String message,
        List<ValidationError> errors,
        LocalDateTime timestamp,
        String path
) {
    public static ApiErrorResponse error(String message, List<ValidationError> errors, String path) {
        return new ApiErrorResponse(
                message,
                errors,
                LocalDateTime.now(),
                path
        );
    }
}
