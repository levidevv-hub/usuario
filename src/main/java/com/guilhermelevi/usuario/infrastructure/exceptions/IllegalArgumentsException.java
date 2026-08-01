package com.guilhermelevi.usuario.infrastructure.exceptions;

public class IllegalArgumentsException extends RuntimeException {
    public IllegalArgumentsException(String message) {
        super(message);
    }
    public IllegalArgumentsException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
