package br.com.ucsal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/view/*")  // Mapeia todas as requisições com "/view/*"
public class ProdutoController extends HttpServlet {

	 private Map<String, Command> commands;
	
    @Override
    public void init() {
    	
    	commands = InicializadorListener.getCommands();
    	
    	// Exibe o conteúdo do mapa "teste"
        System.out.println("Comandos carregados no mapa 'teste':");
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            System.out.println("Rota: " + entry.getKey() + " -> Command: " + entry.getValue().getClass().getSimpleName());
        }       
    }
    	
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println(path);
        Command command = commands.get(path);

        if (command != null) {
            command.execute(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Página não encontrada");
        }
    }
}