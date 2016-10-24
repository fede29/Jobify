package com.fiuba.taller2.jobify.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Chat;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.ChatActivity;
import com.fiuba.taller2.jobify.adapter.ChatsListAdapter;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;


public class ChatsSection extends RelativeLayout {

    User user;
    ChatsListAdapter chatsListAdapter;
    RecyclerView chatsList;
    TextView noConversationsText;

    public final static int CHAT_ACTIVITY_REQUEST_CODE = 1;

    public ChatsSection(Context context) {
        super(context);
        initialize();
    }

    public ChatsSection(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ChatsSection(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setViewsFrom(@NonNull User u) {
        user = u;
        if (user.hasChatsLoaded()) setupView();
        else AppServerRequest.getChats(user, new GetChatsCallback());
    }

    public void onActivityResult(int reqCode, int result, Intent data) {
        if (reqCode == CHAT_ACTIVITY_REQUEST_CODE && result == Activity.RESULT_OK) {
            Chat modifiedChat = (Chat) data.getExtras().getSerializable(ChatActivity.ExtrasKeys.CHAT);
            chatsListAdapter.update(modifiedChat);
        }
    }


    /*************************************** PRIVATE STUFF ****************************************/

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.section_chats, this);
        chatsList = (RecyclerView) findViewById(R.id.chats_list);
        noConversationsText = (TextView) findViewById(R.id.no_conversations_text);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chatsList.setLayoutManager(layoutManager);
    }

    private void setupView() {
        if (user.getChats().size() > 0) noConversationsText.setVisibility(View.GONE);
        else noConversationsText.setVisibility(View.VISIBLE);
        chatsListAdapter = new ChatsListAdapter(user.getChats());
        chatsList.setAdapter(chatsListAdapter);
    }

    private class GetChatsCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                user.hydrateChats(getJSONResponse().getJSONArray(JSONConstants.Chat.CHATS));
                //getContext().runOnUiThread(new SetupView());
            } catch (JSONException e) {
                Log.e("Chats deserialization", e.getMessage());
                e.printStackTrace();
            }
        }

        private class SetupView implements Runnable {
            @Override
            public void run() {
                setupView();
            }
        }
    }

}
