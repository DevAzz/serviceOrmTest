package ru.test.services.api;

import ru.test.entities.ChatMessageEntity;
import ru.test.exceptions.DBException;

import java.util.List;

public interface IChatMessageService {

    Long addChatMessage(ChatMessageEntity messageEntity) throws DBException;

    void removeChatMessage(ChatMessageEntity messageEntity) throws DBException;

    List<ChatMessageEntity> getAll() throws DBException;

}
