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
import ru.test.exceptions.DBException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Абстрактный репозиторий сущностей
 */
public abstract class AbstractRepository<T> {

    /**
     * Фабрика сессий
     */
    protected final SessionFactory sessionFactory;


    public AbstractRepository() {
        sessionFactory = createSessionFactory();
    }

    /**
     * Возвращает запись по ее идентификатору
     * @param id идентифйикатор записи
     * @return запись таблицы
     * @throws DBException в случае ошибки
     */
    public T getById(long id) throws DBException {
        T entity = null;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            entity = session.get(getEntityType(), id);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return entity;
    }

    public long add(T aEntity) throws DBException {
        Long result = null;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            result = (Long) session.save(aEntity);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return result;
    }

    public List<T> getAllEntities() throws DBException {
        List<T> result = null;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<T> criteria =
                    builder.createQuery(getEntityType());
            Root<T> root = criteria.from(getEntityType());
            criteria.select(root);
            result = em.createQuery(criteria).getResultList().stream().map(getEntityType()::cast)
                    .collect(
                            Collectors.toList());
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
        return result;
    }


    public void removeEntity(T aEntity) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.remove(aEntity);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    protected void printConnectInfo() {
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

    protected SessionFactory createSessionFactory() {
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(UserProfileEntity.class)
                .buildMetadata();

        return metadata.getSessionFactoryBuilder().build();
    }

    /**
     * Возвращает тип записи репозитория
     * @return тип записи репозитория
     */
    protected abstract Class<T> getEntityType();

}
