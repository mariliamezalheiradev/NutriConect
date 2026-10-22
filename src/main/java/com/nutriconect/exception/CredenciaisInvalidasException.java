package com.nutriconect.exception;

public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
