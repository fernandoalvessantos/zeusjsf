package com.fernando.zeus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


public class Usuario {


    private Long id;

    @NotEmpty(message = "O Campo nome é obrigatório")
    private String nome;

    @NotEmpty(message = "O Campo email é obrigatório")
    private String email;

    @NotEmpty(message = "O Campo senha é obrigatório")
    private String senha;

    @NotNull(message = "O Campo perfil é obrigatório")
    @JsonProperty("perfilAcesso")
    private String perfil;

    @JsonIgnore
    private List<Demanda> demandas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public List<Demanda> getDemandas() {
        return demandas;
    }

    public void setDemandas(List<Demanda> demandas) {
        this.demandas = demandas;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
