package com.nutriconect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class IngredienteDTO {

    @NotBlank(message = "O nome do ingrediente é obrigatório")
    @Size(max = 100)
    private String nome;

    @Size(max = 50)
    private String categoria;

    @Size(max = 20)
    private String unidade;

    private LocalDate validade;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public LocalDate getValidade() { return validade; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
}
