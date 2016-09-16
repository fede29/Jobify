package com.fiuba.taller2.jobify.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.ContactActivity;
import com.fiuba.taller2.jobify.adapter.ContactsListAdapter;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;


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

        contactsList.setAdapter(new ContactsListAdapter(getActivity(), user.getContacts()));
        contactsList.setOnItemClickListener(new OnContactClickListener());

        return rootView;
    }


    /************************************* PRIVATE STUFF ******************************************/

    private class OnContactClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Contact contact = (Contact) adapterView.getItemAtPosition(i);
            startActivity(ContactActivity.createIntent(getContext(), contact));
        }
    }

}
