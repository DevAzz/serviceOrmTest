package ru.test.entities;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserProfile {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, updatable = false)
    private  String login;

    @Column(name = "password", unique = true, updatable = false)
    private String password;

    public UserProfile() {
    }

    public UserProfile(String login, String pass) {
        this.login = login;
        this.password = pass;
    }

    public UserProfile(String login) {
        this.login = login;
        this.password = login;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
               "id=" + id +
               ", name='" + login + '\'' +
               '}';
    }
}
