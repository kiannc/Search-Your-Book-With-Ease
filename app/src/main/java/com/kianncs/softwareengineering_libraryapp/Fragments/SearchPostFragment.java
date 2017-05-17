package com.kianncs.softwareengineering_libraryapp.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kianncs.softwareengineering_libraryapp.Activity.PostDetailActivity;
import com.kianncs.softwareengineering_libraryapp.Entity.Post;
import com.kianncs.softwareengineering_libraryapp.Entity.PostViewHolder;
import com.kianncs.softwareengineering_libraryapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPostFragment extends Fragment {



    private static final String TAG = "SeachPostsFragment";

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private String keyword = "";
    private EditText searchText;
    private Button searchButton;
    private ProgressDialog progressBar;
    private ImageView imAlert;
    private TextView tvNoResult;

    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_search_post, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.search_list);
        mRecycler.setHasFixedSize(true);
        searchText = (EditText) rootView.findViewById(R.id.searchText);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        imAlert = (ImageView) rootView.findViewById(R.id.imAlertSearchPost);
        tvNoResult = (TextView) rootView.findViewById(R.id.tvNoResultFoundSeachPost);
        progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Getting Result");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);



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
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imAlert.setVisibility(View.GONE);
                tvNoResult.setVisibility(View.GONE);
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(searchButton.getWindowToken(), 0);
                keyword =searchText.getText().toString();
                if(keyword.isEmpty()){
                    validateForm();
                }else{
                    progressBar.show();
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
                            progressBar.dismiss();
                        }
                    };
                    mRecycler.setAdapter(mAdapter);

                }

            }


        });
    }

    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(searchText.getText().toString())) {
            searchText.setError("Required");
            result = false;
        } else {
            searchText.setError(null);
        }

        return result;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public Query getQuery(DatabaseReference databaseReference){
        return databaseReference.child("Posts").orderByChild("bookTitle").equalTo(keyword);
    }

}
