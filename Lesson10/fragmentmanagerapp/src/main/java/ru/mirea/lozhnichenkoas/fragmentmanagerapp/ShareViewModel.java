package ru.mirea.lozhnichenkoas.fragmentmanagerapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {
    private MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    public LiveData<String> getMutableLiveData() {
        return mutableLiveData;
    }

    public void setMutableLiveData(String text) {
        mutableLiveData.setValue(text);
    }
}
