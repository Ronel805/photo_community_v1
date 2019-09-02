package com.example.photo_community;

import android.os.AsyncTask;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
interface CommentDao {
    @Query("select * from Comment")
    List<Comment> getAll();
    @Query("select * from Comment where postKey = :postKey")
    List<Comment> getAllByPostId(String postKey);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Comment... comments);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComments(List<Comment> data);
    @Delete
    void delete(Comment comment);
    @Query("DELETE FROM Comment where postKey = :postKey")
    void deleteComments(String postKey);
}

public class CommentAsyncDao{

    public static void getAllcommentsOfPost(final String PostKey , final Model.GetAllCommentsOfPostListener listener) {

        new AsyncTask<List<Comment>, String, List <Comment>>() {
            @Override
            protected List<Comment> doInBackground(List<Comment>... comments) {
                List<Comment> list = ModelSql.db.CommentDao().getAllByPostId(PostKey);
                return list;
            }

            @Override
            protected void onPostExecute(List<Comment> data) {
                super.onPostExecute(data);
                listener.onComplete(data);

            }
        }.execute();

    }

    public static void addCommentsAndGetCommentsaList(final String PostKey , List<Comment> data, final Model.GetAllCommentsOfPostListener listener) {

        new AsyncTask<List<Comment>, String, List <Comment>>() {
            @Override
            protected List<Comment> doInBackground(List<Comment>... comments) {
                ModelSql.db.CommentDao().insertComments(comments[0]);

                List<Comment> list = ModelSql.db.CommentDao().getAllByPostId(PostKey);
                return list;
            }

            @Override
            protected void onPostExecute(List<Comment> data) {
                super.onPostExecute(data);
                listener.onComplete(data);

            }
        }.execute(data);

    }

    public static void addComment(final Comment comment) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                ModelSql.db.CommentDao().insertAll(comment);
                return "";
            }

            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);

            }
        }.execute();
    }

    public static void deleteAllCommentsByPostKey(final String postKey, final Model.basiconCompleteListener listener) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                ModelSql.db.CommentDao().deleteComments(postKey);
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

