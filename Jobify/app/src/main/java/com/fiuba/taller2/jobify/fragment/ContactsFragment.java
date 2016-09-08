package com.fiuba.taller2.jobify.fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fiuba.taller2.jobify.Chat;
import com.fiuba.taller2.jobify.User;
import com.taller2.fiuba.jobify.R;

import java.util.List;


public class ContactsFragment extends Fragment {

    private User user;
    private View rootView;

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
        rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ListView contactsList = (ListView) rootView.findViewById(R.id.contacts_list);
        // contactsList.setAdapter(new ContactsListAdapter(user.getContacts()));

        return rootView;
    }


    /**
     * *********************************** PRIVATE STUFF ****************************************
     */

    private class ContactsListAdapter extends ArrayAdapter<User> {

        public ContactsListAdapter(Context context, int resource) {
            super(context, resource);
        }

        public ContactsListAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public ContactsListAdapter(Context context, int resource, User[] objects) {
            super(context, resource, objects);
        }

        public ContactsListAdapter(Context context, int resource, int textViewResourceId, User[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public ContactsListAdapter(Context context, int resource, List<User> objects) {
            super(context, resource, objects);
        }

        public ContactsListAdapter(Context context, int resource, int textViewResourceId, List<User> objects) {
            super(context, resource, textViewResourceId, objects);
        }
    }

}
