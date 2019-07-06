package ru.test.repositories;

import ru.test.entities.ChatMessageEntity;
import ru.test.exceptions.DBException;

import java.util.List;

public class ChatMessageRepository extends AbstractRepository<ChatMessageEntity> {

    @Override
    protected Class<ChatMessageEntity> getEntityType() {
        return ChatMessageEntity.class;
    }

    /**
     * Возвращает список сообщений по идентификатору пользователя
     * @param userId идентификатор пользователя
     * @return список пользовательских сообщений
     * @throws DBException в случае ошибки работы с БД
     */
    public List<ChatMessageEntity> getMessagesByUserId(Long userId) throws DBException {
        List<ChatMessageEntity> result = null;

        return result;
    }
}
