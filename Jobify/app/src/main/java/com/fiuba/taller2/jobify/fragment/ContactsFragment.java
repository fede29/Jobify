package com.fiuba.taller2.jobify.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.HttpCallback;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
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

        if (user.hasContactsLoaded()) {
            contactsList.setAdapter(new ContactsListAdapter(getActivity(), user.getContacts()));
        } else {
            ContactsListAdapter adapter = new ContactsListAdapter(getActivity());
            contactsList.setAdapter(adapter);
            AppServerRequest.getContacts(user, new ContactsLoadCallback(getActivity(), adapter));
        }

        return rootView;
    }


    /************************************* PRIVATE STUFF ******************************************/

    private class ContactsListAdapter extends ArrayAdapter<Contact> {

        private class ContactViewHolder {
            private CircleImageView profilePic;
            private TextView name, jobPosition;
        }


        public ContactsListAdapter(Context context) {
            super(context, R.layout.view_contact);
        }

        public ContactsListAdapter(Context context, List<Contact> contacts) {
            super(context, R.layout.view_contact, contacts.toArray(new Contact[0]));
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Contact contact = getItem(position);
            ContactViewHolder holder;
            if (convertView == null || true) {
                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.view_contact, parent, false);

                holder = new ContactViewHolder();
                holder.profilePic = (CircleImageView) convertView.findViewById(R.id.contact_pic);
                holder.name = (TextView) convertView.findViewById(R.id.contact_name);
                holder.jobPosition = (TextView) convertView.findViewById(R.id.job_position);
                convertView.setTag(holder);
            } else {
                holder = (ContactViewHolder) convertView.getTag();
            }

            holder.name.setText(contact.getFullName());
            if (contact.hasProfilePic())
                Picasso.with(getContext()).load(contact.getPictureURL()).into(holder.profilePic);

            /*
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.view_contact, parent, false);
            ((TextView) convertView.findViewById(R.id.contact_name)).setText(contact.getFullName());
            WebCircleImageView contactPic = (WebCircleImageView) convertView.findViewById(R.id.contact_pic);
            contactPic.setImageFromURL(contact.getPictureURL());
            //*/

            return convertView;
        }

    }

    private class ContactsLoadCallback extends HttpCallback {

        ContactsListAdapter adapter;

        public ContactsLoadCallback(Activity activity, ContactsListAdapter adapter) {
            super(activity);
            this.adapter = adapter;
        }

        @Override
        public void onStatus200(Call call, Response httpResponse) {
            try {
                JSONArray jsonContacts = getJSONResponse().getJSONArray(JSONConstants.Arrays.CONTANCTS);
                ArrayList<Contact> contacts = new ArrayList<>();
                for (int i = 0; i < jsonContacts.length(); ++i) {
                    contacts.add(Contact.hydrate(jsonContacts.getJSONObject(i)));
                }
                user.addContacts(contacts);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(user.getContacts());
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                announceError(e.getMessage());
            }
        }
    }

}
