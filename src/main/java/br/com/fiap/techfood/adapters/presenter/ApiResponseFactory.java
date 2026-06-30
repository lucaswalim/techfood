package br.com.fiap.techfood.adapters.presenter;

import br.com.fiap.techfood.adapters.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.adapters.response.api.Meta;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiResponseFactory {

    public <T> ResponseEntity<ApiSuccessResponse<T>> ok(T data) {
        return ApiSuccessResponse.ok(data);
    }

    public <T> ResponseEntity<ApiSuccessResponse<T>> ok(T data, String message) {
        return ApiSuccessResponse.ok(data, message);
    }

    public <T> ResponseEntity<ApiSuccessResponse<T>> created(T data, String message) {
        return ApiSuccessResponse.created(data, message);
    }

    public <T> ResponseEntity<ApiSuccessResponse<List<T>>> okPage(Page<T> page) {
        return ApiSuccessResponse.ok(page.getContent(), Meta.from(page));
    }

    public ResponseEntity<ApiSuccessResponse<String>> okMessage(String message) {
        return ApiSuccessResponse.okMessage(message);
    }

    public ResponseEntity<Void> noContent() {
        return ApiSuccessResponse.noContent();
    }
}
