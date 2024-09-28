package com.seuapp.estoque;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebServlet("/movimentacoes")
public class MovimentacaoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Movimentacao> movimentacoes = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/estoque_db", "root", "root")) {
            String query = "SELECT produtoNome, tipo, quantidade, dataHora FROM Movimentacao";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String produtoNome = rs.getString("produtoNome");
                String tipo = rs.getString("tipo");
                int quantidade = rs.getInt("quantidade");
                Timestamp dataHora = rs.getTimestamp("dataHora");

                movimentacoes.add(new Movimentacao(produtoNome, tipo, quantidade, dataHora.toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonResponse = objectMapper.writeValueAsString(movimentacoes);
        response.getWriter().println(jsonResponse);
    }
}
