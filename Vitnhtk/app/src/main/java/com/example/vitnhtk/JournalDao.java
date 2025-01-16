package com.example.vitnhtk;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JournalDao {

    // Truy vấn để lấy một mục nhập nhật ký theo ngày
    @Query("SELECT * FROM journal_entries WHERE date = :date LIMIT 1")
    JournalEntry getEntryByDate(String date);

    // Chèn hoặc thay thế nhật ký
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JournalEntry journalEntry);

    // Xóa toàn bộ nhật ký (nếu cần)
    @Query("DELETE FROM journal_entries")
    void deleteAll();
}
