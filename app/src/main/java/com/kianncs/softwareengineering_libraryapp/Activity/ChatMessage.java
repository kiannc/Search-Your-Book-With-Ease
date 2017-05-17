package com.kianncs.softwareengineering_libraryapp.Activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kianncs.softwareengineering_libraryapp.ChatRoomManager;
import com.kianncs.softwareengineering_libraryapp.Entity.CommentAdapter;
import com.kianncs.softwareengineering_libraryapp.Entity.User;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;
import com.kianncs.softwareengineering_libraryapp.UserListener;

public class ChatMessage extends AppCompatActivity {

    private Button btn_send_msg;
    private EditText input_msg;
    private TextView showUsername;

    private String room_name;
    private String user_name;
    private ChatRoomManager chatRoomManager;
    private RecyclerView mCommentsRecycler;
    private CommentAdapter mAdapter;
    private UserAccountManager userAccountManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_message);

        btn_send_msg = (Button) findViewById(R.id.btn_send);
        input_msg = (EditText) findViewById(R.id.msg_input);
        mCommentsRecycler = (RecyclerView) findViewById(R.id.recycler_comments);
        showUsername = (TextView) findViewById(R.id.tvShowUsernameChat);

        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));

        room_name = getIntent().getExtras().get("room_name").toString();
        user_name = getIntent().getExtras().get("user_name").toString();
        showUsername.setText(user_name);

        chatRoomManager = new ChatRoomManager();
        userAccountManager = new UserAccountManager();

        mAdapter = new CommentAdapter(this, room_name,true);
        mCommentsRecycler.setAdapter(mAdapter);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = userAccountManager.getCurrentUserID();
                if (!input_msg.getText().equals(null)){
                    userAccountManager.getUser(userID, new UserListener() {
                        @Override
                        public void onUserDataChange(User user) {
                            String commentText = input_msg.getText().toString();
                            chatRoomManager.createNewMessage(room_name, userID, user.getName(), commentText);
                            input_msg.setText(null);
                        }
                    });

                }
            }
        });
    }
}
