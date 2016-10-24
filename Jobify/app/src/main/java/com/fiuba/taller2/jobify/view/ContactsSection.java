package com.fiuba.taller2.jobify.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.adapter.ContactsListAdapter;
import com.taller2.fiuba.jobify.R;


public class ContactsSection extends RelativeLayout {

    User user;
    TextView noContactsText;
    RecyclerView contactsList;


    public ContactsSection(Context context) {
        super(context);
        initialize();
    }

    public ContactsSection(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ContactsSection(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setViewsFrom(@NonNull User u) {
        user = u;
        // TODO: Should do callback pattern and check if contacts are loaded as in ChatsFragment
        if (user.getContacts().size() > 0) noContactsText.setVisibility(View.GONE);
        else noContactsText.setVisibility(View.VISIBLE);
        contactsList.setAdapter(new ContactsListAdapter(getContext(), user.getContacts()));
    }

    /************************************** PRIVATE STUFF *****************************************/

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.section_contacts, this);
        contactsList = (RecyclerView) findViewById(R.id.contacts_list);
        noContactsText = (TextView) findViewById(R.id.no_contacts_text);
        contactsList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
