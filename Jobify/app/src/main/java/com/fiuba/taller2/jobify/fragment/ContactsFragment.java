package com.fiuba.taller2.jobify.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.adapter.ContactsListAdapter;
import com.taller2.fiuba.jobify.R;


public class ContactsFragment extends Fragment {

    User user;
    TextView noContactsText;

    private class ExtrasKeys {
        public final static String USER = "user";
    }

    public static ContactsFragment newInstance(User user) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ExtrasKeys.USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    public ContactsFragment() {
        // Necessary empty default constructor
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        user = (User) getArguments().getSerializable(ExtrasKeys.USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView contactsList = (RecyclerView) rootView.findViewById(R.id.contacts_list);
        noContactsText = (TextView) rootView.findViewById(R.id.no_contacts_text);

        // TODO: Should do callback pattern and check if contacts are loaded as in ChatsFragment
        if (user.getContacts().size() > 0) noContactsText.setVisibility(View.GONE);
        else noContactsText.setVisibility(View.VISIBLE);
        contactsList.setAdapter(new ContactsListAdapter(getActivity(), user.getContacts()));
        contactsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

}
