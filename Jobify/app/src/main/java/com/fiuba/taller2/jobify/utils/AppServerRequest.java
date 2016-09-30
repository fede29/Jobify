package com.fiuba.taller2.jobify.utils;


import android.util.Log;

import com.fiuba.taller2.jobify.Chat;
import com.fiuba.taller2.jobify.Message;
import com.fiuba.taller2.jobify.User;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class AppServerRequest {

    private static final String BASE_URL = "http://192.168.0.104:5000";
    private static final OkHttpClient client = new OkHttpClient();
    private static String token;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void updateToken(String t) {
        token = t;
    }

    public static void login(String email, String password, Callback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(RequestConstants.UserParams.EMAIL, email);
            params.put(RequestConstants.UserParams.PASSWORD, password);
        } catch (JSONException e) {
            Log.e("Login request", e.getMessage());
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, params.toString());
        post(generateURL(RequestConstants.Routes.LOGIN), callback, body);
    }

    public static void register(String email, String password, Callback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(RequestConstants.UserParams.EMAIL, email);
            params.put(RequestConstants.UserParams.PASSWORD, password);
        } catch (JSONException e) {
            Log.e("Register request", e.getMessage());
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, params.toString());
        post(generateURL(RequestConstants.Routes.USERS), callback, body);
    }

    public static void getContacts(int userID, Callback callback) {
        get(
                generateURL(
                        RequestConstants.Routes.USERS, userID,
                        RequestConstants.Routes.CONTACTS
                ),
                callback
        );
    }

    public static void getUser(int userID, Callback callback) {
        get(generateURL(RequestConstants.Routes.USERS, userID), callback);
    }

    public static void updateUser(User user, Callback callback) {
        RequestBody body = RequestBody.create(JSON, user.serialize());
        put(
                generateURL(RequestConstants.Routes.USERS, String.valueOf(user.getID())),
                callback,
                body
        );
    }

    public static void getChats(int userID, Callback callback) {
        String route = generateURL(
                RequestConstants.Routes.USERS, userID,
                RequestConstants.Routes.CHATS
        );
        get(route, callback);
    }

    public static void sendMessage(Chat chat, Message newMessage, Callback callback) {
        String route = generateURL(
                RequestConstants.Routes.USERS, chat.getUserID(),
                RequestConstants.Routes.CHATS, chat.getID(),
                RequestConstants.Routes.MESSAGES
        );
        RequestBody body = RequestBody.create(JSON, newMessage.serialize());
        post(route, callback, body);
    }

    public static void getMessages(Chat chat, Callback callback) {
        String route = generateURL(
                RequestConstants.Routes.USERS, chat.getUserID(),
                RequestConstants.Routes.CHATS, chat.getID(),
                RequestConstants.Routes.MESSAGES
        );
        get(route, callback);
    }

    public static void updateLocation(User user, Callback callback) {
        String route = generateURL(
                RequestConstants.Routes.USERS, user.getID(),
                RequestConstants.Routes.LOCATION
        );
        RequestBody body = RequestBody.create(JSON, user.getPosition().serialize());
        post(route, callback, body);
    }

    public static void searchUsers(String query, Callback callback) {
        String route = addParameters(generateURL(RequestConstants.Routes.USERS), "query", query);
        get(route, callback);
    }


    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void post(String url, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void put(String url, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }


    /************************************** PRIVATE STUFF *****************************************/

    private static String generateURL(Object... uris) {
        String url = BASE_URL;
        for (Object uri : uris)
            url += String.format("/%s", uri);
        return url;
    }

    private static String addParameters(String route, Object... params) {
        route += "?";
        for (int i = 0; i < params.length; i += 2)
            route += String.format("%s=%s", params[i], params[i+1]);
        return route;
    }

    private static class RequestConstants {
        public class Routes {
            public final static String LOGIN = "session";
            public final static String USERS = "users";
            public final static String CONTACTS = "contacts";
            public final static String CHATS = "chats";
            public final static String MESSAGES = "messages";
            public final static String LOCATION = "location";
        }

        public class UserParams {
            public final static String EMAIL = "email";
            public final static String PASSWORD = "password";
        }
    }
}
