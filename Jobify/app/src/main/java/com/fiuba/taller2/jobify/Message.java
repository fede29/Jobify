package com.fiuba.taller2.jobify;

import com.google.gson.annotations.SerializedName;


public class Message {

    @SerializedName("id")           int id;
    @SerializedName("text")         String text;
    @SerializedName("from_user")    Boolean sentByUser;


    public String toString() { return text; }

    public Boolean sentByUser() {
        return sentByUser;
    }

    public String getText() {
        return text;
    }
}
