package com.seuapp.estoque;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class EstoqueManagerApp {

    public static void main(String[] args) {
        // Cria uma fábrica de gerenciador de entidades
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("estoquePU");
        // Cria um gerenciador de entidades
        EntityManager em = emf.createEntityManager();
        
        // Cria uma transação
        EntityTransaction transaction = em.getTransaction();
        
        try {
            // Inicia a transação
            transaction.begin();

            // Cria um novo produto
            Produto produto = new Produto();
            produto.setCodigo("123");
            produto.setNome("Produto Teste");
            produto.setDescricao("Descrição do produto teste.");

            // Persiste o produto no banco de dados
            em.persist(produto);

            // Commit da transação
            transaction.commit();
            
            System.out.println("Produto salvo com sucesso!");

        } catch (Exception e) {
            // Rollback em caso de erro
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Fecha o gerenciador de entidades
            em.close();
            emf.close();
        }
    }
}
