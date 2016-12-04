package com.fiuba.taller2.jobify.utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.d("MyFMService", "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d("MyFMService", "FCM Notification Message: " +
                remoteMessage.getNotification());
        Log.d("MyFMService", "FCM Data Message: " + remoteMessage.getData());
    }
}
