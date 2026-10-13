
package com.example.sod401_team_project.repository;

import android.content.Context;

import com.example.sod401_team_project.data.AppDatabase;
import com.example.sod401_team_project.data.Event;
import com.example.sod401_team_project.data.EventDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventRepository {

    private final EventDao eventDao;

    private final ExecutorService executorService =
            Executors.newSingleThreadExecutor();

    public EventRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        eventDao = database.eventDao();
    }

    public void insertEvent(Event event,
                            OnEventInsertedListener listener) {
        executorService.execute(() -> {
            try {
                long eventId = eventDao.insertEvent(event);

                if (listener != null) {
                    listener.onSuccess(eventId);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void getAllEvents(OnEventsLoadedListener listener) {
        executorService.execute(() -> {
            try {
                List<Event> events = eventDao.getAllEvents();

                if (listener != null) {
                    listener.onSuccess(events);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void getEventById(
            int eventId,
            OnEventLoadedListener listener) {
        executorService.execute(() -> {
            try {
                Event event = eventDao.getEventById(eventId);

                if (listener != null) {
                    listener.onSuccess(event);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void searchEvents(
            String keyword,
            OnEventsLoadedListener listener) {
        executorService.execute(() -> {
            try {
                List<Event> events = eventDao.searchEvents(keyword);

                if (listener != null) {
                    listener.onSuccess(events);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void updateEvent(Event event,
                            OnEventUpdatedListener listener) {
        executorService.execute(() -> {
            try {
                int rowsUpdated = eventDao.updateEvent(event);

                if (listener != null) {
                    listener.onSuccess(rowsUpdated);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void deleteEvent(Event event,
                            OnEventDeletedListener listener) {
        executorService.execute(() -> {
            try {
                int rowsDeleted = eventDao.deleteEvent(event);

                if (listener != null) {
                    listener.onSuccess(rowsDeleted);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public interface OnEventInsertedListener {
        void onSuccess(long eventId);
        void onError(String message);
    }

    public interface OnEventsLoadedListener {
        void onSuccess(List<Event> events);
        void onError(String message);
    }

    public interface OnEventLoadedListener {
        void onSuccess(Event event);
        void onError(String message);
    }

    public interface OnEventUpdatedListener {
        void onSuccess(int rowsUpdated);
        void onError(String message);
    }

    public interface OnEventDeletedListener {
        void onSuccess(int rowsDeleted);
        void onError(String message);
    }
}
