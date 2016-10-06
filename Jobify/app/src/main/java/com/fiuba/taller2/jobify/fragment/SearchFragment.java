package com.fiuba.taller2.jobify.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fiuba.taller2.jobify.Contact;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.adapter.QueryResultsAdapter;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.taller2.fiuba.jobify.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    EditText queryText;
    ImageView searchButton;
    RecyclerView resultsList;
    QueryResultsAdapter resultsAdapter;
    ProgressBar resultsLoader;


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        queryText = (EditText) view.findViewById(R.id.query);
        searchButton = (ImageView) view.findViewById(R.id.search_btn);
        resultsList = (RecyclerView) view.findViewById(R.id.query_results);
        resultsLoader = (ProgressBar) view.findViewById(R.id.results_loader);

        resultsAdapter = new QueryResultsAdapter(this);
        resultsList.setAdapter(resultsAdapter);

        resultsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchButton.setOnClickListener(new OnSearchClickListener());

        return view;
    }

    /**************************************** PRIVATE STUFF ***************************************/

    private class OnSearchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            /*
            resultsAdapter.clear();
            resultsLoader.setVisibility(View.VISIBLE);
            AppServerRequest.searchUsers(queryText.getText().toString(), new QueryResultsCallback());
            */
            Toast.makeText(getActivity(), "In building process, we apologize for the inconveniences caused :D", Toast.LENGTH_LONG).show();
        }
    }

    private class QueryResultsCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                JSONArray usersArray = getJSONResponse().getJSONArray(JSONConstants.Arrays.CONTANCTS);
                List<Contact> contacts = Contact.hydrate(usersArray);
                getActivity().runOnUiThread(new SetResults(contacts));
            } catch (Exception e) {
                Log.e("Users query", e.getMessage());
                e.printStackTrace();
            }
        }

        private class SetResults implements Runnable {
            private List<Contact> contacts;

            public SetResults(List<Contact> c) {
                contacts = c;
            }

            @Override
            public void run() {
                resultsLoader.setVisibility(View.GONE);
                resultsAdapter.setResults(contacts);
            }
        }
    }
}
