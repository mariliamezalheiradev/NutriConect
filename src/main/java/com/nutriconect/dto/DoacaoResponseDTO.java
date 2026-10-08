package com.nutriconect.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DoacaoResponseDTO(Long id, String status, LocalDate dataDoacao,
                                Long doadorId, Long receptorId, List<Item> itens) {

    public record Item(Long ingredienteId, BigDecimal quantidade) {
    }
}
