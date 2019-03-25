package ru.test.repositories;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import ru.test.entities.UserProfileEntity;
import ru.test.entities.UserProfileEntity_;
import ru.test.entities.api.IUser;
import ru.test.exceptions.DBException;
import ru.test.repositories.api.IUsersRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ORM реализация репозитория пользователей
 */
public class ORMUsersRepositoryImpl implements IUsersRepository {

    /**
     * Фабрика сессий
     */
    private final SessionFactory sessionFactory;

    public ORMUsersRepositoryImpl() {
        sessionFactory = createSessionFactory();
    }

    @Override
    public IUser get(long id) throws DBException {
        IUser user = null;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            user = session.get(UserProfileEntity.class, id);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return user;
    }

    @Override
    public long getUserId(String login) throws DBException {
        long result = 0L;
        try {
            Session session = sessionFactory.openSession();
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = em.getCriteriaBuilder();

            CriteriaQuery<UserProfileEntity> criteria =
                    builder.createQuery(UserProfileEntity.class);
            Root<UserProfileEntity> root = criteria.from(UserProfileEntity.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get(UserProfileEntity_.login), login));
            result = em.createQuery(criteria).getSingleResult().getId();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return result;
    }

    @Override
    public IUser getUserByLogin(String login) throws DBException {
        IUser user = null;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<UserProfileEntity> criteria =
                    builder.createQuery(UserProfileEntity.class);
            Root<UserProfileEntity> root = criteria.from(UserProfileEntity.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get(UserProfileEntity_.login), login));
            user = em.createQuery(criteria).getSingleResult();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return user;
    }

    @Override
    public long addUser(String login, String password) throws DBException {
        Long result = null;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            result = (Long) session.save(new UserProfileEntity(login, password));
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return result;
    }

    @Override
    public List<IUser> getAllUsers() throws DBException {
        List<IUser> result = null;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<UserProfileEntity> criteria =
                    builder.createQuery(UserProfileEntity.class);
            Root<UserProfileEntity> root = criteria.from(UserProfileEntity.class);
            criteria.select(root);
            result = em.createQuery(criteria).getResultList().stream().map(IUser.class::cast)
                    .collect(
                            Collectors.toList());
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return result;
    }

    public void printConnectInfo() {
        try {
            Connection connection = sessionFactory.
                    getSessionFactoryOptions().getServiceRegistry().
                    getService(ConnectionProvider.class).getConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out
                    .println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SessionFactory createSessionFactory() {
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(UserProfileEntity.class)
                .buildMetadata();

        return metadata.getSessionFactoryBuilder().build();
    }

}
