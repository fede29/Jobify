package com.fiuba.taller2.jobify;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Chat implements Serializable {

    @SerializedName("id")           int id;
    @SerializedName("contact")      Contact contact;
    @SerializedName("last_message") String lastMessage;
    @SerializedName("is_read")      Boolean isRead;

    public Contact getContact() {
        return contact;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public boolean isRead() {
        return isRead;
    }
}
