package com.seuapp.estoque;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Produto> consultarProdutos() {
        EntityManager em = emf.createEntityManager();
        List<Produto> produtos = em.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();
        em.close();
        return produtos;
    }


    public List<Movimentacao> consultarMovimentacoes() {
        EntityManager em = emf.createEntityManager();
        List<Movimentacao> movimentacoes = em.createQuery("SELECT m FROM Movimentacao m", Movimentacao.class).getResultList();
        em.close();
        return movimentacoes;
    }

    public void exibirProdutos() {
        List<Produto> produtos = consultarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            for (Produto produto : produtos) {
                System.out.println("Código: " + produto.getCodigo());
                System.out.println("Nome: " + produto.getNome());
                System.out.println("Descrição: " + produto.getDescricao());
                System.out.println("-------------------------------");
            }
        }
    }
    
    public void exibirMovimentacoes() {
        List<Movimentacao> movimentacoes = consultarMovimentacoes();
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação registrada.");
        } else {
            for (Movimentacao movimentacao : movimentacoes) {
                System.out.println("ID: " + movimentacao.getId());
                System.out.println("Produto: " + movimentacao.getProdutoNome());
                System.out.println("Tipo: " + movimentacao.getTipo());
                System.out.println("Quantidade: " + movimentacao.getQuantidade());
                System.out.println("Data/Hora: " + movimentacao.getDataHora());
                System.out.println("-------------------------------");
            }
        }
    }
    

}
