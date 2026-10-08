package com.nutriconect.security;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/** Lê, do token da requisição, quem é o usuário autenticado. */
public final class UsuarioLogado {

    private UsuarioLogado() {
    }

    public static Long id(JwtAuthenticationToken autenticacao) {
        Object uid = autenticacao.getToken().getClaims().get(JwtService.CLAIM_ID);
        return Long.valueOf(String.valueOf(uid));
    }
}
