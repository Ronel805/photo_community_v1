package com.example.photo_community;


import android.os.AsyncTask;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE userId = :id")
    User getById(String id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User users);

}

public class UserAsyncDao{

    public static void addUser(final User user, final Model.basiconCompleteListener listener) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                ModelSql.db.UserDao().insertAll(user);
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


    public static void getUserById(Model.addUserListener listener) {
        new AsyncTask<String, String, User>() {
            @Override
            protected User doInBackground(String... strings) {
                String currentid = MyApp.getCurrentUserId();
                User user = ModelSql.db.UserDao().getById(currentid);
                return user;
            }

            @Override
            protected void onPostExecute(User data) {
                super.onPostExecute(data);
                if (listener != null && data != null)
                    listener.onComplete(data);

            }
        }.execute();
    }

//    public static void getUser(final String userKey, final  UserRepository.GetUserListener listener)
//    {
//        new AsyncTask<String, String, User>(){
//
//            @Override
//            protected User doInBackground(String... strings) {
//                return ModelSql.db.userDao().getUser(userKey);
//
//            }
//
//            @Override
//            protected void onPostExecute(User user) {
//                super.onPostExecute(user);
//                Log.d("SQL", "Get post id "+userKey);
//                listener.onResponse(user);
//
//            }
//        }.execute();
//    }
}
