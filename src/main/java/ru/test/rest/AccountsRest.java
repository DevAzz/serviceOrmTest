package ru.test.rest;

import com.google.gson.GsonBuilder;
import ru.test.entities.ChatUser;
import ru.test.entities.api.IUser;
import ru.test.exceptions.AccountException;
import ru.test.services.api.IAccountService;
import ru.test.utils.ContextService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/account")
public class AccountsRest {

    private final IAccountService accountService;

    private final GsonBuilder gsonBuilder;

    public AccountsRest() {
        this.accountService = ContextService.getInstance().getService(IAccountService.class);
        gsonBuilder = new GsonBuilder();
    }

    @GET
    @Path("allUsers")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getAllUser() {
        String result = "";
        try {
            List<IUser> users = accountService.getAllUsers();
            String jsonUsers = users.stream()
                    .map(user -> gsonBuilder.create()
                            .toJson(new ChatUser(user, accountService.isUserOnline(user)))).collect(
                            Collectors.joining(", "));
            result = "[" + jsonUsers + "]";
        } catch (AccountException e) {
            e.printStackTrace();
            //TODO Логирование
        }
        return result;
    }

    @POST
    @Path("exitUser")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public void handleUserExit(String name) {
        try {
            accountService.deleteSession(name);
        } catch (AccountException e) {
            e.printStackTrace();
            //TODO Логирование
        }
    }

}
