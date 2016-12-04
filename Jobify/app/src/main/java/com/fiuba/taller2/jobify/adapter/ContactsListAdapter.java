package com.fiuba.taller2.jobify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.activity.ContactActivity;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder> {

    List<Contact> contacts;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View parentView;
        public CircleImageView contactPic;
        public TextView name, jobPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            name = (TextView) itemView.findViewById(R.id.contact_name);
            jobPosition = (TextView) itemView.findViewById(R.id.job_position);
            contactPic = (CircleImageView) itemView.findViewById(R.id.contact_pic);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            parentView.setOnClickListener(listener);
        }
    }


    public ContactsListAdapter(Context ctx, List<Contact> contacts) {
        this.contacts = contacts;
        context = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.view_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.name.setText(contact.fullname());
        holder.jobPosition.setText(contact.getJobPosition().toString());
        if (contact.hasProfilePic())
            Picasso.with(context).load(contact.getPictureURL()).into(holder.contactPic);
        holder.setOnClickListener(new OnContactClickListener(contact));
    }

    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }


    /*************************************** PRIVATE **********************************************/

    private class OnContactClickListener implements View.OnClickListener {
        Contact contact;

        public OnContactClickListener(Contact contact) {
            this.contact = contact;
        }

        @Override
        public void onClick(View view) {
            context.startActivity(ContactActivity.createIntent(context, contact));
        }
    }

}
