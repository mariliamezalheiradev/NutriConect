package com.nutriconect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/** Chave composta (PK/FK id_receita + id_ingrediente) da tabela `receita_ingrediente`. */
@Embeddable
public class ReceitaIngredienteId implements Serializable {

    @Column(name = "receita_id")
    private Long receitaId;

    @Column(name = "ingrediente_id")
    private Long ingredienteId;

    public ReceitaIngredienteId() {}

    public ReceitaIngredienteId(Long receitaId, Long ingredienteId) {
        this.receitaId = receitaId;
        this.ingredienteId = ingredienteId;
    }

    public Long getReceitaId() { return receitaId; }
    public void setReceitaId(Long receitaId) { this.receitaId = receitaId; }
    public Long getIngredienteId() { return ingredienteId; }
    public void setIngredienteId(Long ingredienteId) { this.ingredienteId = ingredienteId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceitaIngredienteId that)) return false;
        return Objects.equals(receitaId, that.receitaId) && Objects.equals(ingredienteId, that.ingredienteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receitaId, ingredienteId);
    }
}
