package com.nutriconect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class GerarReceitaRequestDTO {

    @NotEmpty(message = "A lista de ingredientes não pode estar vazia")
    private List<@NotBlank(message = "Ingrediente em branco") String> ingredientes;

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }
}
