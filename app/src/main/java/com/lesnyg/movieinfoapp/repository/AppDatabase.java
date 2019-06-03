package com.lesnyg.movieinfoapp.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lesnyg.movieinfoapp.models.Comment;
import com.lesnyg.movieinfoapp.models.Result;

@Database(entities = {Result.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ResultDao resultDao();
}