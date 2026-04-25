package br.com.fiap.techfood.dto.response.api;

import org.springframework.data.domain.Page;

public record Meta(
        long totalElements,
        int totalPages,
        int page,
        int size,
        boolean hasNext,
        boolean hasPrevious
) {

    public static Meta from(Page<?> page) {
        return new Meta(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}