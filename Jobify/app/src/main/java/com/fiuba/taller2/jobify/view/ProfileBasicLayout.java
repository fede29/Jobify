package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.User;
import com.taller2.fiuba.jobify.R;

public class ProfileBasicLayout extends RelativeLayout {

    TextView name, jobPosition, contactsNumber;

    public ProfileBasicLayout(Context context) {
        super(context);
        initialize();
    }

    public ProfileBasicLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ProfileBasicLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setViews(User user) {
        name.setText(user.getFullname());

        String pos = user.getJobPosition().toString();
        if (! pos.isEmpty()) jobPosition.setText(pos);
        else jobPosition.setVisibility(GONE);

        int contactsNo = user.getContacts().size();
        if (contactsNo > 0) contactsNumber.setText(String.valueOf(contactsNo) + " contacts");
        else contactsNumber.setVisibility(GONE);

    }

    /************************************** PRIVATE STUFF *****************************************/

    private void initialize() {
        inflate(getContext(), R.layout.view_profile_basic_info, this);
        name = (TextView) findViewById(R.id.name);
        jobPosition = (TextView) findViewById(R.id.job_position);
        contactsNumber = (TextView) findViewById(R.id.no_contacts);
    }

}
