package ru.mirea.lozhnichenkoas.data.sources;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EncryptedSharedPreferencesManager {
    private static String FILENAME = "userData";
    private SharedPreferences sharedPreferences;

    public EncryptedSharedPreferencesManager(Context context){
        try {
            String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    FILENAME,
                    masterKey,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e){
            throw new RuntimeException("Ошибка при создании защищённого файла настроек: ", e);
        }
    }

    public void saveUser(String email, String password){
        sharedPreferences.edit()
                .putString("EMAIL", email)
                .putString("PASSWORD", password)
                .apply();
    }

    public String getEmail(){
        return sharedPreferences.getString("EMAIL", null);
    }

    public String getPassword(){
        return sharedPreferences.getString("PASSWORD", null);
    }

    public void clearUser(){
        sharedPreferences.edit().clear().apply();
    }
}
