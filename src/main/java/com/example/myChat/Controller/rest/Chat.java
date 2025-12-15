package com.example.myChat.Controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class Chat {

    @GetMapping("/public")
    public ResponseEntity<String> publicTest() {
        return ResponseEntity.ok("A Rota Pública está Funcionando! (Não precisa de JWT)");
    }

    @GetMapping("/secure")
    public ResponseEntity<String> secureTest() {
        return ResponseEntity.status(HttpStatus.OK).body("A Rota Segura Funcionou! (JWT Válido)");
    }
}