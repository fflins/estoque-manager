package com.seuapp.estoque;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProdutoDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void adicionarProduto(Produto produto, int quantidade) {
        // Adiciona o produto ao banco de dados
        em.persist(produto);

        // Registra a movimentação de entrada
        Movimentacao entrada = new Movimentacao(produto.getNome(), "entrada", quantidade, LocalDateTime.now());
        em.persist(entrada);
    }

    @Transactional
    public void removerProduto(String codigo, int quantidade) {
        Produto produto = em.find(Produto.class, codigo);

        if (produto != null) {
            // Registra a movimentação de saída
            Movimentacao saida = new Movimentacao(produto.getNome(), "saida", quantidade, LocalDateTime.now());
            em.persist(saida);

            // Remove o produto do banco de dados
            em.remove(produto);
        }
    }

    public List<Produto> consultarProdutos() {
        // Consulta todos os produtos
        return em.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();
    }

    public List<Movimentacao> consultarMovimentacoes() {
        // Consulta todas as movimentações
        return em.createQuery("SELECT m FROM Movimentacao m", Movimentacao.class).getResultList();
    }
}
