package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Message;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.taller2.fiuba.jobify.R;

/**
 * View class that manages a message view. Abstract because it must be extended by ContactMessage
 * and UserMessage
 */
public class MessageView extends LinearLayout {

    private TextView messageBox;


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

    public void setupView(Message message) {
        ((TextView) findViewById(R.id.message_text)).setText(message.getText());
        if (AppServerRequest.getCurrentUser().getID().equals(message.getFrom()))
            setupAsUsers();
        else
            setupAsContacts();
    }

    public void setupAsContacts() {
        findViewById(R.id.left_padding).setVisibility(GONE);
        findViewById(R.id.right_padding).setVisibility(INVISIBLE);
        messageBox.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.message_box_darkwhite));
        messageBox.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
    }

    public void setupAsUsers() {
        findViewById(R.id.left_padding).setVisibility(INVISIBLE);
        findViewById(R.id.right_padding).setVisibility(GONE);
        setLayoutParams(createUserMessageLP());
        messageBox.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.message_box_darkcyan));
        messageBox.setTextColor(ContextCompat.getColor(getContext(), R.color.darkwhite));
    }


    /****************************************** PRIVATE STUFF *************************************/

    protected void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_message, this);
        messageBox = (TextView) findViewById(R.id.message_text);
    }

    private LayoutParams createUserMessageLP() {
        LayoutParams lp =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.END;
        return lp;
    }

}
