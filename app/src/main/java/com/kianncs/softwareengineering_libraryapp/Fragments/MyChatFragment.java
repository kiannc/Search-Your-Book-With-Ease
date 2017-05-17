package com.kianncs.softwareengineering_libraryapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kianncs.softwareengineering_libraryapp.Activity.ChatMessage;
import com.kianncs.softwareengineering_libraryapp.Entity.ChatRoom;
import com.kianncs.softwareengineering_libraryapp.ChatViewHolder;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChatFragment extends Fragment {

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<ChatRoom, ChatViewHolder> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.my_chats, container, false);
        mRecycler = (RecyclerView) v.findViewById(R.id.myroomlist);
        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Query roomQuery = getQuery(FirebaseDatabase.getInstance().getReference());
        mAdapter = new FirebaseRecyclerAdapter<ChatRoom, ChatViewHolder>(ChatRoom.class, R.layout.item_chatroom,
                ChatViewHolder.class, roomQuery) {

            protected void populateViewHolder(final ChatViewHolder viewHolder, final ChatRoom model, final int position) {
                final DatabaseReference roomKey = getRef(position);
                final String roomKeyKey = roomKey.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ChatMessage.class);
                        intent.putExtra("room_name", roomKeyKey);
                        intent.putExtra("user_name", model.getUserName());
                        startActivity(intent);
                    }
                });
                viewHolder.bindToChatroom(model.getUserName());
            }
        };
        mRecycler.setAdapter(mAdapter);
        return v;
    }

    public Query getQuery(DatabaseReference databaseReference){
        return databaseReference.child("User-Chatrooms").child(new UserAccountManager().getCurrentUserID());
    }

}

