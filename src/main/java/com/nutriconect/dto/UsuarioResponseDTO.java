package com.nutriconect.dto;

/** Resposta pública de usuário: nunca inclui a senha. */
public record UsuarioResponseDTO(Long id, String nome, String email) {
}
