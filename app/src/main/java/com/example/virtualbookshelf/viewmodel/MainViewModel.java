package com.example.virtualbookshelf.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Boolean> navigateToMain = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToBookshelf = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToUser = new MutableLiveData<>();

    public void onMainButtonClicked() {
        navigateToMain.setValue(true);
    }
    public void onBookshelfButtonClicked() {
        navigateToBookshelf.setValue(true);
    }
    public void onUserButtonClicked() {
        navigateToUser.setValue(true);
    }

    public LiveData<Boolean> getNavigateToMain() {
        return navigateToMain;
    }
    public LiveData<Boolean> getNavigateToBookshelf() {
        return navigateToBookshelf;
    }
    public LiveData<Boolean> getNavigateToUser() {
        return navigateToUser;
    }

    public void resetNavigationMain() { navigateToMain.setValue(false); }
    public void resetNavigationBookshelf() { navigateToBookshelf.setValue(false); }
    public void resetNavigationUser() { navigateToUser.setValue(false); }
}