package com.nutriconect.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_doador")
public class Doador extends Usuario {

    @Column(length = 18)
    private String documento;

    public Doador() {}

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
}