package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.taller2.fiuba.jobify.R;

/**
 * View class that manages a message view. Abstract because it must be extended by ContactMessage
 * and UserMessage
 */
public abstract class Message extends View {

    private int id;
    private String text;

    public Message(Context context) {
        super(context);
    }

    public Message(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Message(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String msg) {
        text = msg;
        ((TextView) findViewById(R.id.message_box)).setText(text);
    }


    protected void initialize() {
        inflate(getContext(), R.layout.view_message, null);
        // TODO: Set correct width to message box
    }

}
