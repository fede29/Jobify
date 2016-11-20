package com.fiuba.taller2.jobify;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Message implements Serializable {

    @SerializedName("text")         String text;
    @SerializedName("from")         String from;
    @SerializedName("to")           String to;


    public Message() {}

    public Message(String text, String from, String to) {
        this.text = text;
        this.from = from;
        this.to = to;
    }

    public String toString() { return text; }

    public String getText() {
        return text;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String serialize() {
        return new Gson().toJson(this);
    }
}
