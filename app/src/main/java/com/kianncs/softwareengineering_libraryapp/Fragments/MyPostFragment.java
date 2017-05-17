package com.kianncs.softwareengineering_libraryapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.kianncs.softwareengineering_libraryapp.Activity.NewPostActivity;
import com.kianncs.softwareengineering_libraryapp.Activity.PostDetailActivity;
import com.kianncs.softwareengineering_libraryapp.Entity.Post;
import com.kianncs.softwareengineering_libraryapp.Entity.PostViewHolder;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPostFragment extends Fragment {


    private static final String TAG = "MyPostsFragment";
    private DatabaseReference mDatabase;
    private FloatingActionButton floatingActionButton;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;


    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_my_post, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_new_post2);
        mRecycler = (RecyclerView) rootView.findViewById(R.id.myposts_list);
        mRecycler.setHasFixedSize(true);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(intent);
            }
        });

        //to catch onClickListner that are empty
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Query postsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class, R.layout.item_post,
                PostViewHolder.class, postsQuery) {

            protected void populateViewHolder(final PostViewHolder viewHolder, final Post model, final int position) {
                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });
                viewHolder.bindToPost(model);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUserID() {
        return new UserAccountManager().getCurrentUserID();
    }

    public Query getQuery(DatabaseReference databaseReference){
        return databaseReference.child("User-Posts").child(getUserID());
    }

}
