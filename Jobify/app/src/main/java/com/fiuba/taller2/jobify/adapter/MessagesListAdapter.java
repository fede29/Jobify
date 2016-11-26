package com.fiuba.taller2.jobify.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.Message;
import com.fiuba.taller2.jobify.utils.FirebaseHelper;
import com.fiuba.taller2.jobify.view.MessageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.taller2.fiuba.jobify.R;


public class MessagesListAdapter extends FirebaseRecyclerAdapter<Message, MessagesListAdapter.ViewHolder> {

    private RecyclerView messagesRecycler;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        MessageView messageView;

        public ViewHolder(View view) {
            super(new MessageView(view.getContext()));
            messageView = (MessageView) itemView;
        }
    }


    public MessagesListAdapter(Contact contact) {
        super(Message.class, R.layout.view_empty, ViewHolder.class, FirebaseHelper.getMessagesReference(contact.getId()));
    }

    public void registerDataObserver(RecyclerView messages) {
        messagesRecycler = messages;
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                messagesRecycler.scrollToPosition(positionStart);
            }
        });
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Message model, int position) {
        MessageView view  = viewHolder.messageView;
        view.setupView(model);

    }


}
