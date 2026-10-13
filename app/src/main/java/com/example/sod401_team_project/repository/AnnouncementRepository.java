
package com.example.sod401_team_project.repository;

import android.content.Context;

import com.example.sod401_team_project.data.Announcement;
import com.example.sod401_team_project.data.AnnouncementDao;
import com.example.sod401_team_project.data.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnnouncementRepository {

    private final AnnouncementDao announcementDao;

    private final ExecutorService executorService =
            Executors.newSingleThreadExecutor();

    public AnnouncementRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        announcementDao = database.announcementDao();
    }

    public void insertAnnouncement(
            Announcement announcement,
            OnAnnouncementInsertedListener listener) {

        executorService.execute(() -> {
            try {
                long announcementId =
                        announcementDao.insertAnnouncement(announcement);

                if (listener != null) {
                    listener.onSuccess(announcementId);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void getAllAnnouncements(
            OnAnnouncementsLoadedListener listener) {

        executorService.execute(() -> {
            try {
                List<Announcement> announcements =
                        announcementDao.getAllAnnouncements();

                if (listener != null) {
                    listener.onSuccess(announcements);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void getAnnouncementById(
            int announcementId,
            OnAnnouncementLoadedListener listener) {

        executorService.execute(() -> {
            try {
                Announcement announcement =
                        announcementDao.getAnnouncementById(announcementId);

                if (listener != null) {
                    listener.onSuccess(announcement);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void searchAnnouncements(
            String keyword,
            OnAnnouncementsLoadedListener listener) {

        executorService.execute(() -> {
            try {
                List<Announcement> announcements =
                        announcementDao.searchAnnouncements(keyword);

                if (listener != null) {
                    listener.onSuccess(announcements);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void updateAnnouncement(
            Announcement announcement,
            OnAnnouncementUpdatedListener listener) {

        executorService.execute(() -> {
            try {
                int rowsUpdated =
                        announcementDao.updateAnnouncement(announcement);

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

    public void deleteAnnouncement(
            Announcement announcement,
            OnAnnouncementDeletedListener listener) {

        executorService.execute(() -> {
            try {
                int rowsDeleted =
                        announcementDao.deleteAnnouncement(announcement);

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

    public interface OnAnnouncementInsertedListener {
        void onSuccess(long announcementId);
        void onError(String message);
    }

    public interface OnAnnouncementsLoadedListener {
        void onSuccess(List<Announcement> announcements);
        void onError(String message);
    }

    public interface OnAnnouncementLoadedListener {
        void onSuccess(Announcement announcement);
        void onError(String message);
    }

    public interface OnAnnouncementUpdatedListener {
        void onSuccess(int rowsUpdated);
        void onError(String message);
    }

    public interface OnAnnouncementDeletedListener {
        void onSuccess(int rowsDeleted);
        void onError(String message);
    }
}
