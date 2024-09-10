package com.seuapp.estoque;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ProdutoDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("estoquePU");

    public void adicionarProduto(Produto produto) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(produto);
        em.getTransaction().commit();
        em.close();
    }
}

