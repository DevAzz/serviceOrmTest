package ru.test.repositories;

import org.h2.jdbcx.JdbcDataSource;
import ru.test.entities.UserProfileBase;
import ru.test.entities.api.IUser;
import ru.test.exceptions.DBException;
import ru.test.executor.Executor;
import ru.test.repositories.api.IUsersRepository;
import ru.test.utils.DBTypes;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC реализация репозитория пользователей
 */
public class JDBCUsersRepositoryImpl implements IUsersRepository {

    private final Executor executor;

    private Connection connection;

    public JDBCUsersRepositoryImpl(DBTypes aType) throws DBException {
        switch (aType) {
            case H2:
                connection = getH2Connection();
                break;
            case MY_SQL:
                connection = getMysqlConnection();
                break;
            default:
                break;
        }
        executor = new Executor(connection);
    }

    @Override
    public IUser get(long id) throws DBException {
        IUser user = null;
        try {
            createTable();
            user = executor.execQuery("select * from users where id=" + id, result -> {
                result.next();
                return new UserProfileBase(result.getLong(1), result.getString(2),
                                           result.getString(3));
            });
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return user;
    }

    @Override
    public long getUserId(String login) throws DBException {
        long userId = 0L;
        try {
            createTable();
            userId = executor.execQuery("select id from users where login='" + login + "'",
                                        result -> {
                                            result.next();
                                            return result.getLong(1);
                                        });
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return userId;
    }

    @Override
    public IUser getUserByLogin(String login) throws DBException {
        IUser user = null;
        try {
            createTable();
            user = executor.execQuery("select * from users where login='" + login + "'",
                                      result -> {
                                          result.next();
                                          IUser userValue =
                                                  new UserProfileBase(result.getLong(1),
                                                                      result.getString(2),
                                                                      result.getString(3));
                                          return userValue;
                                      });
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return user;
    }

    @Override
    public long addUser(String name, String password) throws DBException {
        long result = 0L;
        try {
            connection.setAutoCommit(false);
            createTable();
            executor.execUpdate(
                    "insert into users (login, password) values ('" + name + ", " + password +
                    "')");
            connection.commit();
            result = getUserId(name);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
        return result;

    }

    @Override
    public List<IUser> getAllUsers() throws DBException {
        return null;
    }

    /**
     * Создает таблицу пользователей, если ее еще нет
     *
     * @throws DBException в случае ошибки
     */
    private void createTable() throws DBException {
        try {
            executor.execUpdate(
                    "create table if not exists users (id bigint auto_increment, login varchar(256), " +
                    "password varchar(256), primary key (id))");
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Удаляет таблицу поользователей
     *
     * @throws DBException в случае ошибки
     */
    public void dropTable() throws DBException {
        try {
            executor.execUpdate("drop table users");
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Возвращает соединение с БД типа MySQL
     * @return соединение с БД типа MySQL
     * @throws DBException в случае ошибки
     */
    public Connection getMysqlConnection() throws DBException {
        Connection connection = null;
        try {
            DriverManager
                    .registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=tully&").          //login
                    append("password=tully");       //password

            System.out.println("URL: " + url + "\n");

            connection = DriverManager.getConnection(url.toString());
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new DBException(e);
        }
        return connection;
    }

    /**
     * Возвращает соединение с БД типа H2
     * @return соединение с БД типа H2
     * @throws DBException в случае ошибки
     */
    public Connection getH2Connection() throws DBException {
        Connection connection = null;
        try {
            String url = "jdbc:h2:./h2db";
            String name = "tully";
            String pass = "tully";

            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);
            ds.setUser(name);
            ds.setPassword(pass);

            connection = DriverManager.getConnection(url, name, pass);
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return connection;
    }

}
