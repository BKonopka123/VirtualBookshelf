package com.example.virtualbookshelf.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import com.example.virtualbookshelf.model.db.DBManager;

public class BaseViewModel extends AndroidViewModel {

    protected DBManager dbManager;

    protected final MutableLiveData<Boolean> navigateToMain = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> navigateToBookshelf = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> navigateToUser = new MutableLiveData<>();

    public BaseViewModel(Application application) {
        super(application);
        dbManager = new DBManager(application);
        dbManager.open();
        SharedPreferences sharedPreferences = application.getSharedPreferences("MyAppPreferences", Application.MODE_PRIVATE);
        boolean isDataMocked = sharedPreferences.getBoolean("isDataMocked", false);

        if (!isDataMocked) {
            dbManager.MockData();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isDataMocked", true);
            editor.apply();
        }

    }

    public DBManager getDbManager() {
        return dbManager;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        dbManager.close();
    }

    public void onMainButtonClicked() {
        navigateToMain.setValue(true);
    }
    public void onBookshelfButtonClicked() {
        navigateToBookshelf.setValue(true);
    }
    public void onUserButtonClicked() { navigateToUser.setValue(true );
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
