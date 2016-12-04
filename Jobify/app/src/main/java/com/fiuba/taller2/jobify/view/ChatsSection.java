package com.fiuba.taller2.jobify.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.adapter.ChatsListAdapter;
import com.fiuba.taller2.jobify.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.taller2.fiuba.jobify.R;


public class ChatsSection extends RelativeLayout {

    User user;
    ChatsListAdapter chatsListAdapter;
    RecyclerView chatsList;
    TextView noConversationsText;


    public ChatsSection(Context ctx) {
        super(ctx);
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
        setupView();
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
        Query query = FirebaseHelper.getChatsReference();
        query.addValueEventListener(new OnChatsLoadedListener());
        chatsListAdapter = new ChatsListAdapter(getContext(), query);
        chatsList.setAdapter(chatsListAdapter);
    }

    private class OnChatsLoadedListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            findViewById(R.id.progress_bar).setVisibility(GONE);
            if (dataSnapshot.getValue() == null) findViewById(R.id.no_conversations_text).setVisibility(VISIBLE);
            else findViewById(R.id.no_conversations_text).setVisibility(GONE);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
