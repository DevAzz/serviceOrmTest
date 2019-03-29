package ru.test.entities;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date")
    private Date date;

    @Column(name = "text")
    private String text;

    public ChatMessageEntity() {
    }

    public ChatMessageEntity(Long userId, Date date, String text) {
        this.userId = userId;
        this.date = date;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
