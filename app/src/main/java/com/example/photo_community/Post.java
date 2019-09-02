package com.example.photo_community;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Post {
    @PrimaryKey
    @NonNull
    private String postKey;
    private String title;
    private String picture;
    private String userId;
    private String userPhoto;
    private String email;
    private boolean wasDeleted;


    @Ignore
    public Post(String postKey, String title, String picture, String userId, String userPhoto, Date timeStamp, boolean wasDeleted, String email) {
        this.postKey = postKey;
        this.title = title;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.wasDeleted = wasDeleted;
        this.email = email;
    }

    @Ignore
    public Post(String postKey, String title, String picture, String userId, String userPhoto,boolean wasDeleted, String email) {
        this.postKey = postKey;
        this.title = title;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.wasDeleted = wasDeleted;
        this.email = email;
    }

    // make sure to have an empty constructor inside ur model class
    public Post() {
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setWasDeleted(boolean wasDeleted) {
        this.wasDeleted = wasDeleted;
    }

    public boolean getWasDeleted() {
        return wasDeleted;
    }

    public void setUserPhoto(String profileImage) { this.userPhoto=profileImage;    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
