package ru.test.services;

import ru.test.entities.api.IUser;
import ru.test.exceptions.AccountException;
import ru.test.exceptions.DBException;
import ru.test.services.api.IAccountService;
import ru.test.services.api.IDBService;
import ru.test.utils.ContextService;

import java.util.HashMap;
import java.util.Map;

public class DBAccountServiceImpl implements IAccountService {

    private final Map<String, IUser> sessionIdToProfile;

    private final IDBService dbService;

    public DBAccountServiceImpl() {
        sessionIdToProfile = new HashMap<>();
        this.dbService = ContextService.getInstance().getService(IDBService.class);
    }

    @Override
    public void addNewUser(String login, String passowrd) throws AccountException {
        try {
            dbService.addUser(login, passowrd);
        } catch (DBException e) {
            throw new AccountException(e);
        }
    }

    @Override
    public IUser getUserByLogin(String login) throws AccountException {
        IUser user = null;
        try {
            user = dbService.getUserByLogin(login);
        } catch (DBException e) {
            throw new AccountException(e);
        }
        return user;
    }

    @Override
    public IUser getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    @Override
    public void addSession(String sessionId, IUser userProfileEntity) {
        sessionIdToProfile.put(sessionId, userProfileEntity);
    }

    @Override
    public void deleteSession(String sessionId) throws AccountException {
        sessionIdToProfile.remove(sessionId);
    }
}
