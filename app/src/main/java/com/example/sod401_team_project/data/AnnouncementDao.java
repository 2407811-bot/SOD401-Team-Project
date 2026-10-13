
package com.example.sod401_team_project.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AnnouncementDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertAnnouncement(Announcement announcement);

    @Query("SELECT * FROM announcements ORDER BY id DESC")
    List<Announcement> getAllAnnouncements();

    @Query("SELECT * FROM announcements WHERE id = :announcementId LIMIT 1")
    Announcement getAnnouncementById(int announcementId);

    @Query("SELECT * FROM announcements WHERE title LIKE '%' || :keyword || '%' OR message LIKE '%' || :keyword || '%' ORDER BY id DESC")
    List<Announcement> searchAnnouncements(String keyword);

    @Update
    int updateAnnouncement(Announcement announcement);

    @Delete
    int deleteAnnouncement(Announcement announcement);

    @Query("DELETE FROM announcements")
    int deleteAllAnnouncements();

    @Query("SELECT COUNT(*) FROM announcements")
    int getAnnouncementCount();
}
