package com.example.photo_community;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HomeViewModel extends ViewModel {
    MutableLiveData<List<Post>> mData = new MutableLiveData<>();
    public HomeViewModel(){
        Model.instance.getAllPosts(new Model.GetAllPostsListener() {
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


}
