package com.lesnyg.mymovieinfoapp.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lesnyg.mymovieinfoapp.models.Result;

import java.util.List;

@Dao
public interface ResultDao {
    @Query("SELECT * FROM result ")
    LiveData<List<Result>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(Result result);

    @Delete
    void deleteFavorite(Result result);

}
