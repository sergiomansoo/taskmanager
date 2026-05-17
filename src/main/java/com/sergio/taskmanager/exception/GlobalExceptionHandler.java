package com.sergio.taskmanager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
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
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> tratarAuthentication(AuthenticationException exception){
        return ResponseEntity.status(401).body("Usuario inexistente ou senha invalida");
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
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> tratarForbidden(ForbiddenException exception){
        return ResponseEntity.status(403).body(exception.getMessage());
    }

}
