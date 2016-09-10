package com.fiuba.taller2.jobify;

import android.util.Log;

import com.fiuba.taller2.jobify.constant.JSONConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Contact implements Serializable {

    private String firstName, lastName, profilePicUrl;

    public static Contact hydrate(JSONObject thisJson) {
        Contact c = new Contact();
        try {
            c.firstName = thisJson.getString(JSONConstants.Contact.FIRST_NAME);
            c.lastName = thisJson.getString(JSONConstants.Contact.LAST_NAME);
            c.setPictureURL(
                    thisJson.isNull(JSONConstants.Contact.PROFILE_PIC) ?
                            null : thisJson.getString(JSONConstants.Contact.PROFILE_PIC)
            );
        } catch (JSONException e) {
            Log.e("Contact", e.getMessage());
            e.printStackTrace();
        }
        return c;
    }

    public String getFullName() {
        return firstName + ' ' + lastName;
    }

    public String getPictureURL() {
        return profilePicUrl;
    }

    public void setPictureURL(String url) {
        profilePicUrl = url;
    }

    public Boolean hasProfilePic() {
        return profilePicUrl != null;
    }
}
