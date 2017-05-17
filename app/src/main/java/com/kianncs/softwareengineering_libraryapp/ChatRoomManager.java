package com.kianncs.softwareengineering_libraryapp;

import com.kianncs.softwareengineering_libraryapp.Entity.Comment;
import com.kianncs.softwareengineering_libraryapp.Entity.CommentAdapter;

/**
 * Created by chew on 31/10/2016.
 */

public class ChatRoomManager {
    iDatabase database = APIAndDatabaseFactory.getDatabase("firebase");
    public void createNewChatRoom(String userID, String userName,ChatRoomListener chatRoomListener)
    {
        database.createNewChatRoom(userID,userName,chatRoomListener);

    }

    public void getChatRoom(String chatKey, final ChatRoomListener chatRoomListener)
    {
        //database.getMyChats(chatKey,chatRoomListener);
    }

    public void createNewMessage(String postKey, String userID, String userName, String commentText){
        Comment newComment = new Comment(userID, userName, commentText);
        database.createNewMessage(postKey, newComment);
    }

    public void getMessages(String postKey, CommentsListener commentsListener, CommentAdapter commentAdapter) {
        database.getMessages(postKey, commentsListener, commentAdapter);
    }

}
