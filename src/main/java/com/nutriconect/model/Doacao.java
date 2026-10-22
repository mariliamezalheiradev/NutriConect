package com.nutriconect.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_doacao")
public class Doacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataDoacao = LocalDate.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusDoacao status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doador_id", nullable = false)
    private Doador doador;

    @ManyToOne
    @JoinColumn(name = "receptor_id")
    private Receptor receptor;

    @OneToMany(mappedBy = "doacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDoacao> itens = new ArrayList<>();

    public Doacao() {}

    public void adicionarItem(ItemDoacao item) {
        item.setDoacao(this);
        itens.add(item);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDataDoacao() { return dataDoacao; }
    public void setDataDoacao(LocalDate dataDoacao) { this.dataDoacao = dataDoacao; }
    public StatusDoacao getStatus() { return status; }
    public void setStatus(StatusDoacao status) { this.status = status; }
    public Doador getDoador() { return doador; }
    public void setDoador(Doador doador) { this.doador = doador; }
    public Receptor getReceptor() { return receptor; }
    public void setReceptor(Receptor receptor) { this.receptor = receptor; }
    public List<ItemDoacao> getItens() { return itens; }
    public void setItens(List<ItemDoacao> itens) { this.itens = itens; }
}
