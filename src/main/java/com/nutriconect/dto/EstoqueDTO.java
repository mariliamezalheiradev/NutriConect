package com.nutriconect.dto;

import jakarta.validation.constraints.NotNull;

public class EstoqueDTO {

    @NotNull(message = "A quantidade é obrigatória")
    private Double quantidade;

    @NotNull(message = "A data de validade é obrigatória")
    private String dataValidade;

    @NotNull(message = "O doador é obrigatório")
    private Long doadorId;

    @NotNull(message = "O ingrediente é obrigatório")
    private Long ingredienteId;

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Long getDoadorId() {
        return doadorId;
    }

    public void setDoadorId(Long doadorId) {
        this.doadorId = doadorId;
    }

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
    }
}
