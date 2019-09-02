package com.example.photo_community;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {
    public List<Comment> getmData() {
        return mData;
    }

    public void setmData(List<Comment> mData) {
        this.mData = mData;
    }

    List<Comment> mData;

    public CommentListAdapter(List<Comment> data){
        mData = data;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        CommentViewHolder commentViewHolder= new CommentViewHolder(view);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind( mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView mUserImage;
        TextView mContent;
        TextView email;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserImage = itemView.findViewById(R.id.row_comment_image);
            mContent = itemView.findViewById(R.id.row_comment_comment_text);
            email = itemView.findViewById(R.id.row_comment_email);



        }
        public void bind(Comment comment){
            if (comment.getUimg() != null)
                Glide.with(LogInAndRegister.context).load(comment.getUimg()).into(mUserImage);
            if (comment.getContent() != null)
                mContent.setText(comment.getContent());
            if (comment.getUserEmail() != null){
                email.setText(comment.getUserEmail());
            }


        }

    }
}
