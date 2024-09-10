package com.seuapp.estoque;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Produto {
    
    @Id
    private String codigo;
    private String nome;
    private String descricao;
    
    public Produto() {}

    public Produto(String codigo, String nome, String descricao) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
