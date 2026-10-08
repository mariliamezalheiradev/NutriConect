package com.nutriconect.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/** Tabela associativa `receita_ingrediente` (N:N entre receita e ingrediente, com quantidade). */
@Entity
@Table(name = "tb_receita_ingrediente")
public class ReceitaIngrediente implements Serializable {

    @EmbeddedId
    private ReceitaIngredienteId id = new ReceitaIngredienteId();

    @ManyToOne(optional = false)
    @MapsId("receitaId")
    @JoinColumn(name = "receita_id")
    private Receita receita;

    @ManyToOne(optional = false)
    @MapsId("ingredienteId")
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    public ReceitaIngrediente() {}

    public ReceitaIngrediente(Ingrediente ingrediente, BigDecimal quantidade) {
        this.ingrediente = ingrediente;
        this.quantidade = quantidade;
    }

    public ReceitaIngredienteId getId() { return id; }
    public void setId(ReceitaIngredienteId id) { this.id = id; }
    public Receita getReceita() { return receita; }
    public void setReceita(Receita receita) { this.receita = receita; }
    public Ingrediente getIngrediente() { return ingrediente; }
    public void setIngrediente(Ingrediente ingrediente) { this.ingrediente = ingrediente; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
}
