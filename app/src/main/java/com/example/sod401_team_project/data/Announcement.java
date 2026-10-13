
package com.example.sod401_team_project.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "announcements")
public class Announcement {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String message;

    @ColumnInfo(name = "published_date")
    private String publishedDate;

    private String category;

    public Announcement(String title, String message,
                        String publishedDate, String category) {
        this.title = title;
        this.message = message;
        this.publishedDate = publishedDate;
        this.category = category;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
