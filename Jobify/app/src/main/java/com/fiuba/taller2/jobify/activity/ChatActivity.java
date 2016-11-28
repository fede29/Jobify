package com.fiuba.taller2.jobify.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.Message;
import com.fiuba.taller2.jobify.adapter.MessagesListAdapter;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.FirebaseHelper;
import com.taller2.fiuba.jobify.R;


public class ChatActivity extends Activity {

    Contact contact;
    RecyclerView messagesList;
    EditText newMessage;
    MessagesListAdapter messagesAdapter;

    public static class ExtrasKeys {
        public final static String CONTACT = "contact";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        contact = (Contact) getIntent().getExtras().getSerializable(ExtrasKeys.CONTACT);

        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(contact.fullname());
        }

        newMessage = (EditText) findViewById(R.id.new_message_text);
        findViewById(R.id.new_message_text).getBackground().clearColorFilter();
        findViewById(R.id.send_btn).setOnClickListener(new SendMessageListener());

        messagesList = (RecyclerView) findViewById(R.id.messages_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messagesList.setLayoutManager(layoutManager);
        messagesAdapter = new MessagesListAdapter(contact);
        messagesAdapter.registerDataObserver(messagesList);
        messagesList.setAdapter(messagesAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static Intent createIntent(Context ctx, Contact contact) {
        Intent intent = new Intent(ctx, ChatActivity.class);
        intent.putExtra(ExtrasKeys.CONTACT, contact);
        return intent;
    }


    /*************************************** PRIVATE STUFF ****************************************/

    private class SendMessageListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (messagesAdapter.getItemCount() == 0)
                FirebaseHelper.createChat(AppServerRequest.getCurrentUser().toContact(), contact);

            String text = newMessage.getText().toString();
            if (! text.isEmpty()) {
                Message sentMessage = new Message(text,
                        AppServerRequest.getCurrentUser().getID(),
                        contact.getId());
                FirebaseHelper.sendMessage(sentMessage);
                newMessage.setText("");
            }
        }
    }

}
