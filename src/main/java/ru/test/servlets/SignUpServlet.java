package ru.test.servlets;

import ru.test.exceptions.AccountException;
import ru.test.services.api.IAccountService;
import ru.test.utils.ContextService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {

    private final IAccountService accountService;

    public SignUpServlet() {
        this.accountService =
            ContextService.getInstance().getService(IAccountService.class);
    }

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

            if (null == accountService.getUserByLogin(login)) {
                accountService.addNewUser(login, pass);
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
        } catch (AccountException e) {
            e.printStackTrace();
            //TODO Логирование
        }
    }
}
