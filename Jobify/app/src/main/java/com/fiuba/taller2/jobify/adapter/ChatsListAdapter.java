package com.fiuba.taller2.jobify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Chat;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsListAdapter extends ArrayAdapter<Chat> {

    public ChatsListAdapter(Context context) {
        super(context, R.layout.view_chat);
    }

    public ChatsListAdapter(Context context, Collection<Chat> chats) {
        super(context, R.layout.view_chat, chats.toArray(new Chat[chats.size()]));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: ViewHolder patern
        Chat chat = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.view_chat, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.contact_name);
        TextView lastMessage = (TextView) convertView.findViewById(R.id.last_message);
        CircleImageView chatPic = (CircleImageView) convertView.findViewById(R.id.chat_pic);
        name.setText(chat.getContact().getFullname());
        lastMessage.setText(chat.getLastMessage());
        if (chat.getContact().hasProfilePic())
            Picasso.with(getContext()).load(chat.getContact().getPictureURL()).into(chatPic);
        if (! chat.isRead()) {
            chatPic.setBorderColor(getContext().getResources().getColor(R.color.darkercyan));
            name.setTextColor(getContext().getResources().getColor(R.color.darkcyan));
        }

        return convertView;
    }

}
