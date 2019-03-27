package ru.test;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.test.repositories.JDBCUsersRepositoryImpl;
import ru.test.repositories.ORMUsersRepositoryImpl;
import ru.test.services.ChatServiceImpl;
import ru.test.services.DBAccountServiceImpl;
import ru.test.services.DBServiceImpl;
import ru.test.services.HashMapAccountServiceImpl;
import ru.test.servlets.AccountsServlet;
import ru.test.servlets.SignInServlet;
import ru.test.servlets.SignUpServlet;
import ru.test.servlets.WebSocketChatServlet;
import ru.test.utils.ContextService;
import ru.test.utils.DBInteratcionType;
import ru.test.utils.DBTypes;
import ru.test.utils.PropertiesType;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {

    private static Boolean useChat = false;

    private static Properties prop = new Properties();

    public static void main(String... args) throws Exception {
        configServer();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignInServlet(useChat)), "/signin");
        context.addServlet(new ServletHolder(new SignUpServlet()), "/signup");
        context.addServlet(new ServletHolder(new AccountsServlet()), "/account");

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
            }
            DBInteratcionType dbInteraction =
                    DBInteratcionType.valueOf(getPropertyValue(PropertiesType.DB_INTERACTION_TYPE));
            if (useDB) {
                ContextService.getInstance().addService(new DBAccountServiceImpl());
                ContextService.getInstance().addService(new DBServiceImpl());
                DBTypes dbType =
                        DBTypes.valueOf(getPropertyValue(PropertiesType.DB_TYPE));
                switch (dbInteraction) {
                    case ORM:
                        ContextService.getInstance().addService(new ORMUsersRepositoryImpl());
                        break;
                    case JDBC:
                        ContextService.getInstance()
                                .addService(new JDBCUsersRepositoryImpl(dbType));
                        break;
                    default:
                        break;
                }
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
