package com.nutriconect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class DoacaoRequestDTO {

    @NotNull(message = "O doador é obrigatório")
    private Long doadorId;

    @NotEmpty(message = "Informe ao menos um ingrediente")
    private List<@NotBlank(message = "Ingrediente não pode ser vazio") String> ingredientes;

    public Long getDoadorId() {
        return doadorId;
    }

    public void setDoadorId(Long doadorId) {
        this.doadorId = doadorId;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }
}