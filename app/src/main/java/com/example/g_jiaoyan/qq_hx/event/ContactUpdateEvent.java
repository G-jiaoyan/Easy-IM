package com.example.g_jiaoyan.qq_hx.event;

public class ContactUpdateEvent {
    public boolean isAdded;
    public String username;

    public ContactUpdateEvent() {
    }

    public ContactUpdateEvent(boolean isAdded, String username) {
        this.isAdded = isAdded;
        this.username = username;
    }
}
