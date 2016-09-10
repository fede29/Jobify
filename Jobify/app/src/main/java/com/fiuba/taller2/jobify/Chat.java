package com.fiuba.taller2.jobify;

import android.util.Log;

import com.fiuba.taller2.jobify.constant.JSONConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class Chat {

    private int id;
    private User contact;
    private String lastMsg;


    public void loadFrom (JSONObject jsonChat) {
        try {
            id = jsonChat.getInt(JSONConstants.ID);
            contact = User.hydrate(jsonChat.getJSONObject(JSONConstants.Chat.CONTACT));
            lastMsg = jsonChat.getString(JSONConstants.Chat.LAST_MESSAGE);
        } catch (JSONException e) {
            Log.e("Chat", e.getMessage());
            e.printStackTrace();
        }
    }

}
