package com.kianncs.softwareengineering_libraryapp;


import com.kianncs.softwareengineering_libraryapp.Entity.Comment;
import com.kianncs.softwareengineering_libraryapp.Entity.CommentAdapter;
import com.kianncs.softwareengineering_libraryapp.Entity.Post;

/**
 * Created by Yq on 30/10/2016.
 */

public class PostManager {
    iDatabase database = APIAndDatabaseFactory.getDatabase("firebase");
    public void createNewPost(String userID, String userName, String bookTitle, String bookAuthor, String bookPrice,
                              String bookCondition){
        Post newPost = new Post(userID,userName,bookTitle,bookAuthor,bookPrice,bookCondition);
        database.createNewPost(newPost);
    }

    public void removePost(String postKey){
        database.removePost(postKey);
    }

    public void getPostDetail(String postKey, final PostListener postListener){
        database.getPostDetail(postKey, postListener);
    }

    public void getComments(String postKey, CommentsListener commentsListener, CommentAdapter commentAdapter){
        database.getComments(postKey,commentsListener,commentAdapter);
    }

    public void createNewComment(String postKey, String userID, String userName, String commentText){
        Comment newComment = new Comment(userID, userName, commentText);
        database.createNewComment(postKey, newComment);
    }
}
