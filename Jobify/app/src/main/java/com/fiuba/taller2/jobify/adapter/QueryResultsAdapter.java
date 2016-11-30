package com.fiuba.taller2.jobify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.ContactActivity;
import com.squareup.picasso.Picasso;
import com.taller2.fiuba.jobify.R;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class QueryResultsAdapter extends RecyclerView.Adapter<QueryResultsAdapter.ViewHolder> {

    Context context;
    List<User> results;


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


    public QueryResultsAdapter(Context ctx) {
        context = ctx;
        results = new LinkedList<>();
    }

    public void setResults(List<User> results) {
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
        User user = results.get(position);
        Context context = holder.contactPic.getContext();

        holder.contactName.setText(user.getFullname());
        holder.jobPosition.setText(user.getJobPosition().getName());
        if (user.hasProfilePic()) holder.contactPic.setImageBitmap(user.getPicture());

        holder.setOnClickListener(new OnContactClickListener(user));
    }

    @Override
    public int getItemCount() {
        return results != null ? results.size() : 0;
    }


    /**************************************** PRIVATE STUFF ***************************************/

    private class OnContactClickListener implements View.OnClickListener {
        private User user;

        public OnContactClickListener(User c) {
            user = c;
        }

        @Override
        public void onClick(View view) {
            context.startActivity(ContactActivity.createIntent(context, user.toContact()));
        }
    }

}
