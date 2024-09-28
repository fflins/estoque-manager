package com.seuapp.estoque;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/produtos/*")
public class ProdutoServlet extends HttpServlet {

    // Método doGet para lidar com as requisições GET
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Lista para armazenar os produtos
        List<Produto> produtos = new ArrayList<>();

        // Conectar ao banco de dados e buscar os produtos
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/estoque_db", "root", "root")) {
            String query = "SELECT codigo, nome, descricao FROM Produto";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Percorrer o resultado e adicionar os produtos à lista
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

        // Converte a lista de produtos para JSON usando Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(produtos);

        // Envia a resposta JSON
        response.getWriter().println(jsonResponse);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try {
            // Obtenha os parâmetros do produto (assumindo que o frontend envia JSON)
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            String requestData = sb.toString();

            // Parse JSON usando uma biblioteca como Gson
            Gson gson = new Gson();
            Produto produto = gson.fromJson(requestData, Produto.class);

            // Insira o produto no banco de dados
            Connection connection = DatabaseUtils.getConnection(); // Método para obter conexão
            String sql = "INSERT INTO Produto (nome, descricao, codigo) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, produto.getNome());
            preparedStatement.setString(2, produto.getDescricao());
            preparedStatement.setString(3, produto.getCodigo());
            preparedStatement.executeUpdate();

            // Retorne uma resposta de sucesso
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("{\"message\":\"Produto inserido com sucesso\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("{\"message\":\"Erro ao inserir produto\"}");
            e.printStackTrace();
        }
    }

    @Override
protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String codigo = request.getPathInfo() != null ? request.getPathInfo().substring(1) : null; // Extrai o código da URL

    if (codigo == null || codigo.isEmpty()) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Código do produto é obrigatório");
        return;
    }

    try {
        Connection connection = DatabaseUtils.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Produto WHERE codigo = ?");
        stmt.setString(1, codigo);
        int affectedRows = stmt.executeUpdate();

        if (affectedRows > 0) {
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