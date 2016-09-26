package com.fiuba.taller2.jobify.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fiuba.taller2.jobify.Message;
import com.fiuba.taller2.jobify.view.MessageView;
import com.taller2.fiuba.jobify.R;

import java.util.List;


public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.ViewHolder> {

    private List<Message> messages;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MessageView messageView;

        public ViewHolder(MessageView view) {
            super(view);
            messageView = view;
        }
    }


    public MessagesListAdapter(List<Message> msgs) {
        messages = msgs;
    }

    public void add(Message msg) {
        messages.add(msg);
        notifyItemChanged(messages.size() - 1);
    }

    @Override
    public MessagesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessageView v = new MessageView(parent.getContext());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        Message message = messages.get(i);
        if (message.sentByUser()) holder.messageView.setupAsUsers();
        else holder.messageView.setupAsContacts();
        holder.messageView.setupView(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
