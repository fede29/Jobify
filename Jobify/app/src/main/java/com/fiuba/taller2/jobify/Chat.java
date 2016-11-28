package com.fiuba.taller2.jobify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;


public class Chat implements Serializable {

    @SerializedName("id")           int id;
    @SerializedName("contact")      Contact contact;
    @SerializedName("last_message") String lastMessage;
    @SerializedName("is_read")      Boolean isRead;
    @SerializedName("messages")     HashMap<String, Message> messages;
    @Expose(deserialize = false)    User user;


    public Contact getContact() {
        return contact;
    }

    public Message getLastMessage() {
        LinkedList list = new LinkedList<>(messages.keySet());
        Collections.sort(list);
        return messages.get(list.getLast());
    }

    public LinkedList<Message> getMessages() {
        return new LinkedList<>(messages.values());
    }

    public boolean isRead() {
        return isRead;
    }

    public void setUser(User u) {
        user = u;
    }

    public String getUserID() {
        return user.getID();
    }

    public Integer getID() {
        return id;
    }

    public Boolean equals(Chat that) {
        return this.id == that.id;
    }
}
