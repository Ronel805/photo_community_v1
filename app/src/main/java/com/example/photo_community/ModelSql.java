package com.example.photo_community;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Post.class, Comment.class, User.class}, version = 15, exportSchema = false)
@TypeConverters({Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao PostDao();
    public abstract CommentDao CommentDao();
    public abstract UserDao UserDao();
}

public class ModelSql {

    static public AppLocalDbRepository db =
            Room.databaseBuilder(LogInAndRegister.context,
                    AppLocalDbRepository.class,
                    "database.db")
                    .fallbackToDestructiveMigration()
                    .build();
}