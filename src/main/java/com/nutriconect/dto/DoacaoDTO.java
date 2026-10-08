package com.nutriconect.dto;

import jakarta.validation.constraints.NotNull;

public class DoacaoDTO {

    @NotNull(message = "A quantidade é obrigatória")
    private Double quantidade;

    @NotNull(message = "O doador é obrigatório")
    private Long doadorId;

    private Long receptorId;

    @NotNull(message = "O ingrediente é obrigatório")
    private Long ingredienteId;

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Long getDoadorId() {
        return doadorId;
    }

    public void setDoadorId(Long doadorId) {
        this.doadorId = doadorId;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(Long receptorId) {
        this.receptorId = receptorId;
    }

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
    }
}
