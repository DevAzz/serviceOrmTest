package ru.test.services.api;

import ru.test.entities.api.IUser;
import ru.test.exceptions.AccountException;

public interface IAccountService {

    void addNewUser(String login, String passowrd) throws AccountException;

    IUser getUserByLogin(String login) throws AccountException;

    IUser getUserBySessionId(String sessionId) throws AccountException;

    void addSession(String sessionId, IUser userProfileEntity) throws AccountException;

    void deleteSession(String sessionId) throws AccountException;
}
