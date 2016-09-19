package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taller2.fiuba.jobify.R;

public class ContactMessageView extends MessageView {

    public ContactMessageView(Context context) {
        super(context);
        initialize();
    }

    public ContactMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ContactMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }


    /**************************************** PRIVATE STUFF ***************************************/

    @Override
    protected void initialize() {
        super.initialize();
        findViewById(R.id.left_padding).setVisibility(GONE);
        findViewById(R.id.right_padding).setVisibility(VISIBLE);
        ((LinearLayout) findViewById(R.id.message_layout)).setGravity(Gravity.START);
        TextView messageBox = (TextView) findViewById(R.id.message_text);
        messageBox.setBackgroundColor(getResources().getColor(R.color.darkwhite));
        messageBox.setTextColor(getResources().getColor(android.R.color.black));
    }

}
