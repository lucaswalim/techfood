package br.com.fiap.techfood.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hello")
@Slf4j
public class HelloWorldControllerV1 {

    @GetMapping
    public void helloWorld() {
        log.info("[GET] - helloWorld requisitado");
    }

}
