package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.fiuba.taller2.jobify.User;
import com.taller2.fiuba.jobify.R;

public class ProfileExtendedLayout extends RelativeLayout {

    public ProfileExtendedLayout(Context context) {
        super(context);
        initialize();
    }

    public ProfileExtendedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ProfileExtendedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setViews(User u) {

    }


    /*************************************** PRIVATE STUFF ****************************************/

    private void initialize() {
        inflate(getContext(), R.layout.view_profile_extended_info, this);

    }
}
