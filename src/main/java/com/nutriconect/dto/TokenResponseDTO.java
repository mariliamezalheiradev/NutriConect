package com.nutriconect.dto;

public record TokenResponseDTO(String token, String tipo, long expiraEmSegundos, String papel) {
}
