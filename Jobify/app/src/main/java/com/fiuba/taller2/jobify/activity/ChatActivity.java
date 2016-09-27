package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fiuba.taller2.jobify.Chat;
import com.fiuba.taller2.jobify.Message;
import com.fiuba.taller2.jobify.adapter.MessagesListAdapter;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.fiuba.taller2.jobify.utils.NonResponsiveCallback;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;

public class ChatActivity extends Activity {

    Chat chat;
    RecyclerView messagesList;
    EditText newMessage;
    MessagesListAdapter messagesAdapter;

    public static class ExtrasKeys {
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

        newMessage = (EditText) findViewById(R.id.new_message_text);
        findViewById(R.id.new_message_text).getBackground().clearColorFilter();
        findViewById(R.id.send_btn).setOnClickListener(new SendMessageListener());

        messagesList = (RecyclerView) findViewById(R.id.messages_list);
        if (chat.hasMessagesLoaded()) {
            messagesAdapter = new MessagesListAdapter(chat.getMessages());
            messagesList.setAdapter(messagesAdapter);
        } else {
            AppServerRequest.getMessages(chat, new MessagesLoadCallback());
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messagesList.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setChatResult();
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
        messagesAdapter = new MessagesListAdapter(chat.getMessages());
        messagesList.setAdapter(messagesAdapter);
    }

    private void setChatResult() {
        Intent chatIntent = new Intent();
        chatIntent.putExtra(ExtrasKeys.CHAT, chat);
        setResult(Activity.RESULT_OK, chatIntent);
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

    private class SendMessageListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String text = newMessage.getText().toString();
            if (! text.isEmpty()) {
                Message sentMessage = Message.newFromUser(text);
                AppServerRequest.sendMessage(chat, sentMessage, new NonResponsiveCallback());
                chat.addMessage(sentMessage);
                newMessage.setText("");
                messagesList.scrollToPosition(messagesAdapter.getItemCount() - 1);
            }
        }
    }

}
