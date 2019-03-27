package ru.test.services;

import com.google.gson.GsonBuilder;
import ru.test.entities.ChatUser;
import ru.test.services.api.IChatService;
import ru.test.sockets.ChatWebSocket;
import ru.test.utils.MessageType;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Реализация сервиса чата на WebSocket
 */
public class ChatServiceImpl implements IChatService<ChatWebSocket> {

    private Set<ChatWebSocket> webSockets;

    private final GsonBuilder gsonBuilder;

    public ChatServiceImpl() {
        this.webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
        gsonBuilder = new GsonBuilder();
    }

    @Override
    public void sendMessage(String data) {
        for (ChatWebSocket user : webSockets) {
            try {
                user.sendString(data);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void sendChangeUserStatusMessage(ChatUser user) {
        String json = gsonBuilder.create()
                .toJson(user);
        sendMessage(MessageType.USER_LIST.getName() + "[" + json + "]");
    }

    @Override
    public void sendChangeUsersStatusMessage(List<ChatUser> users) {
        String jsonUsers = users.stream()
                    .map(user -> gsonBuilder.create()
                            .toJson(user)).collect(
                            Collectors.joining(", "));
            sendMessage(MessageType.USER_LIST.getName() + "[" + jsonUsers + "]");
    }

    @Override
    public void add(ChatWebSocket webSocket) {
        webSockets.add(webSocket);
    }

    @Override
    public void remove(ChatWebSocket webSocket) {
        webSockets.remove(webSocket);
    }

}
