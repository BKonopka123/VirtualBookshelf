package com.example.virtualbookshelf.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import com.example.virtualbookshelf.model.db.DBManager;

/**
 * BaseViewModel is a ViewModel responsible for managing the navigation logic and database operations in the app.
 * It interacts with the database through DBManager and handles navigation state updates via LiveData.
 */
public class BaseViewModel extends AndroidViewModel {

    /** Instance of DBManager to interact with the database. */
    protected DBManager dbManager;

    /** LiveData to observe navigation state changes. */
    protected final MutableLiveData<Boolean> navigateToMain = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> navigateToBookshelf = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> navigateToUser = new MutableLiveData<>();

    /**
     * Constructor for initializing the BaseViewModel.
     * It sets up the database manager and initializes the mock data if necessary.
     *
     * @param application The application context used for database initialization.
     */
    public BaseViewModel(Application application) {
        super(application);
        dbManager = DBManager.getInstance(application.getApplicationContext());

        // Check SharedPreferences to see if data has been mocked
        SharedPreferences sharedPreferences = application.getSharedPreferences("MyAppPreferences", Application.MODE_PRIVATE);
        boolean isDataMocked = sharedPreferences.getBoolean("isDataMocked", false);

        // Mock data if it hasn't been done already
        if (!isDataMocked) {
            dbManager.mockData();  // TODO: DELETE
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isDataMocked", true);
            editor.apply();
        }

    }

    /**
     * Provides access to the DBManager for database operations.
     *
     * @return The DBManager instance for interacting with the database.
     */
    public DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Called when the ViewModel is being cleared. It closes the database connection.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
    }

    /**
     * Called when the main button is clicked. Updates the LiveData to navigate to the main screen.
     */
    public void onMainButtonClicked() {
        navigateToMain.setValue(true);
    }

    /**
     * Called when the bookshelf button is clicked. Updates the LiveData to navigate to the bookshelf screen.
     */
    public void onBookshelfButtonClicked() {
        navigateToBookshelf.setValue(true);
    }

    /**
     * Called when the user button is clicked. Updates the LiveData to navigate to the user screen.
     */
    public void onUserButtonClicked() { navigateToUser.setValue(true );}



    /**
     * Provides access to LiveData for observing navigation state changes.
     *
     * @return LiveData to observe navigation state changes.
     */
    public LiveData<Boolean> getNavigateToMain() {
        return navigateToMain;
    }

    /**
     * Provides access to LiveData for observing navigation state changes.
     *
     * @return LiveData to observe navigation state changes.
     */
    public LiveData<Boolean> getNavigateToBookshelf() {
        return navigateToBookshelf;
    }

    /**
     * Provides access to LiveData for observing navigation state changes.
     *
     * @return LiveData to observe navigation state changes.
     */
    public LiveData<Boolean> getNavigateToUser() {
        return navigateToUser;
    }



    /**
     * Resets the LiveData value for navigation to the main screen.
     */
    public void resetNavigationMain() { navigateToMain.setValue(false); }

    /**
     * Resets the LiveData value for navigation to the bookshelf screen.
     */
    public void resetNavigationBookshelf() { navigateToBookshelf.setValue(false); }

    /**
     * Resets the LiveData value for navigation to the user screen.
     */
    public void resetNavigationUser() { navigateToUser.setValue(false); }
}
