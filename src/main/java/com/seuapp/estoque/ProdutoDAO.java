package com.seuapp.estoque;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;

public class ProdutoDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("estoquePU");

    public void adicionarProduto(Produto produto, int quantidade) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
    
        // Adiciona o produto ao banco de dados
        em.persist(produto);
    
        // Registra a movimentação de entrada
        Movimentacao entrada = new Movimentacao(produto.getNome(), "entrada", quantidade, LocalDateTime.now());
        em.persist(entrada); 
    
        em.getTransaction().commit();
        em.close();
    }
    
    
    public void removerProduto(String codigo, int quantidade) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
    
        // Busca o produto pelo código
        Produto produto = em.find(Produto.class, codigo);
    
        if (produto != null) {
            // Registra a movimentação de saída usando o nome do produto
            Movimentacao saida = new Movimentacao(produto.getNome(), "saida", quantidade, LocalDateTime.now());
            em.persist(saida);
    
            // Remove o produto do banco de dados
            em.remove(produto);
    
        } else {
            System.out.println("Produto com o código " + codigo + " não encontrado.");
        }
    
        em.getTransaction().commit();
        em.close();
    }
    
}
