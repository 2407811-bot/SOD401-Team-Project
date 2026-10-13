
package com.example.sod401_team_project.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertEvent(Event event);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    List<Long> insertEvents(List<Event> events);

    @Query("SELECT * FROM events ORDER BY date ASC, time ASC")
    List<Event> getAllEvents();

    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    Event getEventById(int eventId);

    @Query("SELECT * FROM events WHERE title LIKE '%' || :keyword || '%' ORDER BY date ASC")
    List<Event> searchEvents(String keyword);

    @Update
    int updateEvent(Event event);

    @Delete
    int deleteEvent(Event event);

    @Query("DELETE FROM events")
    int deleteAllEvents();

    @Query("SELECT COUNT(*) FROM events")
    int getEventCount();
}
