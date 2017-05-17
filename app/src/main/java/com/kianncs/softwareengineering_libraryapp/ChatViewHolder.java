package com.kianncs.softwareengineering_libraryapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Yq on 2/11/2016.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder {
    private TextView chatRoomName;

    public ChatViewHolder (View itemView){
        super(itemView);
        chatRoomName = (TextView) itemView.findViewById(R.id.chatroom_name);
    }

    public void bindToChatroom(String name){
        chatRoomName.setText(name);
    }
}
