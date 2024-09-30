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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/estoque_db", "root", "root")) {
            String query = "SELECT codigo, nome, descricao FROM Produto";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                produtos.add(new Produto(codigo, nome, descricao));
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
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            String requestData = sb.toString();

            // Parse JSON usando Gson
            Gson gson = new Gson();
            Produto produto = gson.fromJson(requestData, Produto.class);

            // Insira o produto no banco de dados
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/estoque_db", "root", "root")) {
                String sql = "INSERT INTO Produto (nome, descricao, codigo) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, produto.getNome());
                preparedStatement.setString(2, produto.getDescricao());
                preparedStatement.setString(3, produto.getCodigo());
                preparedStatement.executeUpdate();
            }

            // Registra a movimentação de entrada
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/estoque_db", "root", "root")) {
                String sqlMovimentacao = "INSERT INTO Movimentacao (produtoNome, tipo, quantidade, dataHora) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlMovimentacao);
                preparedStatement.setString(1, produto.getNome());
                preparedStatement.setString(2, "entrada");
                preparedStatement.setInt(3, 1); // Supondo uma entrada de quantidade 1
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

    String produtoNome = null;

    try {
        // Conectar ao banco de dados
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/estoque_db", "root", "root");
        
        PreparedStatement stmtProduto = connection.prepareStatement("SELECT nome FROM Produto WHERE codigo = ?");
        stmtProduto.setString(1, codigo);
        ResultSet rsProduto = stmtProduto.executeQuery();

        if (rsProduto.next()) {
            produtoNome = rsProduto.getString("nome");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Produto não encontrado");
            return;
        }

        PreparedStatement stmtDelete = connection.prepareStatement("DELETE FROM Produto WHERE codigo = ?");
        stmtDelete.setString(1, codigo);
        int affectedRows = stmtDelete.executeUpdate();

        if (affectedRows > 0) {
            // Registra a movimentação de saída
            String sqlMovimentacao = "INSERT INTO Movimentacao (produtoNome, tipo, quantidade, dataHora) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlMovimentacao);
            preparedStatement.setString(1, produtoNome); // Agora usamos o nome do produto
            preparedStatement.setString(2, "saida");
            preparedStatement.setInt(3, 1); // Supondo uma saída de quantidade 1
            preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.executeUpdate();

            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().println("{\"message\":\"Produto removido com sucesso\"}"); // Mensagem de sucesso
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Produto não encontrado");
        }
    } catch (SQLException e) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao acessar o banco de dados");
        e.printStackTrace();
    }
}

}
