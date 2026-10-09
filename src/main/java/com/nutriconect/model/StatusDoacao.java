package com.nutriconect.model;

import jakarta.persistence.*;
import java.io.Serializable;

/** Tabela de domínio `status_doação` do diagrama (id_status, descricao VARCHAR(30)). */
@Entity
@Table(name = "tb_status_doacao")
public class StatusDoacao implements Serializable {

    public static final String PENDENTE = "PENDENTE";
    public static final String EM_ANDAMENTO = "EM_ANDAMENTO";
    public static final String CONCLUIDO = "CONCLUIDO";
    public static final String CANCELADO = "CANCELADO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String descricao;

    public StatusDoacao() {}

    public StatusDoacao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
