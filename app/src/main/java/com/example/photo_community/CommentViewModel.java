package com.example.photo_community;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CommentViewModel extends ViewModel {
    MutableLiveData<List<Comment>> mData = new MutableLiveData<>();
    static String changedPostKey;
    public CommentViewModel(){
    }
    public LiveData<List<Comment>> getmData() {
        return mData;
    }

    public void setmData(List<Comment> mData) {
        this.mData.setValue(mData);
    }

    public void setListener(final String postKey) {
        Model.instance.getAllcommentsOfPost(postKey, new Model.GetAllCommentsOfPostListener() {

            @Override
            public void onComplete(List<Comment> data) {
                changedPostKey = postKey;
                mData.setValue(data);
            }
        });

    }
}
