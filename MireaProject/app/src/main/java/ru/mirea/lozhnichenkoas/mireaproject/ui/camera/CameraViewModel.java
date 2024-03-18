package ru.mirea.lozhnichenkoas.mireaproject.ui.camera;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

public class CameraViewModel extends ViewModel {
    private Uri imageUri;

    public void setImageUri(Uri uri) {
        this.imageUri = uri;
    }

    public Uri getImageUri() {
        return imageUri;
    }}
