package com.example.photo_community;

import android.os.AsyncTask;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
interface PostDao {
    @Query("select * from Post where wasDeleted=0")
    List<Post> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPosts(List<Post> data);
    @Delete
    void delete(Post post);
    @Query("DELETE FROM Post where postKey = :postKey")
    void deleteByPostKey(String postKey);
    @Query("select * from Post where userId =:userId and wasDeleted=0")
    List<Post> getAllByUserId(String userId);
    @Query("select * from Post where postKey =:post")
    Post getPostByPostKey(String post);
}

public class PostAsyncDao{

    public static void getAllPosts(final Model.GetAllPostsListener listener) {
        new AsyncTask<String, String, List <Post>>() {
            @Override
            protected List<Post> doInBackground(String... strings) {
                List<Post> list = ModelSql.db.PostDao().getAll();
                return list;
            }

            @Override
            protected void onPostExecute(List<Post> data) {
                super.onPostExecute(data);
                listener.onComplete(data);

            }
        }.execute();
    }

    public static void addPost(final Post post, final Model.basiconCompleteListener listener) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                ModelSql.db.PostDao().insertAll(post);
                return "";
            }

            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                if (listener != null)
                    listener.onComplete(true);

            }
        }.execute();
    }

    public static void addPostsAndGetPostsList(List<Post> data, final Model.GetAllPostsListener listener) {
        new AsyncTask<List<Post>, String, List <Post>>() {
            @Override
            protected List<Post> doInBackground(List<Post>... posts) {
                ModelSql.db.PostDao().insertPosts(posts[0]);

                List<Post> list = ModelSql.db.PostDao().getAll();
                return list;
            }

            @Override
            protected void onPostExecute(List<Post> data) {
                super.onPostExecute(data);
                listener.onComplete(data);

            }
        }.execute(data);
    }

    public static void getAllPostsByUserId(final String userId, final Model.GetAllPostsListener listener) {
        new AsyncTask<String, String, List <Post>>() {
            @Override
            protected List<Post> doInBackground(String... strings) {
                List<Post> list = ModelSql.db.PostDao().getAllByUserId(userId);
                return list;
            }

            @Override
            protected void onPostExecute(List<Post> data) {
                super.onPostExecute(data);
                listener.onComplete(data);

            }
        }.execute();
    }

    public static void addPostsAndGetPostsListByUserId(List<Post> data, final Model.GetAllPostsListener listener) {
        new AsyncTask<List<Post>, String, List <Post>>() {
            @Override
            protected List<Post> doInBackground(List<Post>... posts) {
                ModelSql.db.PostDao().insertPosts(posts[0]);

                List<Post> list = ModelSql.db.PostDao().getAllByUserId(MyApp.getCurrentUserId());
                return list;
            }

            @Override
            protected void onPostExecute(List<Post> data) {
                super.onPostExecute(data);
                listener.onComplete(data);

            }
        }.execute(data);
    }

    public static void getPostByPostKey(final String postKey, final Model.addPostListener listener) {

        new AsyncTask<String, String, Post>() {
            @Override
            protected Post doInBackground(String... strings) {
                Post post = ModelSql.db.PostDao().getPostByPostKey(postKey);
                return post;
            }

            @Override
            protected void onPostExecute(Post data) {
                super.onPostExecute(data);
                listener.onComplete(data);

            }
        }.execute();

    }

    public static void deletePostByPostKey(final String postKey, final Model.basiconCompleteListener listener) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                ModelSql.db.PostDao().deleteByPostKey(postKey);
                return "";
            }

            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                listener.onComplete(true);

            }
        }.execute();
    }
}

