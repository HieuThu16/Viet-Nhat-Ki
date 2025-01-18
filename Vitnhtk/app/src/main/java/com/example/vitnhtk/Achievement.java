package com.example.vitnhtk;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "achievement_table")
public class Achievement {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private String timestamp; // Thời gian

    // Constructor
    public Achievement(String title, String description, String timestamp) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
