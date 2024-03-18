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

public class CryprFile {
    public static void encryptFile(Activity activity, String fileName, String data, SecretKey secretKey) {
        if (fileName.isEmpty()) {
            Toast.makeText(activity, "Введите имя файла", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Создаем шифр для шифрования
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Создаем исходящий поток для записи зашифрованных данных в файл
            FileOutputStream fileOutputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

            // Записываем данные и закрываем поток
            cipherOutputStream.write(data.getBytes());
            cipherOutputStream.close();

            Toast.makeText(activity, "Файл успешно зашифрован", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Ошибка при шифровании файла", Toast.LENGTH_SHORT).show();
        }
    }

    public static String decryptFile(Activity activity, String fileName, SecretKey secretKey) {
        try {
            // Создаем шифр для расшифровки
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Создаем входящий поток для чтения зашифрованных данных из файла
            FileInputStream fileInputStream = activity.openFileInput(fileName);
            CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);

            // Читаем данные и конвертируем в строку с использованием кодировки UTF-8
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, bytesRead, "UTF-8"));
            }
            cipherInputStream.close();

            // Записываем расшифрованные данные в файл
            FileOutputStream fileOutputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(stringBuilder.toString().getBytes("UTF-8"));
            fileOutputStream.close();

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Ошибка при расшифровании файла", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

}
