package ru.test.repositories;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/** Обработчик результата запроса */
public interface ResultHandler<T> {
	T handle(Session session) throws HibernateException;
}
