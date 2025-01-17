package com.example.vitnhtk;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ideas1")
public class Idea {

    @PrimaryKey(autoGenerate = true)
    private int id; // ID của ý tưởng
    private String title; // Tiêu đề ý tưởng
    private String description; // Nội dung mô tả
    private String timestamp; // Thời gian tạo ý tưởng

    // Constructor
    public Idea(String title, String description, String timestamp) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Getter và Setter
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

    @Override
    public String toString() {
        return "Idea{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
