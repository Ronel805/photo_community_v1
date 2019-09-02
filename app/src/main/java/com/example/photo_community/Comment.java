package com.example.photo_community;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//import com.google.firebase.database.ServerValue;
//import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@Entity
public class Comment {

    @PrimaryKey
    @NonNull
    String commentId;
    String content;
    String uid;
    String postKey;
    String userImage;


    public Comment() {
    }

    public Comment(String commentId, String content, String uid, String postKey ,String uimg) {
        this.commentId = commentId;
        this.content = content;
        this.uid = uid;
        this.postKey = postKey;
        this.userImage = uimg;

    }


    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUimg() {
        return userImage;
    }

    public void setUimg(String uimg) {
        this.userImage = uimg;
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
}