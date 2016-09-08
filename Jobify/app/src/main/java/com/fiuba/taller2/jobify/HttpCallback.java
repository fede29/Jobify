package com.fiuba.taller2.jobify;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This class is a skelleton for a HTTP request callback from the AppServer. It implements basic
 *  and generic error handling.
 * It is not ment to be instanciated, but only to be extended.
 * Child classes should implement their own onFailure() and onResponse() methods, calling
 *  the super methods first.
 */
public abstract class HttpCallback implements Callback {

    private Activity activity;


    public HttpCallback (Activity a) {
        activity = a;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        // TODO: Log error
        String message;
        if (e instanceof SocketTimeoutException) {
            message = "Connection failed. Please, check your Internet connection.";
        } else {
            message = "There was an error. Please try again later.";
        }
        showLongToast(message);
    }

    @Override
    public void onResponse(Call call, Response httpResponse) throws IOException {
        // TODO: Log error
        if (httpResponse.code() == 500) {
            showLongToast("There was a problem, please try again later");
        }
    }

    /*************************************** PRIVATE STUFF ****************************************/

    protected void announceError(String message) {
        // TODO: Must log and show toast
        showLongToast(message);
    }

    protected void announceDefaultError() {
        // TODO: Log
        showLongToast("There was an error. Please, try again later");
    }

    protected void showLongToast (final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
