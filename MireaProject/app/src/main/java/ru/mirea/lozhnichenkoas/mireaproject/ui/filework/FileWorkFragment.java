package ru.mirea.lozhnichenkoas.mireaproject.ui.filework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.mirea.lozhnichenkoas.mireaproject.R;
import ru.mirea.lozhnichenkoas.mireaproject.databinding.FragmentFileWorkBinding;

public class FileWorkFragment extends Fragment {

    private byte[] keyBytes = {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
    };
    private String fileName;
    private TextView editDataFile;
    private Spinner spinner;
    private FragmentFileWorkBinding fragmentFileWorkBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentFileWorkBinding = FragmentFileWorkBinding.inflate(inflater, container, false);
        View root = fragmentFileWorkBinding.getRoot();

        spinner = fragmentFileWorkBinding.spinner;
        editDataFile = fragmentFileWorkBinding.editDataFile;

        getFiles(); // получаем все файлы

        //чтобы при нажатии обновлялся список файлов (можно и убрать)
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    getFiles(); // Вызываем метод getFiles() при каждом нажатии на элемент Spinner
                }
                return false;
            }
        });

        fragmentFileWorkBinding.buttonReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = (String) spinner.getSelectedItem();
                if (fileName != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            editDataFile.post(new Runnable() {
                                @Override
                                public void run() {
                                    editDataFile.setText(FileRead.getTextFromFile(requireContext(), fileName));
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        fragmentFileWorkBinding.buttonWriteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = (String) spinner.getSelectedItem();
                fileName = fileName.substring(0, fileName.length() - 4); // обрезаем .txt
                String data = editDataFile.getText().toString();
                if (!data.isEmpty()){
                    FileSave.saveFile(requireActivity(),
                            fileName,
                            data);
                } else {
                    Toast.makeText(requireContext(), "Введите текст", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // шифруем Encrypt
        fragmentFileWorkBinding.buttonEncryptFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = (String) spinner.getSelectedItem();
                String data = FileRead.getTextFromFile(requireContext(), fileName);
                SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
                CryprFile.encryptFile(requireActivity(),fileName, data, secretKey);
            }
        });

        // дешифруем Decrypt
        fragmentFileWorkBinding.buttonDecryptFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
                editDataFile.setText(CryprFile.decryptFile(requireActivity(), fileName, secretKey));
            }
        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        editDataFile.setText(""); // Очищаем текст в EditText при возобновлении фрагмента
    }

    private void getFiles() {
        // Получаем директорию, в которой хранятся файлы
        File directory = requireActivity().getFilesDir();

        // Получаем список файлов в директории
        File[] files = directory.listFiles();

        // Создаем список имен файлов для отображения в выпадающем списке
        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            // ищем только .txt
            for (File file : files)
                if (file.isFile() && file.getName().toLowerCase().endsWith(".txt"))
                    fileNames.add(file.getName());
        }

        // Создаем адаптер для выпадающего списка
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, fileNames);

        // Устанавливаем адаптер для выпадающего списка
        spinner.setAdapter(adapter);
    }
}