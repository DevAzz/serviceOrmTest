package ru.test.repositories;

import ru.test.entities.ChatMessageEntity;
import ru.test.entities.ChatMessageEntity_;
import ru.test.entities.UserProfileEntity_;
import ru.test.exceptions.DBException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ChatMessageRepository extends AbstractRepository<ChatMessageEntity> {

    @Override
    protected Class<ChatMessageEntity> getEntityType() {
        return ChatMessageEntity.class;
    }

    /**
     * Возвращает список сообщений по идентификатору пользователя
     *
     * @param userId     идентификатор пользователя
     * @param receiverId идентификатор адресата сообщения
     * @return список пользовательских сообщений
     * @throws DBException в случае ошибки работы с БД
     */
    public List<ChatMessageEntity> getMessagesByUserId(Long userId, Long receiverId)
            throws DBException {
        return executeResultHundler(session -> {
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<ChatMessageEntity> query = builder.createQuery(ChatMessageEntity.class);
            Root<ChatMessageEntity> root = query.from(ChatMessageEntity.class);
            query.where(builder.or(builder.and(
                    builder.equal(root.get(ChatMessageEntity_.author).get(UserProfileEntity_.ID)
                            , receiverId),
                    builder.equal(root.get(ChatMessageEntity_.receiver).get(UserProfileEntity_.ID)
                            , userId)), builder.and(
                    builder.equal(root.get(ChatMessageEntity_.author).get(UserProfileEntity_.ID)
                            , userId),
                    builder.equal(root.get(ChatMessageEntity_.receiver).get(UserProfileEntity_.ID)
                            , receiverId))));
            return em.createQuery(query).getResultList();
        });
    }

    /**
     * Возвращает список общих сообщений
     * @return список общих сообщений
     * @throws DBException в случае ошибки
     */
    public List<ChatMessageEntity> getCommonMessages() throws DBException{
        return executeResultHundler(session -> {
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<ChatMessageEntity> query = builder.createQuery(ChatMessageEntity.class);
            Root<ChatMessageEntity> root = query.from(ChatMessageEntity.class);
            query.where(builder.isNull(root.get(ChatMessageEntity_.receiver).get(UserProfileEntity_.ID)));
            return em.createQuery(query).getResultList();
        });
    }
}
