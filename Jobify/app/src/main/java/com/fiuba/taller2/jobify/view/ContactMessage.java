package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.taller2.fiuba.jobify.R;

public class ContactMessage extends Message {

    public ContactMessage(Context context) {
        super(context);
        initialize();
    }

    public ContactMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ContactMessage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }


    /**************************************** PRIVATE STUFF ***************************************/

    @Override
    protected void initialize() {
        super.initialize();
        findViewById(R.id.left_padding).setVisibility(GONE);
        findViewById(R.id.right_padding).setVisibility(VISIBLE);
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setBackgroundColor(getResources().getColor(R.color.darkwhite));
        messageBox.setTextColor(getResources().getColor(android.R.color.black));
    }

}
