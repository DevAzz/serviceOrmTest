package ru.test.entities;

import ru.test.entities.api.IUser;

public class ChatUser extends UserProfileBase {

    private Boolean online;

    public ChatUser(IUser baseUser, Boolean online) {
        super(baseUser.getId(), baseUser.getLogin(), null);
        this.online = online;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }
}
