package ru.test.services.api;

import ru.test.entities.ChatMessageEntity;
import ru.test.exceptions.DBException;

import java.util.List;

public interface IChatMessageService {

    Long addChatMessage(ChatMessageEntity messageEntity) throws DBException;

    void removeChatMessage(ChatMessageEntity messageEntity) throws DBException;

    List<ChatMessageEntity> getAll() throws DBException;

    /**
     * Возвращает сообщение по идентифтикатору
     * @param id идентификатор сообщения
     * @return список сообщений
     * @exception DBException в случае ошибки работы с БД
     */
    ChatMessageEntity getById(Long id) throws DBException;

    /**
     * Возвращает список сообщений по идентификатору пользователя
     * @param userId идентификатор пользователя
     * @return список пользовательских сообщений
     * @throws DBException в случае ошибки работы с БД
     */
    List<ChatMessageEntity> getMessagesByUserId(Long userId) throws DBException;
}
