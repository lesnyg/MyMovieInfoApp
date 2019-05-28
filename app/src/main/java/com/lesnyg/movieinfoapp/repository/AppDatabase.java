package com.lesnyg.movieinfoapp.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lesnyg.movieinfoapp.models.Comment;
import com.lesnyg.movieinfoapp.models.Result;

@Database(entities = {Result.class,Comment.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ResultDao resultDao();
    public abstract CommentDao commentDao();
//    private static AppDatabase db;
//    //싱글턴
//    public static AppDatabase getInstance(Context context) {
//        if (db == null) {
//            db = Room.databaseBuilder(context,
//                    AppDatabase.class, "db")
//                    .allowMainThreadQueries()   //Main 쓰레드 사용 ok
//                    .fallbackToDestructiveMigration()   //스키마 변경 ok
//                    .build();
//        }
//        return db;
//    }
}