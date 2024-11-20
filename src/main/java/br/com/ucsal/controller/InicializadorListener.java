package br.com.ucsal.controller;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebListener
public class InicializadorListener implements ServletContextListener {

    private static final Map<String, Command> commands = new HashMap<>();
    

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Inicializando recursos na inicialização da aplicação");
        commands.put("/", new ProdutoListarServlet()); //ROTA PARA O COMANDO VAZIO

        Reflections reflections = new Reflections("br.com.ucsal.controller");
        Set<Class<?>> rotas = reflections.getTypesAnnotatedWith(Rota.class);

        if (rotas.isEmpty()) {
            System.out.println("Nenhuma classe anotada com @Rota encontrada.");
        } else {
            for (Class<?> rota : rotas) {
                Rota anotacao = rota.getAnnotation(Rota.class);

                // Adicionando ao map com base na anotação @Rota
                try {
                    String rotaValue = anotacao.value();
                    Command commandInstance = (Command) rota.getDeclaredConstructor().newInstance(); // Cria uma instância da classe Command

                    // Adiciona o comando ao mapa
                    commands.put(rotaValue, commandInstance);
                    
                    // Exibe a rota mapeada no console
                    System.out.println("Rota encontrada: " + rotaValue + " -> " + rota.getName() + " (Command: " + commandInstance.getClass().getSimpleName() + ")");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Map<String, Command> getCommands() {
        return commands;
    }
}
