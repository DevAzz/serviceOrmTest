package ru.test;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.test.repositories.ChatMessageRepository;
import ru.test.repositories.UsersRepository;
import ru.test.services.*;
import ru.test.servlets.SignInServlet;
import ru.test.servlets.SignUpServlet;
import ru.test.servlets.WebSocketChatServlet;
import ru.test.utils.ContextService;
import ru.test.utils.PropertiesType;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {

    private static Boolean useChat = false;

    private static Properties prop = new Properties();

    public static void main(String... args) throws Exception {
        configServer();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.packages",
                "ru.test.rest");

        context.addServlet(new ServletHolder(new SignInServlet(useChat)), "/signin");
        context.addServlet(new ServletHolder(new SignUpServlet()), "/signup");

        if (useChat) {
            context.addServlet(new ServletHolder(new WebSocketChatServlet()), "/chat");
        }

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});



        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(80);
        connector.setHost("0.0.0.0");
        server.addConnector(connector);
        server.setHandler(handlers);

        server.start();

        System.out.println("Server started");
        server.join();
    }

    /**
     * Настройка сервера
     */
    private static void configServer() {
        try (FileInputStream input = new FileInputStream("src/main/resources/config.properties")) {
            prop.load(input);
            boolean useDB =
                    Boolean.parseBoolean(getPropertyValue(PropertiesType.USE_DB));
            useChat =
                    Boolean.parseBoolean(getPropertyValue(PropertiesType.USE_CHAT));
            if (useChat) {
                ContextService.getInstance().addService(new ChatServiceImpl());
                ContextService.getInstance().addService(new ChatMessageRepository());
                ContextService.getInstance().addService(new ChatMessageServiceImpl());
            }
            if (useDB) {
                ContextService.getInstance().addService(new DBAccountServiceImpl());
                ContextService.getInstance().addService(new DBServiceImpl());
                ContextService.getInstance().addService(new UsersRepository());
            } else {
                ContextService.getInstance().addService(new HashMapAccountServiceImpl());
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Логирование
        }
    }

    private static String getPropertyValue(PropertiesType aType) {
        return prop.getProperty(aType.getPropertyName());
    }

}
