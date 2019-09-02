package com.example.photo_community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.LinkedList;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder>{
    List<Post> mData = new LinkedList<>();
    Fragment holdingFragment;

    public void setmData(List<Post> mData) {
        this.mData = mData;
    }




    public PostListAdapter(List<Post> data, Fragment holdingFragment){
        mData = data;
        this.holdingFragment = holdingFragment;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        PostViewHolder postViewHolder = new PostViewHolder(view);

        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        String postKey, userPhoto, picture, title;

        postKey = mData.get(position).getPostKey();
        userPhoto = mData.get(position).getUserPhoto();
        picture = mData.get(position).getPicture();
        title = mData.get(position).getTitle();


        holder.bind(mData.get(position), userPhoto, picture, title, holdingFragment);


    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView mUserPhoto, mPicture ;
        TextView mTitle;
        TextView userEmail;
        RecyclerView mCommentsListRv;
        LinearLayoutManager mLayoutManager;
        CommentListAdapter mCommentListAdapter;
        Button btn, deletePost, updatePost;
        String postKeyOfClass;
        EditText content;
        List<Comment> mData = new LinkedList<>();
        CommentViewModel viewData;
        HomeViewModel homeViewModel;
        LiveData<List<Comment>> liveData;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserPhoto = itemView.findViewById(R.id.row_post_uploader_image);
            mPicture = itemView.findViewById(R.id.post_row_image);
            mTitle = itemView.findViewById(R.id.row_post_title);

            content = itemView.findViewById(R.id.row_post_comment);
            btn = itemView.findViewById(R.id.row_post_add_comment_btn);
            deletePost = itemView.findViewById(R.id.row_post_delete_post);
            updatePost = itemView.findViewById(R.id.row_post_updata_post);
            userEmail = itemView.findViewById(R.id.row_post_user_email);


            mCommentsListRv = itemView.findViewById(R.id.recycler_view_comments);
            mLayoutManager = new LinearLayoutManager(itemView.getContext());
            mCommentsListRv.setLayoutManager(mLayoutManager);
            mCommentsListRv.setHasFixedSize(true);
            mCommentListAdapter = new CommentListAdapter(mData);

            homeViewModel = ViewModelProviders.of(holdingFragment).get(HomeViewModel.class);


        }

        public void bind(Post post, String userPhoto,String picture, String title, Fragment holdingFragment){
            dealButtonsAccrdingFragment(holdingFragment, post, picture, title);
            postKeyOfClass = post.getPostKey();
            if (userPhoto != null)
                Glide.with(LogInAndRegister.context).load(userPhoto).into(mUserPhoto);
            if (picture != null)
                Glide.with(LogInAndRegister.context).load(picture).into(mPicture);
            if (mTitle != null)
                mTitle.setText(title);
            mCommentsListRv.setAdapter(mCommentListAdapter);
            userEmail.setText(post.getEmail());

            viewData = ViewModelProviders.of(holdingFragment).get(CommentViewModel.class);
            viewData.setListener(post.getPostKey());
            liveData = viewData.getmData();
            liveData.observe(holdingFragment, new Observer<List<Comment>>() {
                @Override
                public void onChanged(List<Comment> comments) {
                    if (CommentViewModel.changedPostKey == postKeyOfClass) {
                        mCommentListAdapter.setmData(comments);
                        mCommentListAdapter.notifyDataSetChanged();
                    }
                }
            });







            Model.instance.getAllcommentsOfPost(post.getPostKey(), new Model.GetAllCommentsOfPostListener() {
                @Override
                public void onComplete(List<Comment> data) {
                    mData = data;
                    mCommentListAdapter.setmData(data);
                    mCommentListAdapter.notifyDataSetChanged();
                }
            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Comment comment = new Comment(content.getText().toString(), MyApp.getCurrentUserId(), post.getPostKey(), "user image", "email");
                    Model.instance.addComment(comment, new Model.addCommentListener() {

                        @Override
                        public void onComplete(Comment comment) {
                            mData.add(comment);
                            mCommentListAdapter.setmData(mData);
                            mCommentListAdapter.notifyDataSetChanged();
                            holdingFragment.onAttach(holdingFragment.getContext());
                        }
                    });
                }
            });

        }
        private void dealButtonsAccrdingFragment(Fragment fragment, Post post, String picture, String title){
            if(Home.class == fragment.getClass()){
                deletePost.setVisibility(View.GONE);
                updatePost.setVisibility(View.GONE);
            }
            else {
                deletePost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Model.instance.deletePost(post);
                    }
                });

                ProfileDirections.ActionProfileFragmentToUpdateFragment actionToUpdate = ProfileDirections.actionProfileFragmentToUpdateFragment(post.getPostKey(), picture, title);
                updatePost.setOnClickListener(Navigation.createNavigateOnClickListener(actionToUpdate));

            }

        }
    }
}
