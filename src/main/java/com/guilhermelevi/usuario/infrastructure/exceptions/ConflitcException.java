package com.guilhermelevi.usuario.infrastructure.exceptions;

public class ConflitcException extends RuntimeException {

    public ConflitcException(String mensagem) {
        super(mensagem);
    }

    public ConflitcException(String mensagem, Throwable throwable) {
        super(mensagem);
    }
}
