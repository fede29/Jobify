package com.fiuba.taller2.jobify.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Chat;
import com.fiuba.taller2.jobify.activity.ChatActivity;
import com.fiuba.taller2.jobify.fragment.ChatsFragment;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ViewHolder> {

    private List<Chat> chats;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView chatPic;
        public TextView contactName, lastMessage;

        public ViewHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            chatPic = (CircleImageView) itemView.findViewById(R.id.chat_pic);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            lastMessage = (TextView) itemView.findViewById(R.id.last_message);
            itemView.setOnClickListener(listener);
        }
    }


    public ChatsListAdapter(List<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.view_chat, parent, false);
        return new ViewHolder(view, new OnChatClickListener(chats.get(i)));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        Chat chat = chats.get(i);
        Context context = holder.chatPic.getContext();

        holder.contactName.setText(chat.getContact().getFullName());
        holder.lastMessage.setText(chat.getLastMessage());
        if (chat.getContact().hasProfilePic())
            Picasso.with(context).load(chat.getContact().getPictureURL()).into(holder.chatPic);

        if (! chat.isRead()) {
            holder.chatPic.setBorderColor(ContextCompat.getColor(context, R.color.darkercyan));
            holder.contactName.setTextColor(ContextCompat.getColor(context, R.color.darkcyan));
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }



    private class OnChatClickListener implements View.OnClickListener {
        private Chat chat;

        public OnChatClickListener(Chat chat) {
            this.chat = chat;
        }

        @Override
        public void onClick(View view) {
            ((Activity) view.getContext()).startActivityForResult(
                    ChatActivity.createIntent(view.getContext(), chat),
                    ChatsFragment.CHAT_ACTIVITY_REQUEST_CODE
            );
        }
    }

}
