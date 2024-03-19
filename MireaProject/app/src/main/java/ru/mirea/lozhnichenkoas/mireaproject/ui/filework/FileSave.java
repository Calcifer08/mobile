package ru.mirea.lozhnichenkoas.mireaproject.ui.filework;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;

public class FileSave {

    public static void saveFile(Activity activity, String fileName, String data) {
        if (fileName.isEmpty()) {
            Toast.makeText(activity, "Введите имя файла", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            FileOutputStream fileOutputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
            Toast.makeText(activity, "Файл успешно сохранен", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show();
        }
    }
}
