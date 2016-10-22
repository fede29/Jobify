package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.taller2.fiuba.jobify.R;

public class FollowButton extends TextView {
    public FollowButton(Context context) {
        super(context);
        initialize();
    }

    public FollowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public FollowButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setFollowing() {
        setText("Folllowing");
        setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        setGravity(Gravity.CENTER);
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.following_button));
    }


    /************************************ PRIVATE STUFF *******************************************/

    private void initialize() {
        setText("Follow");
        setTextColor(ContextCompat.getColor(getContext(), R.color.darkcyan));
        setGravity(Gravity.CENTER);
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.follow_button));
    }

}
