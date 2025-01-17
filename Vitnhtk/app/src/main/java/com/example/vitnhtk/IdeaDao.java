package com.example.vitnhtk;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IdeaDao {

    // Thêm ý tưởng mới
    @Insert
    void insertIdea(Idea idea);

    // Cập nhật ý tưởng
    @Update
    void updateIdea(Idea idea);

    // Xóa ý tưởng
    @Delete
    void deleteIdea(Idea idea);

    // Lấy tất cả ý tưởng, sắp xếp theo thời gian
    @Query("SELECT * FROM ideas1 ORDER BY timestamp DESC")
    List<Idea> getAllIdeas();

    // Lấy ý tưởng theo ID
    @Query("SELECT * FROM ideas1 WHERE id = :id LIMIT 1")
    Idea getIdeaById(int id);
}
