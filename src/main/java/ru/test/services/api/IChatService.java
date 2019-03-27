package ru.test.services.api;

import ru.test.entities.ChatUser;

import java.util.List;

/**
 * Сервис чата
 */
public interface IChatService<T extends Object> {

    /**
     * Отправка сообщения клиентам
     * @param data контент сообщения
     */
    void sendMessage(String data);

    /**
     * Рассылка широковещательного сообщения об изменении статуса пользователя
     * @param user пользователь
     */
    void sendChangeUserStatusMessage(ChatUser user);

    /**
     * Рассылка широковещательного сообщения об изменении статуса нескольких пользователей
     * @param users список пользователей
     */
    void sendChangeUsersStatusMessage(List<ChatUser> users);

    /**
     * Добавляет клиента
     * @param client клиент
     */
    void add(T client);

    /**
     * Удаляет клиента
     * @param client клиент
     */
    void remove(T client);

}
