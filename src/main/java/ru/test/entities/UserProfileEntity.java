package ru.test.entities;

import ru.test.entities.api.IUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_chat",
               //foreign key for EmployeeEntity in employee_car table
               joinColumns = @JoinColumn(name = "user_id"),
               //foreign key for other side - EmployeeEntity in employee_car table
               inverseJoinColumns = @JoinColumn(name = "chat_id"))
    private List<ChatEntity> chats = new ArrayList<>();

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

    public List<ChatEntity> getChats() {
        return chats;
    }

    public void setChats(List<ChatEntity> chats) {
        this.chats = chats;
    }

    public void addChat(ChatEntity entity) {
        this.chats.add(entity);
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
