package com.sergio.taskmanager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> tratarRuntime(NotFoundException exception){
        return ResponseEntity.status(404).body(exception.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> tratarRuntime(RuntimeException exception){
        return ResponseEntity.status(400).body(exception.getMessage());
    }
    //tratamento de exceção para json invalido, ex: ENUM vazio
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> tratarJsonInvalido (HttpMessageNotReadableException notReadable){
        return ResponseEntity.badRequest().body("Json invalido ou campo com valor incorreto");
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> tratarMetodoNaoSuportado(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity.status(405).body("Metodo HTTP nao permitido para esse endpoint");
    }

}
