package com.fiuba.taller2.jobify.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.HttpCallback;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.taller2.fiuba.jobify.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


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
        // First: check if user doesnt have contacts list. If not, do this:
        // contactsList.setAdapter(new ContactsListAdapter(getContext());
        // AppServerRequest.getContacts(user, new ContactsLoadCallback(this, contactsList.getAdapter()))
        // Else, if has contacts list, add them to the adapter

        return rootView;
    }


    /**
     * *********************************** PRIVATE STUFF ****************************************
     */

    private class ContactsListAdapter extends ArrayAdapter<Contact> {

        public ContactsListAdapter(Context context) {
            super(context, R.layout.view_contact);
        }

        public ContactsListAdapter(Context context, List<Contact> contacts) {
            super(context, R.layout.view_contact, (Contact[]) contacts.toArray());
        }
    }

    private class ContactsLoadCallback extends HttpCallback {

        ContactsListAdapter adapter;

        public ContactsLoadCallback(Activity activity, ContactsListAdapter adapter) {
            super(activity);
            this.adapter = adapter;
        }

        @Override
        public void onResponse(Call call, Response httpResponse) throws IOException {
            super.onResponse(call, httpResponse);
            try {
                JSONObject response = new JSONObject(httpResponse.body().string());
                JSONArray jsonContacts = response.getJSONArray(JSONConstants.Arrays.CONTANCTS);
                for (int i = 0; i < jsonContacts.length(); ++i) {
                    adapter.add(Contact.hydrate(jsonContacts.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                announceDefaultError();
            }
        }
    }

}
