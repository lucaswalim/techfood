package br.com.fiap.techfood.dto.response.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiSuccessResponse<T>(
        T data,
        String message,
        Meta meta,
        OffsetDateTime timestamp
) {

    private static OffsetDateTime now() {
        return OffsetDateTime.now();
    }

    private static <T> ApiSuccessResponse<T> body(T data, String message, Meta meta) {
        return new ApiSuccessResponse<>(data, message, meta, now());
    }

    public static <T> ResponseEntity<ApiSuccessResponse<T>> ok(T data) {
        return ResponseEntity.ok(body(data, null, null));
    }

    public static <T> ResponseEntity<ApiSuccessResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(body(data, message, null));
    }

    public static <T> ResponseEntity<ApiSuccessResponse<T>> ok(T data, Meta meta) {
        return ResponseEntity.ok(body(data, null, meta));
    }

    public static <T> ResponseEntity<ApiSuccessResponse<T>> ok(T data, String message, Meta meta) {
        return ResponseEntity.ok(body(data, message, meta));
    }

    public static ResponseEntity<ApiSuccessResponse<String>> okMessage(String message) {
        return ResponseEntity.ok(body(null, message, null));
    }

    public static <T> ResponseEntity<ApiSuccessResponse<T>> created(T data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(body(data, null, null));
    }

    public static <T> ResponseEntity<ApiSuccessResponse<T>> created(T data, String message) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(body(data, message, null));
    }

    public static <T> ResponseEntity<ApiSuccessResponse<T>> created(T data, String message, Meta meta) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(body(data, message, meta));
    }

    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }
}