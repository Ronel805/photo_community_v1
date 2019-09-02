package com.example.photo_community;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    MutableLiveData<List<Post>> mData = new MutableLiveData<>();
    static MutableLiveData<User> user = new MutableLiveData<>();

    public ProfileViewModel(){

    }
    public void setListenerProfile(String userId){
        Model.instance.getAllPostsByUserId(new Model.GetAllPostsListener() {//change to get all by id
            @Override
            public void onComplete(List<Post> data) {
                mData.setValue(data);
            }
        });

    }
    public LiveData<List<Post>> getmData() {
        return mData;
    }

    public void setmData(List<Post> mData) {
        this.mData.setValue(mData);
    }

    public void getUserById(String id){
        Model.instance.getUserById(id, new Model.getUserListener() {
            @Override
            public void onComplete(User u) {
                user.setValue(u);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public LiveData<User> getUser(){
        return user;
    }

    public String getConnectedUserId(){
        return Model.instance.getCurrentUserId();
    }
}
