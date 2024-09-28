package com.seuapp.estoque.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seuapp.estoque.Produto;
import com.seuapp.estoque.Movimentacao;
import com.seuapp.estoque.ProdutoDAO;


import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoDAO produtoDAO;

    // Endpoint para adicionar um produto
    @PostMapping
    public ResponseEntity<String> adicionarProduto(@RequestBody Produto produto) {
        produtoDAO.adicionarProduto(produto, 1);
        return ResponseEntity.ok("Produto adicionado com sucesso.");
    }

    // Endpoint para remover um produto
    @DeleteMapping("/{codigo}")
    public ResponseEntity<String> removerProduto(@PathVariable String codigo) {
        produtoDAO.removerProduto(codigo, 1);
        return ResponseEntity.ok("Produto removido com sucesso.");
    }

    // Endpoint para listar todos os produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoDAO.consultarProdutos();
        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produtos);
    }

    // Endpoint para listar todas as movimentações
    @GetMapping("/movimentacoes")
    public ResponseEntity<List<Movimentacao>> listarMovimentacoes() {
        List<Movimentacao> movimentacoes = produtoDAO.consultarMovimentacoes();
        if (movimentacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movimentacoes);
    }
}
