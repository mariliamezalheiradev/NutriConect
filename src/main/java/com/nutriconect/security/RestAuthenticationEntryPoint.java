package com.nutriconect.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutriconect.exception.ErroResposta;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** Resposta 401 (sem login, token inválido ou expirado) no formato padrão de erro. */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public RestAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("WWW-Authenticate", "Bearer");
        mapper.writeValue(response.getWriter(), ErroResposta.of(401, "Não autenticado",
                "Faça login e envie o token no cabeçalho Authorization: Bearer <token>.",
                request.getRequestURI()));
    }
}
