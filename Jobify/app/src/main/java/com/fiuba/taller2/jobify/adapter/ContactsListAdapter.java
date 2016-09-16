package com.fiuba.taller2.jobify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Contact;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsListAdapter extends ArrayAdapter<Contact> {

    private class ContactViewHolder {
        private CircleImageView profilePic;
        private TextView name, jobPosition;
    }

    public ContactsListAdapter(Context context, List<Contact> contacts) {
        super(context, R.layout.view_contact, contacts.toArray(new Contact[0]));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: ViewHolder patern
        Contact contact = getItem(position);
        ContactViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.view_contact, parent, false);

        holder = new ContactViewHolder();
        holder.profilePic = (CircleImageView) convertView.findViewById(R.id.contact_pic);
        holder.name = (TextView) convertView.findViewById(R.id.contact_name);
        holder.jobPosition = (TextView) convertView.findViewById(R.id.job_position);
        convertView.setTag(holder);
        holder.name.setText(contact.getFullName());
        if (contact.hasProfilePic())
            Picasso.with(getContext()).load(contact.getPictureURL()).into(holder.profilePic);

        return convertView;
    }

}
