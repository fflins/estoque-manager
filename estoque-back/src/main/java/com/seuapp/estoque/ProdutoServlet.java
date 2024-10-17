package com.seuapp.estoque;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.sql.Timestamp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

@WebServlet("/produtos")
class ProdutoServlet extends HttpServlet {

    // Método doGet para lidar com as requisições GET
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Teste 1");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/estoque_db", "root", "1212")) {
            System.out.println("Teste 2");
            String query = "SELECT codigo, nome, descricao, quantidade FROM Produto";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String codigo = rs.getString("codigo");
                System.out.println(rs.getString("codigo"));
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                int quantidade = rs.getInt("quantidade");
                produtos.add(new Produto(codigo, nome, descricao, quantidade));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(produtos);
        response.getWriter().println(jsonResponse);
    }

    @POST
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
    
        try {
            // Lê e converte o JSON recebido em objeto Produto
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            String requestData = sb.toString();
    
            Gson gson = new Gson();
            Produto produto = gson.fromJson(requestData, Produto.class);
    
            // Insere o produto no banco de dados
            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/estoque_db", "root", "1212")) {
    
                String sql = "INSERT INTO Produto (codigo, nome, descricao, quantidade) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, produto.getCodigo());
                preparedStatement.setString(2, produto.getNome());
                preparedStatement.setString(3, produto.getDescricao());
                preparedStatement.setInt(4, produto.getQuantidade());  // Insere a quantidade
                preparedStatement.executeUpdate();
            }
    
            // Registra a movimentação de entrada
            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/estoque_db", "root", "1212")) {
    
                String sqlMovimentacao = "INSERT INTO Movimentacao (produtoNome, tipo, quantidade, dataHora) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlMovimentacao);
                preparedStatement.setString(1, produto.getNome());
                preparedStatement.setString(2, "entrada");
                preparedStatement.setInt(3, produto.getQuantidade());  // Registra a quantidade correta
                preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.executeUpdate();
            }
    
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("{\"message\":\"Produto inserido com sucesso\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("{\"message\":\"Erro ao inserir produto\"}");
            e.printStackTrace();
        }
    }
    @DELETE
protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String codigo = request.getPathInfo().substring(1);

    if (codigo == null || codigo.isEmpty()) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Código do produto é obrigatório");
        return;
    }

    String requestData = request.getReader().lines().reduce("", (acc, line) -> acc + line);
    int quantidade;
    try {
        quantidade = Integer.parseInt(requestData.replaceAll("\\D+", "")); // Extrai a quantidade do JSON
    } catch (NumberFormatException e) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantidade inválida");
        return;
    }

    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/estoque_db", "root", "1212")) {
        PreparedStatement stmtSelect = connection.prepareStatement("SELECT quantidade FROM Produto WHERE codigo = ?");
        stmtSelect.setString(1, codigo);
        ResultSet rs = stmtSelect.executeQuery();

        if (rs.next()) {
            int estoqueAtual = rs.getInt("quantidade");
            if (estoqueAtual < quantidade) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantidade insuficiente no estoque");
                return;
            }

            int novoEstoque = estoqueAtual - quantidade;

            if (novoEstoque > 0) {
                // Atualiza a quantidade no banco
                PreparedStatement stmtUpdate = connection.prepareStatement(
                    "UPDATE Produto SET quantidade = ? WHERE codigo = ?"
                );
                stmtUpdate.setInt(1, novoEstoque);
                stmtUpdate.setString(2, codigo);
                stmtUpdate.executeUpdate();
            } else {
                // Exclui o produto se o estoque for 0
                PreparedStatement stmtDelete = connection.prepareStatement(
                    "DELETE FROM Produto WHERE codigo = ?"
                );
                stmtDelete.setString(1, codigo);
                stmtDelete.executeUpdate();
            }

            // Registra a movimentação de saída
            String sqlMovimentacao = "INSERT INTO Movimentacao (produtoNome, tipo, quantidade, dataHora) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtMovimentacao = connection.prepareStatement(sqlMovimentacao);
            stmtMovimentacao.setString(1, codigo); // Assume que o nome é o código (ou adapte conforme necessário)
            stmtMovimentacao.setString(2, "saida");
            stmtMovimentacao.setInt(3, quantidade);
            stmtMovimentacao.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmtMovimentacao.executeUpdate();

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("{\"message\":\"Remoção realizada com sucesso\"}");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Produto não encontrado");
        }
    } catch (SQLException e) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao acessar o banco de dados");
        e.printStackTrace();
    }
}

}
