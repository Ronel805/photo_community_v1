package com.example.photo_community;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.photo_community.Model;
import com.example.photo_community.Post;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    RecyclerView mPostsListRv;
    LinearLayoutManager mLayoutManeger;
    PostListAdapter mPostListAdapter;
    HomeViewModel viewData;
    LiveData<List<Post>> liveData;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewData = ViewModelProviders.of(this).get(HomeViewModel.class);
        liveData = viewData.getmData();
        liveData.observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                List<Post> notDeletetPosts = new LinkedList<>();
                for(Post post: posts){
                    if (post.getPicture() != null)
                        notDeletetPosts.add(post);
                }
                mPostListAdapter.setmData(notDeletetPosts);
                mPostListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mPostsListRv = v.findViewById(R.id.recycler_view_posts);
        mLayoutManeger = new LinearLayoutManager(v.getContext());
        mPostsListRv.setLayoutManager(mLayoutManeger);
        mPostsListRv.setHasFixedSize(true);
        mPostListAdapter = new PostListAdapter(viewData.getmData().getValue(), this);
        mPostsListRv.setAdapter(mPostListAdapter);



        return v;

    }

}
