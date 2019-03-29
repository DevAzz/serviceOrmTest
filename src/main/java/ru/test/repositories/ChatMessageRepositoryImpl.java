package ru.test.repositories;

import ru.test.entities.ChatMessageEntity;

public class ChatMessageRepositoryImpl extends AbstractRepository<ChatMessageEntity> {

    @Override
    protected Class<ChatMessageEntity> getEntityType() {
        return ChatMessageEntity.class;
    }
}
