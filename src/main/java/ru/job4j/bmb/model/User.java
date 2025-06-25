package ru.job4j.bmb.model;

import java.util.Objects;

public class User {

    private Long id;
    private long clientId;
    private long chatId;

    public User(long chatId, long clientId, Long id) {
        this.chatId = chatId;
        this.clientId = clientId;
        this.id = id;
    }

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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return getClientId() == user.getClientId() && getChatId() == user.getChatId() && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClientId(), getChatId());
    }
}