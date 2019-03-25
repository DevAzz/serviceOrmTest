package ru.test.utils;

public enum MessageType {

    CHAT_MESSAGE ("CHAT_MESSAGE "),

    USER_LIST ("USER_LIST");

    private String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
