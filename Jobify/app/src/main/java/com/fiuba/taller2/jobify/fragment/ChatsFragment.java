package com.fiuba.taller2.jobify.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.fiuba.taller2.jobify.Chat;
import com.fiuba.taller2.jobify.User;
import com.fiuba.taller2.jobify.activity.ChatActivity;
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
    RecyclerView chatsList;

    public final static int CHAT_ACTIVITY_REQUEST_CODE = 1;


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

        chatsList = (RecyclerView) rootView.findViewById(R.id.chats_list);
        if (user.hasChatsLoaded()) {
            chatsListAdapter = new ChatsListAdapter(this, user.getChats());
            chatsList.setAdapter(chatsListAdapter);
        } else {
            AppServerRequest.getChats(user.getID(), new GetChatsCallback());
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chatsList.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onActivityResult(int reqCode, int result, Intent data) {
        if (reqCode == CHAT_ACTIVITY_REQUEST_CODE && result == Activity.RESULT_OK) {
            Chat modifiedChat = (Chat) data.getExtras().getSerializable(ChatActivity.ExtrasKeys.CHAT);
            chatsListAdapter.update(modifiedChat);
        }
    }


    /*************************************** PRIVATE STUFF ****************************************/

    private void setupView() {
        chatsListAdapter = new ChatsListAdapter(this, user.getChats());
        chatsList.setAdapter(chatsListAdapter);
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
