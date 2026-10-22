package com.nutriconect.exception;

public class GeocodificacaoIndisponivelException extends RuntimeException {

    public GeocodificacaoIndisponivelException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}