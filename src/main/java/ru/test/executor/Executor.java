package ru.test.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Исполнитель SQL запросов
 */
public class Executor {

    /** Соединение с БД */
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    /**
     * Выполняет update-запрос
     * @param update SQL запрос
     * @throws SQLException в случае ошибки
     */
    public void execUpdate(String update) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(update);
        stmt.close();
    }

    /**
     * Выполняет select-запрос к БД
     * @param query запрос
     * @param handler обработчик результата запроса
     * @param <T> тип результата
     * @return результат запроса
     * @throws SQLException в случае ошибки
     */
    public <T> T execQuery(String query,
                           ResultHandler<T> handler)
            throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        result.close();
        stmt.close();

        return value;
    }

}
