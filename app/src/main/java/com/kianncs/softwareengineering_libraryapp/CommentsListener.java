package com.kianncs.softwareengineering_libraryapp;




import com.kianncs.softwareengineering_libraryapp.Entity.Comment;

import java.util.ArrayList;

/**
 * Created by Yq on 31/10/2016.
 */

public interface CommentsListener {
    void onCommentDataChange(ArrayList<String> commentIDs, ArrayList<Comment> comments);
}
