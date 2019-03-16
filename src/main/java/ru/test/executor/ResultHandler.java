package ru.test.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Обработчик результата запроса */
public interface ResultHandler<T> {
	T handle(ResultSet resultSet) throws SQLException;
}
