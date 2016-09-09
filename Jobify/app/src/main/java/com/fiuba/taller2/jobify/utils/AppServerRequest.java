package com.fiuba.taller2.jobify.utils;


import com.fiuba.taller2.jobify.User;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class AppServerRequest {

    private static final String BASE_URL = "http://192.168.0.109:5000/";
    private static final OkHttpClient client = new OkHttpClient();
    private static String token;

    public static void updateToken(String t) {
        token = t;
    }

    public static void login(String email, String password, Callback callback) {
        // TODO: Modify parameters, send via JSON
        RequestBody formBody = new FormBody.Builder().build();
        Request request = new Request.Builder().url(BASE_URL + RequestConstants.Routes.LOGIN)
                .post(formBody)
                .addHeader(RequestConstants.UserParams.EMAIL, email)
                .addHeader(RequestConstants.UserParams.PASSWORD, password)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void getContacts(User user, Callback contactsLoadCallback) {
        String url = String.format(
                BASE_URL + "%s/%d/%s",
                RequestConstants.Routes.USERS,
                user.getID(),
                RequestConstants.Routes.CONTACTS
        );
        get(url, contactsLoadCallback);
    }

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    /************************************** PRIVATE STUFF *****************************************/

    private static class RequestConstants {
        public class Routes {
            public final static String LOGIN = "session";
            public final static String USERS = "users";
            public final static String CONTACTS = "contacts";
        }

        public class UserParams {
            public final static String EMAIL = "email";
            public final static String PASSWORD = "password";
        }
    }
}
