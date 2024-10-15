package ru.mirea.lozhnichenkoas.cryptoloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.mirea.lozhnichenkoas.cryptoloader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public final String TAG = this.getClass().getSimpleName();
    private final int LoaderID = 1234;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onCLickButton(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("EXP", "mirea");
        LoaderManager.getInstance(this).initLoader(LoaderID, bundle, this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d(TAG, "onLoaderReset");
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle bundle) {
        if (id == LoaderID) {
            Toast.makeText(this, "onCreateLoader" + id, Toast.LENGTH_SHORT).show();
            return new MyLoader(this, bundle);
        }
        throw new InvalidParameterException("Invalid parametr id");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if (loader.getId() == LoaderID) {
            Log.d(TAG, "onLoadFinished" + s);
            Toast.makeText(this, "onLoadFinished" + s, Toast.LENGTH_SHORT).show();
        }
    }

    public static SecretKey generateKey() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any	data	used	as	random	seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            return new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptMsg(String message, SecretKey secret) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public void onClickEncrypt (View view) {
        String message = binding.editTextText.getText().toString();

        // Генерация ключа
        SecretKey secretKey = generateKey();

        // Шифрование сообщения
        byte[] encryptedMessage = encryptMsg(message, secretKey);

        //	Отправка данных в Loader
        Bundle	bundle	=	new	Bundle();
        bundle.putByteArray(MyLoader.ARG_WORD,	encryptedMessage);
        bundle.putByteArray("key",	secretKey.getEncoded());
        bundle.putString("EXP", message);
        LoaderManager.getInstance(this).initLoader(LoaderID,	bundle,	this);


        String s = MyLoader.decryptMsg(encryptedMessage, secretKey);
        Toast.makeText(this, "Расшифрованная фраза: " + s, Toast.LENGTH_SHORT).show();
    }
}