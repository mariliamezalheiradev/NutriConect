package com.nutriconect.dto;

import com.nutriconect.model.StatusDoacao;

import java.time.LocalDateTime;

public record DoacaoResponseDTO(Long id, Double quantidade, StatusDoacao status,
                                LocalDateTime dataCriacao, Long doadorId,
                                Long receptorId, Long ingredienteId) {
}
