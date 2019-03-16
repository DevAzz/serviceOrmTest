package ru.test.exceptions;

/**
 * Исключение при работе с аккунтами пользователей
 */
public class AccountException extends BaseExeption {
    public AccountException(Throwable cause) {
        super(cause);
    }
}
