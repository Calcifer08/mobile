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
                                    editDataFile.setText(FileRead.readFile(requireContext(), fileName));
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
                SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
                CryptFile.encryptFile(requireActivity(),fileName, secretKey);
            }
        });

        // дешифруем Decrypt
        fragmentFileWorkBinding.buttonDecryptFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
                String fileName = (String) spinner.getSelectedItem();
                String decryptdata = CryptFile.decryptFile(requireActivity(), fileName, secretKey);
                //если надо - сохраняем
                FileSave.saveFile(requireActivity(), fileName, decryptdata);
                editDataFile.setText(decryptdata);
            }
        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        editDataFile.setText(""); // очищаем текст при остановке фрагмента
    }

    private void getFiles() {
        // получаем директорию
        File directory = requireActivity().getFilesDir();

        // получаем список файлов в директории
        File[] files = directory.listFiles();

        // создаем список имен файлов для отображения в выпадающем списке
        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            // ищем только .txt
            for (File file : files)
                if (file.isFile() && file.getName().toLowerCase().endsWith(".txt"))
                    fileNames.add(file.getName());
        }

        // создаем адаптер для выпадающего списка
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, fileNames);

        // устанавливаем адаптер для выпадающего списка
        spinner.setAdapter(adapter);
    }
}