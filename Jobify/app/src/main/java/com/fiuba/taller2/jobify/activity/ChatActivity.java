package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.fiuba.taller2.jobify.Chat;
import com.fiuba.taller2.jobify.adapter.MessagesListAdapter;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;

public class ChatActivity extends Activity {

    Chat chat;
    MessagesListAdapter messagesAdapter;

    static class ExtrasKeys {
        public final static String CHAT = "chat";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chat = (Chat) getIntent().getExtras().getSerializable(ExtrasKeys.CHAT);

        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(chat.getContact().getFullName());
        }

        findViewById(R.id.new_message_text).getBackground().clearColorFilter();
        ListView messagesList = (ListView) findViewById(R.id.messages_list);
        if (chat.hasMessagesLoaded()) {
            messagesAdapter = new MessagesListAdapter(this, chat.getMessages());
        } else {
            messagesAdapter = new MessagesListAdapter(this);
            AppServerRequest.getMessages(chat, new MessagesLoadCallback());
        }
        messagesList.setAdapter(messagesAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent createIntent(Context ctx, Chat chat) {
        Intent intent = new Intent(ctx, ChatActivity.class);
        intent.putExtra(ExtrasKeys.CHAT, chat);
        return intent;
    }


    /*************************************** PRIVATE STUFF ****************************************/

    private void setupMessages() {
        messagesAdapter.addAll(chat.getMessages());
    }

    private class MessagesLoadCallback extends HttpCallback {

        @Override
        public void onResponse() {
            try {
                if (statusIs(200)) {
                    chat.hydrateMessages(getJSONResponse().getJSONArray(JSONConstants.Chat.MESSAGES));
                    runOnUiThread(new SetupMessages());
                } else {
                    Log.e("Messages request", String.format("code = %d", getStatusCode()));
                }
            } catch (JSONException e) {
                Log.e("Messages JSON Load", e.getMessage());
                e.printStackTrace();
            }
        }

        private class SetupMessages implements Runnable {
            @Override
            public void run() {
                setupMessages();
            }
        }
    }

}
