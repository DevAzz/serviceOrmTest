package ru.test.repositories;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.test.entities.UserProfileEntity;
import ru.test.entities.UserProfileEntity_;
import ru.test.entities.api.IUser;
import ru.test.exceptions.DBException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * ORM реализация репозитория пользователей
 */
public class UsersRepository extends AbstractRepository<UserProfileEntity> {

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
    protected Class<UserProfileEntity> getEntityType() {
        return UserProfileEntity.class;
    }
}
