package com.nutriconect.dto;

import java.util.List;

public class DoacaoRequestDTO {
    private Long doadorId;
    private List<String> ingredientes;

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