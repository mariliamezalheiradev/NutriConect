package com.nutriconect.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/** Tabela `estoque` do diagrama: uma linha de saldo por ingrediente (relação 1:1). */
@Entity
@Table(name = "tb_estoque")
public class Estoque implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "ingrediente_id", nullable = false, unique = true)
    private Ingrediente ingrediente;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    public Estoque() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Ingrediente getIngrediente() { return ingrediente; }
    public void setIngrediente(Ingrediente ingrediente) { this.ingrediente = ingrediente; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
}
