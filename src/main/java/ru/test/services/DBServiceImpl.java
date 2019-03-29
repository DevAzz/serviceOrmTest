package ru.test.services;

import org.hibernate.HibernateException;
import ru.test.entities.UserProfileEntity;
import ru.test.entities.api.IUser;
import ru.test.exceptions.DBException;
import ru.test.repositories.UsersRepository;
import ru.test.services.api.IDBService;
import ru.test.utils.ContextService;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса взаимодействия с БД на основе ORM
 */
public class DBServiceImpl implements IDBService {

    @Override
    public IUser getUser(long id) throws DBException {
        IUser user = null;
        try {
            user = getRepository().getById(id);
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return user;
    }

    @Override
    public Long getUserId(String login) throws DBException {
        Long id = null;
        try {
            IUser user = getRepository().getUserByLogin(login);
            id = null != user ? user.getId() : null;
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
            id = getRepository().add(new UserProfileEntity(login, password));
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return id;
    }

    @Override
    public List<IUser> getAllUsers() throws DBException {
        List<IUser> result = null;
        try {
            result = getRepository().getAllEntities().stream().map(IUser.class::cast).collect(
                    Collectors.toList());
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return result;
    }

    private UsersRepository getRepository() {
        return ContextService.getInstance().getService(UsersRepository.class);
    }

}
