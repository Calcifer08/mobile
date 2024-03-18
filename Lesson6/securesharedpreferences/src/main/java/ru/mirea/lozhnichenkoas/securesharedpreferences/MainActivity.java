package ru.mirea.lozhnichenkoas.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.widget.TextView;

import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.lozhnichenkoas.securesharedpreferences.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView textName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textName = binding.TextViewName;

        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        try {
            String mainKey = MasterKeys.getOrCreate(keyGenParameterSpec);
            SharedPreferences securePreferences = EncryptedSharedPreferences.create(
                    "secret_shared_pref",
                    mainKey,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            securePreferences.edit().putString("NAME", "Есенин С. А.").apply();
            textName.setText(securePreferences.getString("NAME", ""));
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}