package ru.test.entities;

import ru.test.entities.api.IUser;

import javax.persistence.*;
import java.util.Objects;

/**
 * ORM Сущность пользовательского профиля
 */
@Entity
@Table(name = "users")
public class UserProfileEntity implements IUser {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, updatable = false)
    private  String login;

    @Column(name = "password", unique = true, updatable = false)
    private String password;

    public UserProfileEntity() {
    }

    public UserProfileEntity(String login, String pass) {
        this.login = login;
        this.password = pass;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserProfileEntity{" +
               "id=" + id +
               ", name='" + login + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserProfileEntity that = (UserProfileEntity) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
}
