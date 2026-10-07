package com.nutriconect.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_doacao")
public class Doacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double quantidade;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDoacao status = StatusDoacao.PENDENTE;

    @ManyToOne
    @JoinColumn(name = "doador_id", nullable = false)
    private Doador doador;

    @ManyToOne
    @JoinColumn(name = "receptor_id")
    private Receptor receptor;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;

    public Doacao() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getQuantidade() { return quantidade; }
    public void setQuantidade(Double quantidade) { this.quantidade = quantidade; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public StatusDoacao getStatus() { return status; }
    public void setStatus(StatusDoacao status) { this.status = status; }
    public Doador getDoador() { return doador; }
    public void setDoador(Doador doador) { this.doador = doador; }
    public Receptor getReceptor() { return receptor; }
    public void setReceptor(Receptor receptor) { this.receptor = receptor; }
    public Ingrediente getIngrediente() { return ingrediente; }
    public void setIngrediente(Ingrediente ingrediente) { this.ingrediente = ingrediente; }
}