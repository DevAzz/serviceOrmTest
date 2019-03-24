package ru.test.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.test.entities.api.IUser;

import javax.persistence.*;

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

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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
    public String getJSon() {
        return GSON.toJson(this, UserProfileEntity.class);
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
               "id=" + id +
               ", name='" + login + '\'' +
               '}';
    }
}
