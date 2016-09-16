package com.fiuba.taller2.jobify.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fiuba.taller2.jobify.Chat;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.adapter.ChatsListAdapter;
import com.fiuba.taller2.jobify.constant.JSONConstants;
import com.fiuba.taller2.jobify.utils.AppServerRequest;
import com.fiuba.taller2.jobify.utils.HttpCallback;
import com.taller2.fiuba.jobify.R;

import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    User user;
    ChatsListAdapter chatsListAdapter;

    private class ExtrasKeys {
        public final static String USER = "user";
    }


    public static ChatsFragment newInstance(User user) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ExtrasKeys.USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        user = (User) getArguments().getSerializable(ExtrasKeys.USER);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);

        ListView chatsList = (ListView) rootView.findViewById(R.id.chats_list);
        if (user.hasChatsLoaded()) {
            chatsListAdapter = new ChatsListAdapter(getActivity(), user.getChats());
            chatsList.setAdapter(chatsListAdapter);
        } else {
            chatsListAdapter = new ChatsListAdapter(getActivity());
            chatsList.setAdapter(chatsListAdapter);
            AppServerRequest.getChats(user.getID(), new GetChatsCallback());
        }

        return rootView;
    }


    /*************************************** PRIVATE STUFF ****************************************/

    private void setupView() {
        chatsListAdapter.addAll(user.getChats());
    }

    private class GetChatsCallback extends HttpCallback {
        @Override
        public void onResponse() {
            try {
                user.hydrateChats(getJSONResponse().getJSONArray(JSONConstants.Chat.CHATS));
                getActivity().runOnUiThread(new SetupView());
            } catch (JSONException e) {
                Log.e("Chats deserialization", e.getMessage());
                e.printStackTrace();
            }
        }

        private class SetupView implements Runnable {
            @Override
            public void run() {
                setupView();
            }
        }
    }


}
