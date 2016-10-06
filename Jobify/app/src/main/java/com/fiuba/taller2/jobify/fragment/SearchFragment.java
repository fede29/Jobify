package com.fiuba.taller2.jobify.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import org.json.JSONObject;

import java.util.LinkedList;
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
    TextView noResultsText;


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
        noResultsText = (TextView) view.findViewById(R.id.no_results_text);

        queryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                return action == EditorInfo.IME_ACTION_SEARCH && searchButton.callOnClick();
            }
        });

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
            resultsAdapter.clear();
            resultsLoader.setVisibility(View.VISIBLE);
            noResultsText.setVisibility(View.GONE);
            /* TODO: Generalize search query
            AppServerRequest.searchUsers(queryText.getText().toString(), new QueryResultsCallback());
            */
            AppServerRequest.getUser(queryText.getText().toString(), new SingleUserResultCallback());
        }
    }

    private class SingleUserResultCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                if (statusIs(200)) {
                    LinkedList<Contact> contactList = new LinkedList<>();
                    JSONObject jsonUser = getJSONResponse().getJSONObject(JSONConstants.User.USER);
                    Conturtact c = Contact.hydrate(jsonUser);
                    c.setUserID(queryText.getText().toString());
                    contactList.add(c);
                    getActivity().runOnUiThread(new SetResults(contactList));
                } else {
                    getActivity().runOnUiThread(new SetResults(new LinkedList<Contact>()));
                }
            } catch (JSONException e) {
                Log.e("Single user query", e.getMessage());
                e.printStackTrace();
            }
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
    }

    public class SetResults implements Runnable {
        private List<Contact> contacts;

        public SetResults(List<Contact> c) {
            contacts = c;
        }

        @Override
        public void run() {
            resultsLoader.setVisibility(View.GONE);
            resultsAdapter.setResults(contacts);
            if (contacts.size() > 0) noResultsText.setVisibility(View.GONE);
            else noResultsText.setVisibility(View.VISIBLE);
        }
    }
}
