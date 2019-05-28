package com.lesnyg.movieinfoapp.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.lesnyg.movieinfoapp.models.Comment;
import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment WHERE movieId=:movieid")
    LiveData<List<Comment>> commentgetAll(int movieid);

    @Insert
    void insertComment(Comment comment);

    @Delete
    void deleteComment(Comment comment);



}
