package com.nutriconect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReceitaDTO {

    @NotBlank(message = "O nome da receita é obrigatório")
    @Size(max = 100)
    private String nome;

    private String descricao;

    private String modoPreparo;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getModoPreparo() { return modoPreparo; }
    public void setModoPreparo(String modoPreparo) { this.modoPreparo = modoPreparo; }
}
