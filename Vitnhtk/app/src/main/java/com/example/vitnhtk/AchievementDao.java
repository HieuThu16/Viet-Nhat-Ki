package com.example.vitnhtk;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AchievementDao {

    @Insert
    void insertAchievement(Achievement achievement);

    @Update
    void updateAchievement(Achievement achievement);

    @Delete
    void delete(Achievement achievement);

    @Query("SELECT * FROM achievement_table ORDER BY id ASC")
    List<Achievement> getAllAchievements();

    @Query("SELECT * FROM achievement_table WHERE id = :id LIMIT 1")
    Achievement getAchievementById(int id);
}
