package com.kianncs.softwareengineering_libraryapp.Entity;

/**
 * Created by Yq on 26/10/2016.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kianncs.softwareengineering_libraryapp.R;


public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView userNameView;
    public TextView authorView;
    public TextView priceView;
    public TextView conditionView;


    public PostViewHolder(View itemView) {
        super(itemView);

        userNameView = (TextView) itemView.findViewById(R.id.post_username);
        titleView = (TextView) itemView.findViewById(R.id.book_title);
        authorView = (TextView) itemView.findViewById(R.id.book_author);
        priceView = (TextView) itemView.findViewById(R.id.book_price);
        conditionView = (TextView) itemView.findViewById(R.id.book_condition);
    }

    public void bindToPost(Post post) {
        userNameView.setText(post.getUserName());
        titleView.setText(post.getBookTitle());
        authorView.setText(post.getBookAuthor());
        priceView.setText(post.getBookPrice());
        conditionView.setText(post.getBookCondition());
    }
}
