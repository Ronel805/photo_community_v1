package com.example.photo_community;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//import com.google.firebase.database.ServerValue;
//import com.google.firebase.firestore.ServerTimestamp;

@Entity
public class Comment {

    @PrimaryKey
    @NonNull
    String commentId;
    String content;
    String uid;
    String postKey;
    String userImage;
    String userEmail;


    public Comment() {
    }

    @Ignore
    public Comment(String commentId, String content, String uid, String postKey ,String uimg, String userEmail) {
        this.commentId = commentId;
        this.content = content;
        this.uid = uid;
        this.postKey = postKey;
        this.userImage = uimg;
        this.userEmail = userEmail;
    }

    @Ignore
    public Comment(String content, String uid, String postKey ,String uimg, String userEmail) {
        this.content = content;
        this.uid = uid;
        this.postKey = postKey;
        this.userImage = uimg;
        this.userEmail = userEmail;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}