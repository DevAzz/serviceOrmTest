package ru.test.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Сущность сообщения чата
 */
@Entity
@Table(name = "chat_message")
public class ChatMessageEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfileEntity user;

    @Column(name = "date")
    private Date date;

    @Lob
    @Column(name = "text", length = 1000000)
    private String text;

    public ChatMessageEntity() {
    }

    public ChatMessageEntity(UserProfileEntity user, Date date, String text) {
        this.user = user;
        this.date = date;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfileEntity getUser() {
        return user;
    }

    public void setUser(UserProfileEntity user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatMessageEntity that = (ChatMessageEntity) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(date, that.date) &&
               Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, text);
    }
}
