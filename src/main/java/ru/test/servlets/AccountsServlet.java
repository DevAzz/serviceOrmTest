package ru.test.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.test.entities.api.IUser;
import ru.test.exceptions.AccountException;
import ru.test.services.api.IAccountService;
import ru.test.utils.ContextService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccountsServlet extends HttpServlet {

    private final IAccountService accountService;

    private final Gson gsonBuilder;

    public AccountsServlet() {
        this.accountService = ContextService.getInstance().getService(IAccountService.class);
        gsonBuilder = new GsonBuilder().create();
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        try {
            IUser user = accountService.getUserBySessionId(request.getRequestedSessionId());
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("[" + gsonBuilder.toJson(user) + "]");
        } catch (AccountException e) {
            e.printStackTrace();
            //TODO Логирование
        }

    }
}
