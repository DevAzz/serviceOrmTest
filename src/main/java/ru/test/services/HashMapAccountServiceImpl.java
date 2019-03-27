package ru.test.services;

import ru.test.entities.UserProfileBase;
import ru.test.entities.api.IUser;
import ru.test.exceptions.AccountException;
import ru.test.services.api.IAccountService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация сервиса взаимодействия с аккаунтами пользователей на основе карт
 */
public class HashMapAccountServiceImpl implements IAccountService {
    private final Map<String, IUser> loginToProfile;
    private final Map<String, IUser> sessionIdToProfile;

    public HashMapAccountServiceImpl() {
        loginToProfile = new HashMap<>();
        sessionIdToProfile = new HashMap<>();
    }

    @Override
    public void addNewUser(String login, String passowrd) throws AccountException {
        loginToProfile.put(login, new UserProfileBase(login, passowrd));
    }

    @Override
    public IUser getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    @Override
    public List<IUser> getAllUsers() throws AccountException {
        return new ArrayList<>(loginToProfile.values());
    }

    @Override
    public IUser getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    @Override
    public void addSession(String sessionId, IUser userProfileEntity) throws AccountException {
        sessionIdToProfile.put(sessionId, userProfileEntity);
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }

    @Override
    public Boolean isUserOnline(IUser user) {
        return sessionIdToProfile.containsValue(user);
    }
}
