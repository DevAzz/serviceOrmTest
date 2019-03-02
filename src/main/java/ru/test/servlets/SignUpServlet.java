package ru.test.servlets;

import ru.test.exceptions.DBException;
import ru.test.services.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {

    private final DBService dbService;

    public SignUpServlet(DBService usersDao) {this.dbService = usersDao;}

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseText = "";
        try {
            String login = req.getParameter("login");
            String pass = req.getParameter("password");

            if (login == null || pass == null) {
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (null == dbService.getUserByLogin(login)) {
                dbService.addUser(login, pass);
                responseText = "Status code (200)\n" +
                               "и текст страницы:\n" +
                               "User added: " + login;
            } else {
                responseText = "Status code (200)\n" +
                               "и текст страницы:\n" +
                               "User already exist: " + login;
            }
            resp.getWriter().println(responseText);
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            //TODO Логирование
        }
    }
}
