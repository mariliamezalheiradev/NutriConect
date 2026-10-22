package com.nutriconect.exception;

public class IAIndisponivelException extends RuntimeException {

    public IAIndisponivelException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public IAIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
