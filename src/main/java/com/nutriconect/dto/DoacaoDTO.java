package com.nutriconect.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class DoacaoDTO {

    @NotNull(message = "O doador é obrigatório")
    private Long doadorId;

    private Long receptorId;

    @NotEmpty(message = "Informe ao menos um item")
    @Valid
    private List<ItemDoacaoDTO> itens;

    public Long getDoadorId() { return doadorId; }
    public void setDoadorId(Long doadorId) { this.doadorId = doadorId; }
    public Long getReceptorId() { return receptorId; }
    public void setReceptorId(Long receptorId) { this.receptorId = receptorId; }
    public List<ItemDoacaoDTO> getItens() { return itens; }
    public void setItens(List<ItemDoacaoDTO> itens) { this.itens = itens; }
}
