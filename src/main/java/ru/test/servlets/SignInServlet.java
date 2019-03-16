package ru.test.servlets;

import ru.test.entities.api.IUser;
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

public class SignInServlet extends HttpServlet {

    private final IAccountService accountService;

    public SignInServlet() {this.accountService = ContextService.getInstance().getService(IAccountService.class);}


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<String, Object> pageVariables = createPageVariablesMap(req);
            String login = req.getParameter("login");
            String pass = req.getParameter("pass");

            if (login == null || pass == null) {
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            IUser profile = accountService.getUserByLogin(login);
            String responseText = "";
            if (null == profile) {
                responseText = "Status code (401)\n" +
                               "текст страницы:\n" +
                               "Unauthorized";
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().println(responseText);
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                resp.getWriter().println(
                        PageGenerator.instance().getPage("chat.html", pageVariables));
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
