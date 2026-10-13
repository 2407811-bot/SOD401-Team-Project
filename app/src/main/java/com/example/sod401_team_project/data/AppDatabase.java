
package com.example.sod401_team_project.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                User.class,
                Event.class,
                Announcement.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract EventDao eventDao();

    public abstract AnnouncementDao announcementDao();

    public static AppDatabase getDatabase(Context context) {

        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {

                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "campuspulse_database"
                    ).build();
                }
            }
        }

        return INSTANCE;
    }
}
