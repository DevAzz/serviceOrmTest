package ru.test.servlets;

import ru.test.exceptions.AccountException;
import ru.test.services.api.IAccountService;
import ru.test.utils.ContextService;
import ru.test.utils.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            Map<String, Object> pageVariables = createPageVariablesMap(req);
            String login = req.getParameter("login");
            String pass = req.getParameter("pass");

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
                pageVariables.put("username", login);

                resp.getWriter().println(
                        PageGenerator.instance().getPage("signin.html", pageVariables));
            } else {
                responseText = "Status code (200)\n" +
                               "и текст страницы:\n" +
                               "User already exist: " + login;
            }

        } catch (AccountException e) {
            e.printStackTrace();
            //TODO Логирование
        }
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("pathInfo", request.getPathInfo());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());
        return pageVariables;
    }

}
