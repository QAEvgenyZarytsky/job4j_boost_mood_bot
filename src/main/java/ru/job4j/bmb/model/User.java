package ru.job4j.bmb.model;

import java.util.Objects;
import jakarta.persistence.*;



public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private long clientId;

    @Column(name = "chat_id")
    private long chatId;

    public User(long chatId, long clientId, Long id) {
        this.chatId = chatId;
        this.clientId = clientId;
        this.id = id;
    }
    public User() {}

    public long getChatId() {
        return chatId;
    }

    public long getClientId() {
        return clientId;
    }

    public Long getId() {
        return id;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}