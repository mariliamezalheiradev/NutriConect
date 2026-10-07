package com.nutriconect.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_receptor")
public class Receptor extends Usuario {

    @Column(length = 18)
    private String cnpj;

    private String endereco;

    public Receptor() {}

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}