package com.fiuba.taller2.jobify.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fiuba.taller2.jobify.Chat;
import com.fiuba.taller2.jobify.activity.ChatActivity;
import com.fiuba.taller2.jobify.utils.FirebaseHelper;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsListAdapter extends FirebaseRecyclerAdapter<Chat, ChatsListAdapter.ViewHolder> {

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        CircleImageView chatPic;
        TextView contactName, lastMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            chatPic = (CircleImageView) itemView.findViewById(R.id.chat_pic);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            lastMessage = (TextView) itemView.findViewById(R.id.last_message);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }
    }


    public ChatsListAdapter(Context ctx) {
        super(Chat.class, R.layout.view_chat, ViewHolder.class, FirebaseHelper.getChatsReference());
        context = ctx;
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Chat model, int position) {
        Picasso.with(viewHolder.itemView.getContext()).load(model.getContact().getPictureURL())
                .into(viewHolder.chatPic);
        viewHolder.contactName.setText(model.getContact().getFullname());
        viewHolder.setOnClickListener(new OnChatClickListener(model));
        viewHolder.lastMessage.setText(model.getLastMessage().getText());
    }


    /**************************************** PRIVATE STUFF ***************************************/

    private class OnChatClickListener implements View.OnClickListener {
        private Chat chat;

        public OnChatClickListener(Chat chat) {
            this.chat = chat;
        }

        @Override
        public void onClick(View view) {
            context.startActivity(ChatActivity.createIntent(context, chat));
        }
    }

}
