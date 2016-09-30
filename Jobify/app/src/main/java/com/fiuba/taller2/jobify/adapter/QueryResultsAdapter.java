package com.fiuba.taller2.jobify.adapter;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.ContactActivity;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class QueryResultsAdapter extends RecyclerView.Adapter<QueryResultsAdapter.ViewHolder> {

    private List<Contact> results;
    private Fragment parentFragment;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView contactPic;
        public TextView contactName, jobPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            contactPic = (CircleImageView) itemView.findViewById(R.id.contact_pic);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            jobPosition = (TextView) itemView.findViewById(R.id.job_position);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }
    }

    public QueryResultsAdapter(Fragment f) {
        parentFragment = f;
        setResults(new ArrayList<Contact>());
    }

    public QueryResultsAdapter(Fragment f, List<Contact> results) {
        parentFragment = f;
        setResults(results);
    }

    public void setResults(List<Contact> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    public void clear() {
        results.clear();
        notifyDataSetChanged();
    }

    @Override
    public QueryResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_contact_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QueryResultsAdapter.ViewHolder holder, int position) {
        Contact contact = results.get(position);
        Context context = holder.contactPic.getContext();

        holder.contactName.setText(contact.getFullName());
        holder.jobPosition.setText(contact.getJobPosition().getName());
        if (contact.hasProfilePic())
            Picasso.with(context).load(contact.getPictureURL()).into(holder.contactPic);

        holder.setOnClickListener(new OnContactClickListener(contact));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    /**************************************** PRIVATE STUFF ***************************************/

    private class OnContactClickListener implements View.OnClickListener {
        private Contact contact;

        public OnContactClickListener(Contact c) {
            contact = c;
        }

        @Override
        public void onClick(View view) {
            parentFragment.startActivity
                    (ContactActivity.createIntent(parentFragment.getActivity(), contact));
        }
    }

}
