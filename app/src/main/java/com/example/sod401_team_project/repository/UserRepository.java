
package com.example.sod401_team_project.repository;

import android.content.Context;

import com.example.sod401_team_project.data.AppDatabase;
import com.example.sod401_team_project.data.User;
import com.example.sod401_team_project.data.UserDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private final UserDao userDao;

    private final ExecutorService executorService =
            Executors.newSingleThreadExecutor();

    public UserRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        userDao = database.userDao();
    }

    public void insertUser(User user,
                           OnUserInsertedListener listener) {
        executorService.execute(() -> {
            try {
                long userId = userDao.insertUser(user);

                if (listener != null) {
                    listener.onSuccess(userId);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void getAllUsers(OnUsersLoadedListener listener) {
        executorService.execute(() -> {
            try {
                List<User> users = userDao.getAllUsers();

                if (listener != null) {
                    listener.onSuccess(users);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void getUserByEmail(
            String email,
            OnUserLoadedListener listener) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserByEmail(email);

                if (listener != null) {
                    listener.onSuccess(user);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void updateUser(User user,
                           OnUserUpdatedListener listener) {
        executorService.execute(() -> {
            try {
                int rowsUpdated = userDao.updateUser(user);

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

    public void deleteUser(User user,
                           OnUserDeletedListener listener) {
        executorService.execute(() -> {
            try {
                int rowsDeleted = userDao.deleteUser(user);

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

    public interface OnUserInsertedListener {
        void onSuccess(long userId);
        void onError(String message);
    }

    public interface OnUsersLoadedListener {
        void onSuccess(List<User> users);
        void onError(String message);
    }

    public interface OnUserLoadedListener {
        void onSuccess(User user);
        void onError(String message);
    }

    public interface OnUserUpdatedListener {
        void onSuccess(int rowsUpdated);
        void onError(String message);
    }

    public interface OnUserDeletedListener {
        void onSuccess(int rowsDeleted);
        void onError(String message);
    }
}
