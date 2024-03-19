package ru.mirea.lozhnichenkoas.mireaproject.ui.filework;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;

public class CryptFile {
    public static void encryptFile(Activity activity, String fileName, SecretKey secretKey) {
        if (fileName.isEmpty()) {
            Toast.makeText(activity, "Введите имя файла", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String data = FileRead.readFile(activity, fileName);

            // создаем шифр для шифрования
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // создаем поток для записи данных
            FileOutputStream fileOutputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

            // записываем данные
            cipherOutputStream.write(data.getBytes("UTF-8"));
            cipherOutputStream.close();

            Toast.makeText(activity, "Файл успешно зашифрован", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Ошибка при шифровании файла", Toast.LENGTH_SHORT).show();
        }
    }

    public static String decryptFile(Activity activity, String fileName, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // поток для чтения данных из файла
            FileInputStream fileInputStream = activity.openFileInput(fileName);
            CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);

            // читаем и конвертируем в строку
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, bytesRead, "UTF-8"));
            }
            cipherInputStream.close();

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Ошибка при расшифровании файла", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

}
