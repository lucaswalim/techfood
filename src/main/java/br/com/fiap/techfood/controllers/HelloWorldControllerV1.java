package br.com.fiap.techfood.controllers;

import br.com.fiap.techfood.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hello")
@Slf4j
public class HelloWorldControllerV1 {

    @GetMapping
    public ResponseEntity<ApiResponse<Void>> helloWorld() {
        log.info("[GET] - helloWorld requisitado");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("[GET] - helloWorld requisitado"));
    }

}
