package ru.test.repositories;

import org.hibernate.Session;

public interface Callback {
    void callingBack(Session aSession);
}
