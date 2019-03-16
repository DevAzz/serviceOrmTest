package ru.test.exceptions;

/**
 * Базовое исключение работы с БД
 *
 */
public class DBException extends BaseExeption {
    public DBException(Throwable cause) {
        super(cause);
    }
}
