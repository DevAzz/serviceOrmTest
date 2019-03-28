package ru.test.sockets;

import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import ru.test.entities.ChatUser;
import ru.test.entities.api.IUser;
import ru.test.exceptions.AccountException;
import ru.test.services.api.IAccountService;
import ru.test.services.api.IChatService;
import ru.test.utils.ContextService;
import ru.test.utils.MessageType;

import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;

@WebSocket
public class ChatWebSocket {
    private IChatService chatService;
    private Session session;
    private HttpSession httpSession;
    private IAccountService accountService;
    private IUser currentUser = null;

    public ChatWebSocket(HttpSession httpSession) {
        this.chatService = ContextService.getInstance().getService(IChatService.class);
        accountService = ContextService.getInstance().getService(IAccountService.class);
        this.httpSession = httpSession;
    }

    @OnWebSocketConnect
    public void onOpen(Session session) throws AccountException {
        chatService.add(this);
        this.session = session;
        currentUser = accountService.getUserBySessionId(httpSession.getId());
        String jsonUsers = accountService.getAllUsers().stream()
                        .map(user -> {
                            String result = "";
                            if (user.getId() != currentUser.getId()) {
                                result = new GsonBuilder().create()
                                        .toJson(new ChatUser(user,
                                                             accountService.isUserOnline(user)));
                            }
                            return result;
                        }).collect(
                        Collectors.joining(", "));
       sendString(MessageType.USER_LIST.getName() + "[" + jsonUsers + "]");
       chatService.sendChangeUserStatusMessage(new ChatUser(currentUser, true));
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        chatService.sendMessage(data);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        chatService.sendChangeUserStatusMessage(new ChatUser(currentUser, false));
        chatService.remove(this);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        //error.printStackTrace();
    }

    public void sendString(String data) {
        try {
            session.getRemote().sendString(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
