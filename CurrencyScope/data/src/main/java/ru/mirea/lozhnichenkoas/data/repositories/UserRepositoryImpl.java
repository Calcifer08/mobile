package ru.mirea.lozhnichenkoas.data.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.lozhnichenkoas.data.sources.EncryptedSharedPreferencesManager;
import ru.mirea.lozhnichenkoas.domain.repositories.UserRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.AuthCallback;

public class UserRepositoryImpl implements UserRepository {
    private FirebaseAuth auth;
    private EncryptedSharedPreferencesManager preferencesManager;

    public UserRepositoryImpl(Context context) {
        this.auth = FirebaseAuth.getInstance();
        this.preferencesManager = new EncryptedSharedPreferencesManager(context);
    }

    @Override
    public void registerUser(String email, String password, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        preferencesManager.saveUser(email, password);
                        callback.onSuccess(email);
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void signInUser(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        preferencesManager.saveUser(email, password);
                        callback.onSuccess(email);
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void signOutUser() {
        auth.signOut();
        preferencesManager.clearUser();
    }

    @Override
    public void autoSignIn(AuthCallback callback) {
        String email = preferencesManager.getEmail();
        String password = preferencesManager.getPassword();

        if (email != null && password != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            // вернём null, если время истечёт
            Runnable timeoutRunnable = () -> callback.onFailure("Время ожидания вышло");
            handler.postDelayed(timeoutRunnable, 3000);

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        // иначе вызовится после 3 сек
                        handler.removeCallbacks(timeoutRunnable);
                        if (task.isSuccessful()) {
                            callback.onSuccess(email);
                        } else {
                            callback.onFailure(task.getException().getMessage());
                        }
                    });
        }
        callback.onFailure("Нет данных в хранилище");
    }
}
