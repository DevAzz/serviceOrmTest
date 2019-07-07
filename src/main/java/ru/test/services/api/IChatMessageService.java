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
     * @param receiverId идентификатор адресата сообщения
     * @return список пользовательских сообщений
     * @throws DBException в случае ошибки работы с БД
     */
    List<ChatMessageEntity> getMessagesByUserId(Long userId, Long receiverId) throws DBException;

    /**
     * Возвращает список общих сообщений
     * @return список общих сообщений
     * @throws DBException в случае ошибки
     */
    List<ChatMessageEntity> getCommonMessages() throws DBException;
}
