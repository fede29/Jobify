package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Message;
import com.taller2.fiuba.jobify.R;

/**
 * View class that manages a message view. Abstract because it must be extended by ContactMessage
 * and UserMessage
 */
public class MessageView extends LinearLayout {

    public MessageView(Context context) {
        super(context);
        initialize();
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public static MessageView instantiateFrom(Context ctx, Message msg) {
        if (msg.sentByUser()) return new UserMessageView(ctx);
        else return new ContactMessageView(ctx);
    }

    public void setupView(Message message) {
        ((TextView) findViewById(R.id.message_text)).setText(message.getText());
    }


    protected void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_message, this);
    }

}
