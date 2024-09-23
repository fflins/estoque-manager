package com.seuapp.estoque;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Scanner;

public class EstoqueManagerApp {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("estoquePU");
        ProdutoDAO produtoDAO = new ProdutoDAO();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Escolha uma ação: (1) Adicionar Produto (2) Remover Produto (3) Sair");
            int escolha = Integer.parseInt(scanner.nextLine());

            if (escolha == 3) {
                break;
            }

            if (escolha == 1) {

                System.out.println("Insira o código do produto:");
                String codigo = scanner.nextLine();
    
                System.out.println("Insira o nome do produto:");
                String nome = scanner.nextLine();
    
                System.out.println("Insira a descrição do produto:");
                String descricao = scanner.nextLine();
    
                Produto produto = new Produto(codigo, nome, descricao);
                // Adiciona produto e registra entrada automaticamente
                produtoDAO.adicionarProduto(produto, 1);

                System.out.println("Produto adicionado.");
            } else if (escolha == 2) {

                System.out.println("Insira o código do produto:");
                String codigo = scanner.nextLine();
    
                // Remove o produto e registra a saída automaticamente
                produtoDAO.removerProduto(codigo, 1);
                System.out.println("Produto removido.");
            } else {
                System.out.println("Opção inválida.");
            }
        }

        emf.close();
        scanner.close();
    }
}
