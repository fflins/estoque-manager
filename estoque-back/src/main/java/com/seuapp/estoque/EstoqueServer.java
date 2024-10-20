package com.seuapp.estoque;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import java.util.EnumSet;

import jakarta.servlet.DispatcherType;

public class EstoqueServer {
    public static void main(String[] args) throws Exception {
        // Configura o servidor na porta 8080
        Server server = new Server(8080);

        // Cria o contexto para o servlet
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Add the filter, and then use the provided FilterHolder to configure it
        context.addFilter(CORSFilter.class, "/*", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));

        // Adiciona o filtro CORS para todas as requisições
        context.addFilter(CORSFilter.class, "/*", 
            EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR, DispatcherType.ASYNC));

        // Adiciona o servlet que gerenciará as requisições para produtos
        context.addServlet(new ServletHolder(new ProdutoServlet()), "/produtos/*");

        // Adiciona o servlet que gerenciará as requisições para movimentações
        context.addServlet(new ServletHolder(new MovimentacaoServlet()), "/movimentacoes/*");

        // Inicia o servidor
        server.start();
        server.join();
    }
}
