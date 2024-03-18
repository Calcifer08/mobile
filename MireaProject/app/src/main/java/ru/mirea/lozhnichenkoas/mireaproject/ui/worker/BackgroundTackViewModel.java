package ru.mirea.lozhnichenkoas.mireaproject.ui.worker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BackgroundTackViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public BackgroundTackViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is BackgroundTack fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
