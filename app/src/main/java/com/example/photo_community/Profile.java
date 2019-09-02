package com.example.photo_community;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    Button logout;
    RecyclerView mPostsListRv;
    LinearLayoutManager mLayoutManeger;
    PostListAdapter mPostListAdapter;
    ProfileViewModel viewData;
    LiveData<List<Post>> liveData;
    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewData = ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewData.setListenerProfile(MyApp.getCurrentUserId());
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
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = v.findViewById(R.id.profileLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance.logout();
                MyApp.setCurrentUserId(null);
                Intent intent = new Intent(getActivity(), LogInAndRegister.class);
                getActivity().finish();
                startActivity(intent);
            }
        });



        mPostsListRv = v.findViewById(R.id.profile_recycler_view);
        mLayoutManeger = new LinearLayoutManager(v.getContext());
        mPostsListRv.setLayoutManager(mLayoutManeger);
        mPostsListRv.setHasFixedSize(true);
        mPostListAdapter = new PostListAdapter(viewData.getmData().getValue(), this);
        mPostsListRv.setAdapter(mPostListAdapter);

        return  v;
    }

}
