package ru.test.servlets;

import com.google.gson.GsonBuilder;
import ru.test.entities.ChatUser;
import ru.test.entities.api.IUser;
import ru.test.exceptions.AccountException;
import ru.test.services.api.IAccountService;
import ru.test.utils.ContextService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AccountsServlet extends HttpServlet {

    private final IAccountService accountService;

    private final GsonBuilder gsonBuilder;

    public AccountsServlet() {
        this.accountService = ContextService.getInstance().getService(IAccountService.class);
        gsonBuilder = new GsonBuilder();
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        try {
            List<IUser> users = accountService.getAllUsers();
            String jsonUsers = users.stream()
                    .map(user -> gsonBuilder.create()
                            .toJson(new ChatUser(user, accountService.isUserOnline(user)))).collect(
                            Collectors.joining(", "));
            response.getOutputStream().write(("[" + jsonUsers + "]").getBytes("UTF-8"));
        } catch (AccountException e) {
            e.printStackTrace();
            //TODO Логирование
        }
    }

    public void doDelete(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        try {
            accountService.deleteSession(request.getSession().getId());
        } catch (AccountException e) {
            e.printStackTrace();
            //TODO Логирование
        }
    }

}
