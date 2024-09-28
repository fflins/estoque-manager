package com.seuapp.estoque;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
public class Movimentacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID único para movimentação
    
    @Column(nullable = false)
    private String produtoNome; // Apenas o nome do produto
    
    @Column(nullable = false)
    private String tipo; // "entrada" ou "saida"
    
    @Column(nullable = false)
    private int quantidade;
    
    @Column(nullable = false)
    private LocalDateTime dataHora; // Data da movimentação

    public Movimentacao() {}

    public Movimentacao(String produtoNome, String tipo, int quantidade, LocalDateTime dataHora) {
        this.produtoNome = produtoNome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    


}
