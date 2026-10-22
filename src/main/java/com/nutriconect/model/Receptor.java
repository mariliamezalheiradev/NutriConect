package com.nutriconect.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_receptor")
public class Receptor extends Usuario {

    @Column(length = 18)
    private String cnpj;

    private String endereco;

    @Column(length = 8)
    private String cep;

    private Double latitude;

    private Double longitude;

    public Receptor() {}

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}