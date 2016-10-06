package com.fiuba.taller2.jobify;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class Chat implements Serializable {

    @SerializedName("id")           int id;
    @SerializedName("contact")      Contact contact;
    @SerializedName("last_message") String lastMessage;
    @SerializedName("is_read")      Boolean isRead;
    @Expose(deserialize = false)    ArrayList<Message> messages;
    @Expose(deserialize = false)    User user;


    public Contact getContact() {
        return contact;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public boolean isRead() {
        return isRead;
    }

    public Boolean hasMessagesLoaded() {
        return messages != null;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setUser(User u) {
        user = u;
    }

    public void hydrateMessages(JSONArray jsonMessages) {
        Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
        messages = new Gson().fromJson(jsonMessages.toString(), listType);
    }

    public String getUserID() {
        return user.getID();
    }

    public Integer getID() {
        return id;
    }

    public void addMessage(Message msg) {
        messages.add(msg);
        lastMessage = msg.getText();
    }

    public Boolean equals(Chat that) {
        return this.id == that.id;
    }
}
