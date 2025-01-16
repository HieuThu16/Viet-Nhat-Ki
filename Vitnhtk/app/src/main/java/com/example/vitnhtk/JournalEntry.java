package com.example.vitnhtk;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "journal_entries")
public class JournalEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private String positive;
    private String negative;
    private String progress;
    private String learned;

    public JournalEntry(String date, String positive, String negative, String progress, String learned) {
        this.date = date;
        this.positive = positive;
        this.negative = negative;
        this.progress = progress;
        this.learned = learned;
    }

    // Getter and Setter methods
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getPositive() { return positive; }
    public void setPositive(String positive) { this.positive = positive; }
    public String getNegative() { return negative; }
    public void setNegative(String negative) { this.negative = negative; }
    public String getProgress() { return progress; }
    public void setProgress(String progress) { this.progress = progress; }
    public String getLearned() { return learned; }
    public void setLearned(String learned) { this.learned = learned; }
    @Override
    public String toString() {
        return "JournalEntry{" +
                "date='" + date + '\'' +
                ", positive='" + positive + '\'' +
                ", negative='" + negative + '\'' +
                ", progress='" + progress + '\'' +
                ", learned='" + learned + '\'' +
                '}';
    }
}
