package ru.mirea.lozhnichenkoas.mireaproject.ui.filework;

import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

public class FileRead {
    public static String readFile(Context context, String fileName) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(fileName);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            String text = new String(bytes);
            return text;
        } catch (IOException ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
