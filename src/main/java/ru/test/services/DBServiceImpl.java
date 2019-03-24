package ru.test.services;

import org.hibernate.HibernateException;
import ru.test.entities.api.IUser;
import ru.test.exceptions.DBException;
import ru.test.repositories.api.IUsersRepository;
import ru.test.services.api.IDBService;
import ru.test.utils.ContextService;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Реализация сервиса взаимодействия с БД на основе ORM
 */
public class DBServiceImpl implements IDBService {

    @Override
    public IUser getUser(long id) throws DBException {
        IUser user = null;
        try {
            user = getRepository().get(id);
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return user;
    }

    @Override
    public long getUserId(String login) throws DBException {
        long id = 0L;
        try {
            id = getRepository().getUserId(login);
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return id;
    }

    @Override
    public IUser getUserByLogin(String login) throws DBException {
        IUser user = null;
        try {
            user = getRepository().getUserByLogin(login);
        } catch (Exception e) {
            if (!(e instanceof NoResultException)) {
                throw new DBException(e);
            }
        }
        return user;
    }

    @Override
    public Long addUser(String login, String password) throws DBException {
        Long id = null;
        try {
            id = getRepository().addUser(login, password);
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return id;
    }

    @Override
    public List<IUser> getAllUsers() throws DBException {
        List<IUser> result = null;
        try {
            result = getRepository().getAllUsers();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return result;
    }

    private IUsersRepository getRepository() {
        return ContextService.getInstance().getService(IUsersRepository.class);
    }

}
