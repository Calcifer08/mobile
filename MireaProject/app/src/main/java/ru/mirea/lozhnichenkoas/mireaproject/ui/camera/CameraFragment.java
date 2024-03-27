package ru.mirea.lozhnichenkoas.mireaproject.ui.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import ru.mirea.lozhnichenkoas.mireaproject.databinding.FragmentCameraBinding;

public class CameraFragment extends Fragment {

    private boolean isWork = false;
    private Uri imageUri;
    private FragmentCameraBinding fragmentCameraBinding;
    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
        isWork = permissions.containsValue(true);
    });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = fragmentCameraBinding.getRoot();

        int	cameraPermissionStatus = ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.CAMERA);
        int	storagePermissionStatus = ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if	(cameraPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus
                ==	PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            requestPermissionLauncher.launch(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE});
        }

        //	Создание функции обработки результата от системного приложения «камера»
        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    fragmentCameraBinding.imageViewCamera.setImageURI(imageUri);
                }
            }
        };

        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback);

        //	Обработчик нажатия на компонент «imageView»
        fragmentCameraBinding.imageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //	проверка на наличие разрешений для камеры
                if (isWork) {
                    try {
                        File photoFile = createImageFile();
                        //	генерирование пути к файлу на основе authorities
                        String authorities = requireContext().getPackageName() + ".mireaprovider";
                        imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        cameraActivityResultLauncher.launch(cameraIntent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(requireContext(), "Нет разрешений", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";

        File storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }
}