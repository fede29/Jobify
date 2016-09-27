package com.fiuba.taller2.jobify;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Message implements Serializable {

    @SerializedName("id")           int id;
    @SerializedName("text")         String text;
    @SerializedName("from_user")    Boolean sentByUser;

    public static Message newFromUser(String text) {
        Message msg = new Message();
        msg.text = text;
        msg.sentByUser = true;
        return msg;
    }

    public String toString() { return text; }

    public Boolean sentByUser() {
        return sentByUser;
    }

    public String getText() {
        return text;
    }

    public String serialize() {
        return new Gson().toJson(this);
    }
}
