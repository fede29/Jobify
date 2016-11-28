package com.fiuba.taller2.jobify.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;

public class FirebaseHelper {

    static Context context;
    static FirebaseAuth auth;
    static FirebaseUser user;


    public static void initialize(Context ctx) {
        context = ctx;
        FirebaseApp.initializeApp(context);
        auth = FirebaseAuth.getInstance();
    }

    public static void register(String email, String password) {
        checkAuthNullability();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("Firebase register", task.getException() != null ? task.getException().getMessage() : "Unknown cause");
                        }
                    }
                });
    }

    public static void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                        user = auth.getCurrentUser();
                    else
                        Log.e("Firebase login", task.getException() != null ? task.getException().getMessage() : "Unknown error");
                }
            });
    }

    public static Query getMessagesReference(Object userId) {
        return FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(sanitizeKey(AppServerRequest.getCurrentUser().getID()))
                .child(sanitizeKey(userId.toString()))
                .child("messages");
    }

    public static Query getChatsReference() {
        return FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(sanitizeKey(AppServerRequest.getCurrentUser().getID()));
    }

    public static void createChat(Contact contact1, Contact contact2) {
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(sanitizeKey(contact1.getId()))
                .child(sanitizeKey(contact2.getId()))
                .child("contact")
                .setValue(contact2);
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(sanitizeKey(contact2.getId()))
                .child(sanitizeKey(contact1.getId()))
                .child("contact")
                .setValue(contact1);
    }

    public static void sendMessage(Message message) {
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(sanitizeKey(message.getFrom()))
                .child(sanitizeKey(message.getTo()))
                .child("messages")
                .push().setValue(message);
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(sanitizeKey(message.getTo()))
                .child(sanitizeKey(message.getFrom()))
                .child("messages")
                .push().setValue(message);
    }


    private static void checkAuthNullability() {
        if (auth == null)
            throw new RuntimeException("auth null. Have you run FirebaseHelper.initialize()?");
    }

    private static String sanitizeKey(String string) {
        return string.replace("@", "at").replace(".", "dot");
    }

}
