package com.kianncs.softwareengineering_libraryapp.Entity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.kianncs.softwareengineering_libraryapp.ChatRoomManager;
import com.kianncs.softwareengineering_libraryapp.CommentViewHolder;
import com.kianncs.softwareengineering_libraryapp.CommentsListener;
import com.kianncs.softwareengineering_libraryapp.Entity.Comment;
import com.kianncs.softwareengineering_libraryapp.PostManager;
import com.kianncs.softwareengineering_libraryapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yq on 30/10/2016.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>{
    private Context mContext;

    private List<String> mCommentIds = new ArrayList<>();
    private List<Comment> mComments = new ArrayList<>();

    private static final String TAG = "PostDetailActivity";

    private PostManager postManager;
    private ChatRoomManager chatRoomManager;

    public CommentAdapter(final Context context, String postKey, boolean chatroom) {
        mContext = context;
        postManager = new PostManager();
        chatRoomManager = new ChatRoomManager();

        if(chatroom==true){
            chatRoomManager.getMessages(postKey, new CommentsListener() {
                @Override
                public void onCommentDataChange(ArrayList<String> commentIDs, ArrayList<Comment> comments) {
                    mComments = comments;
                    mCommentIds = commentIDs;
                }
            },this);
        }
        else {
            postManager.getComments(postKey, new CommentsListener() {
                @Override
                public void onCommentDataChange(ArrayList<String> commentIDs, ArrayList<Comment> comments) {
                    mComments = comments;
                    mCommentIds = commentIDs;
                }
            }, this);
        }

    }

    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.authorView.setText(comment.userName);
        holder.bodyView.setText(comment.text);
    }

    public int getItemCount() {
        return mComments.size();
    }

    public void cleanupListener(String postKey) {
       /* if (mChildEventListener != null) {
            FirebaseDatabase.getInstance().getReference().child("Post-Comments").child(postKey).removeEventListener(mChildEventListener);
        }*/
    }

}

