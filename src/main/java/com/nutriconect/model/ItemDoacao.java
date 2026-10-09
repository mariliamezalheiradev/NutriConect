package com.nutriconect.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/** Tabela `item_doação` do diagrama: um ingrediente e sua quantidade dentro de uma doação. */
@Entity
@Table(name = "tb_item_doacao")
public class ItemDoacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doacao_id", nullable = false)
    private Doacao doacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    public ItemDoacao() {}

    public ItemDoacao(Ingrediente ingrediente, BigDecimal quantidade) {
        this.ingrediente = ingrediente;
        this.quantidade = quantidade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Doacao getDoacao() { return doacao; }
    public void setDoacao(Doacao doacao) { this.doacao = doacao; }
    public Ingrediente getIngrediente() { return ingrediente; }
    public void setIngrediente(Ingrediente ingrediente) { this.ingrediente = ingrediente; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
}
