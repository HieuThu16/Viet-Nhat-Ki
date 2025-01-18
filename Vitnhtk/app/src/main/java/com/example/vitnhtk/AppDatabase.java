package com.example.vitnhtk;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Chỉ khai báo @Database một lần và liệt kê tất cả các entities
@Database(entities = {Idea.class, JournalEntry.class, Achievement.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Lưu trữ một thể hiện duy nhất của cơ sở dữ liệu (Singleton pattern)
    private static volatile AppDatabase INSTANCE;

    // Các DAO (Data Access Object) cho các entity
    public abstract IdeaDao ideaDao();
    public abstract JournalDao journalDao();
    public abstract AchievementDao achievementDao(); // DAO cho Achievement

    /**
     * Lấy thể hiện của cơ sở dữ liệu, nếu chưa tạo mới thì tạo.
     * @param context - Context để tạo database.
     * @return thể hiện của AppDatabase.
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration() // Tự động tạo lại DB khi có thay đổi version mà không cần migrate dữ liệu.
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Gọi phương thức này để xóa cơ sở dữ liệu khi cần (ví dụ khi có thay đổi về schema mà không có migration).
     * */
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
