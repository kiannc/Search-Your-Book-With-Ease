package com.kianncs.softwareengineering_libraryapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Yq on 30/10/2016.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder{
    public TextView authorView;
    public TextView bodyView;

    public CommentViewHolder(View itemView) {
        super(itemView);

        authorView = (TextView) itemView.findViewById(R.id.comment_username);
        bodyView = (TextView) itemView.findViewById(R.id.comment_body);
    }
}
