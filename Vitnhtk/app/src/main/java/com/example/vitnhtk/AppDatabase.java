package com.example.vitnhtk;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Chỉ khai báo @Database một lần và liệt kê tất cả các entities
@Database(entities = {Idea.class, JournalEntry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    // Khai báo các DAO
    public abstract IdeaDao ideaDao();
    public abstract JournalDao journalDao();

    // Phương thức khởi tạo duy nhất cho cơ sở dữ liệu
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration() // Xử lý khi thay đổi cấu trúc database
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
