package ru.test.servlets;

import ru.test.entities.UserProfile;
import ru.test.exceptions.DBException;
import ru.test.services.DBService;
import ru.test.services.UsersDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignInServlet extends HttpServlet {

    private final DBService dbService;

    public SignInServlet(DBService dbService) {this.dbService = dbService;}


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String login = req.getParameter("login");
            String pass = req.getParameter("password");

            if (login == null || pass == null) {
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            UserProfile profile = dbService.getUserByLogin(login);
            String responseText = "";
            if (null == profile) {
                responseText = "Status code (401)\n" +
                               "текст страницы:\n" +
                               "Unauthorized";
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().println(responseText);
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                responseText = "Status code (200)\n" +
                               "и текст страницы:\n" +
                               "Authorized: " + login;
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().println(responseText);
                resp.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (DBException e) {
            e.printStackTrace();
            //TODO Логирование
        }
    }
}
