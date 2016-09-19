package com.fiuba.taller2.jobify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.fiuba.taller2.jobify.Message;
import com.fiuba.taller2.jobify.view.MessageView;
import com.taller2.fiuba.jobify.R;

import java.util.Collection;


public class MessagesListAdapter extends ArrayAdapter<Message> {

    public MessagesListAdapter(Context context) {
        super(context, R.layout.view_message);
    }

    public MessagesListAdapter(Context context, Collection<Message> messages) {
        super(context, R.layout.view_message, messages.toArray(new Message[messages.size()]));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // TODO: Implement ViewHolder pattern
        MessageView messageView;

        Message message = getItem(position);
        messageView = MessageView.instantiateFrom(getContext(), message);
        messageView.setupView(message);

        return messageView;
    }
}
