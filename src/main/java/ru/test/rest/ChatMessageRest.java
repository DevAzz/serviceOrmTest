package ru.test.rest;

import com.google.gson.GsonBuilder;
import ru.test.entities.ChatMessageEntity;
import ru.test.exceptions.DBException;
import ru.test.services.api.IChatMessageService;
import ru.test.utils.ContextService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Path("/chatMessages")
public class ChatMessageRest {

    private IChatMessageService messageService;

    private final GsonBuilder gsonBuilder;

    public ChatMessageRest() {
        messageService = ContextService.getInstance().getService(IChatMessageService.class);
        gsonBuilder = new GsonBuilder();
    }

    @GET
    @Path("allMessages")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getAllMessages() throws DBException {
        return getMessages(() -> {
            List<ChatMessageEntity> result = new ArrayList<>();
            try {
                result = messageService.getCommonMessages();
            } catch (DBException e) {
                e.printStackTrace();
                // TODO Логирование
            }
            return result;
        });
    }

    private String getMessages(Supplier<List<ChatMessageEntity>> supplier) throws DBException {
        List<ChatMessageEntity> list = supplier.get();
        String json = list.stream()
                .map(message -> gsonBuilder.create()
                        .toJson(message)).collect(
                        Collectors.joining(", "));
        return "[" + json + "]";
    }

    @GET
    @Path("messagesByUserId/{authorId}/{receiverId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getMessageByUserId(@PathParam("authorId") Long authorId,
                                     @PathParam("receiverId") Long receiverId) throws DBException {
        return getMessages(() -> {
            List<ChatMessageEntity> result = new ArrayList<>();
            try {
                result = messageService.getMessagesByUserId(authorId, receiverId);
            } catch (DBException e) {
                e.printStackTrace();
                // TODO Логирование
            }
            return result;
        });
    }

}
