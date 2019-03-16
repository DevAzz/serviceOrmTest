package ru.test.exceptions;

/**
 * Базовое исключение приложения
 */
public class BaseExeption extends Exception {
    public BaseExeption(Throwable cause) {
        super(cause);
    }
}
