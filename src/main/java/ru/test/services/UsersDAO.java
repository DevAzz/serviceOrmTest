package ru.test.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import ru.test.entities.UserProfile;
import ru.test.entities.UserProfile_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class UsersDAO {

    private Session session;

    private EntityManager em;

    public UsersDAO(Session session) {
        this.session = session;
        em = session.getEntityManagerFactory().createEntityManager();
    }

    public UserProfile get(long id) throws HibernateException {
        return (UserProfile) session.get(UserProfile.class, id);
    }

    public long getUserId(String name) throws HibernateException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<UserProfile> criteria = builder.createQuery(UserProfile.class );
        Root<UserProfile> root = criteria.from( UserProfile.class );
        criteria.select( root );
        criteria.where( builder.equal( root.get(UserProfile_.login ), name ) );
        return em.createQuery( criteria ).getSingleResult().getId();
    }

    public UserProfile getUserByLogin(String login) throws HibernateException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<UserProfile> criteria = builder.createQuery(UserProfile.class );
        Root<UserProfile> root = criteria.from( UserProfile.class );
        criteria.select( root );
        criteria.where( builder.equal( root.get(UserProfile_.login ), login ) );
        return em.createQuery( criteria ).getSingleResult();
    }

    public long insertUser(String name, String password) throws HibernateException {
        return (Long) session.save(new UserProfile(name, password));
    }

}
