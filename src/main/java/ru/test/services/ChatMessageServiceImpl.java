package ru.test.services;

import ru.test.entities.ChatMessageEntity;
import ru.test.exceptions.DBException;
import ru.test.repositories.ChatMessageRepository;
import ru.test.services.api.IChatMessageService;
import ru.test.utils.ContextService;

import java.util.List;

public class ChatMessageServiceImpl implements IChatMessageService {

    private final ChatMessageRepository repository;

    public ChatMessageServiceImpl() {
        repository = ContextService.getInstance().getService(ChatMessageRepository.class);
    }

    @Override
    public Long addChatMessage(ChatMessageEntity messageEntity) throws DBException {
        return repository.add(messageEntity);
    }

    @Override
    public void removeChatMessage(ChatMessageEntity messageEntity) throws DBException {
        repository.removeEntity(messageEntity);
    }

    @Override
    public List<ChatMessageEntity> getAll() throws DBException {
        return repository.getAllEntities();
    }

    @Override
    public ChatMessageEntity getById(Long id) throws DBException{
        return repository.getById(id);
    }

    @Override
    public List<ChatMessageEntity> getMessagesByUserId(Long userId) throws DBException {
        return repository.getMessagesByUserId(userId);
    }

}
