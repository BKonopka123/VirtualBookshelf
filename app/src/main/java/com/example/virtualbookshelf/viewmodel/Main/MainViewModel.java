package com.example.virtualbookshelf.viewmodel.Main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.virtualbookshelf.viewmodel.BaseViewModel;

/**
 * MainViewModel is a ViewModel responsible for managing data and actions in the main activity.
 * It inherits from BaseViewModel and doesn't currently implement any additional logic.
 */
public class MainViewModel extends BaseViewModel {

//    /** LiveData to observe navigation state changes. */
//    protected final MutableLiveData<Boolean> navigateToMainPhotoHistory = new MutableLiveData<>();

    /** LiveData to observe navigation state changes. */
    protected final MutableLiveData<Boolean> navigateToMainProcessImage = new MutableLiveData<>();

    /**
     * Constructor for MainViewModel.
     * Initializes the ViewModel by calling the constructor of BaseViewModel.
     *
     * @param application The application context.
     */
    public MainViewModel(Application application) {
        super(application);
    }

//    /**
//     * Called when the main photo history button is clicked. Updates the LiveData to navigate to the Photo history screen.
//     */
//    public void onMainPhotoHistoryButtonClicked() { navigateToMainPhotoHistory.setValue(true); }

    /**
     * Called when the main process image button is clicked. Updates the LiveData to navigate to the Process image screen.
     */
    public void onMainProcessImageButtonClicked() { navigateToMainProcessImage.setValue(true); }

//    /**
//     * Provides access to LiveData for observing navigation state changes.
//     *
//     * @return LiveData to observe navigation state changes.
//     */
//    public LiveData<Boolean> getNavigateToMainPhotoHistory() { return navigateToMainPhotoHistory; }

    /**
     * Provides access to LiveData for observing navigation state changes.
     *
     * @return LiveData to observe navigation state changes.
     */
    public LiveData<Boolean> getNavigateToMainProcessImage() { return navigateToMainProcessImage; }

//    /**
//     * Resets the LiveData value for navigation to the Photo history screen.
//     */
//    public void resetNavigationMainPhotoHistory() { navigateToMainPhotoHistory.setValue(false); }

    /**
     * Resets the LiveData value for navigation to the Process image screen.
     */
    public void resetNavigationMainProcessImage() { navigateToMainProcessImage.setValue(false); }

}