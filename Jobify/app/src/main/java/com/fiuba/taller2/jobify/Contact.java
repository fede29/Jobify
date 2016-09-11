package com.fiuba.taller2.jobify;

import android.app.Activity;
import android.util.Log;

import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Contact implements Serializable {

    protected int userID;
    protected String firstName, lastName, pictureURL;
    protected User user;

    public static Contact hydrate(JSONObject thisJson) {
        Contact c = new Contact();
        c.loadFrom(thisJson);
        return c;
    }

    public void loadFrom(JSONObject thisJson) {
        try {
            userID = thisJson.getInt(JSONConstants.Contact.USER_ID);
            firstName = thisJson.getString(JSONConstants.Contact.FIRST_NAME);
            lastName = thisJson.getString(JSONConstants.Contact.LAST_NAME);
            pictureURL = thisJson.isNull(JSONConstants.Contact.PROFILE_PIC) ?
                    null : thisJson.getString(JSONConstants.Contact.PROFILE_PIC);
        } catch (JSONException e) {
            Log.e("Contact", e.getMessage());
            e.printStackTrace();
        }
    }

    public String getFullName() {
        return firstName + ' ' + lastName;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public Boolean hasProfilePic() {
        return pictureURL != null;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public Integer getUserID() {
        return userID;
    }

    public User getUser() {
        return user;
    }
}
