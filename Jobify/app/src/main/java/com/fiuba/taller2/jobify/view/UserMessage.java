package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;

public class UserMessage extends Message {

    public UserMessage(Context context) {
        super(context);
        initialize();
    }

    public UserMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public UserMessage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }


    protected void initialize() {
        super.initialize();
        // We assume that the view_message layout is set by default for user messages
    }
}
