package com.example.virtualbookshelf.viewmodel;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.User;

import java.io.IOException;
import java.io.InputStream;

/**
 * UserViewModel is a ViewModel responsible for managing user-related data.
 * It retrieves user data from the database and updates the user profile image.
 */
public class UserViewModel extends BaseViewModel {

    /** LiveData object that holds the current user data. */
    private final MutableLiveData<User> userLiveData;

    /**
     * Constructor for UserViewModel.
     * Initializes userLiveData and loads the user data when the ViewModel is created.
     *
     * @param application The application context.
     */
    public UserViewModel(Application application) {
        super(application);
        this.userLiveData = new MutableLiveData<>();
        loadUserData();
    }

    /**
     * Loads the user data from the database by querying for a user with ID 1.
     * It sets the user data into the userLiveData.
     */
    private void loadUserData() {
        Cursor cursor_user = dbManager.getUserById(1);

        try {
            User user = null;
            if (cursor_user.moveToFirst()) {
                user = new User(cursor_user.getInt(0), cursor_user.getString(1), cursor_user.getBlob(2), cursor_user.getInt(3), cursor_user.getInt(4), cursor_user.getInt(5), cursor_user.getInt(6), cursor_user.getInt(7));
            }
            cursor_user.close();
            userLiveData.setValue(user);
        } catch (Exception e) {
            Log.e("UserViewModel", e.getMessage(), e);
        }
    }

    /**
     * Gets the LiveData object that holds the current user data.
     * This LiveData can be observed by the UI to get updates on the user data.
     *
     * @return A LiveData containing the current user data.
     */
    public MutableLiveData<User> getUser() {
        return userLiveData;
    }

    /**
     * Refreshes the user data by reloading it from the database.
     * It calls loadUserData() again to fetch the latest user information.
     */
    public void refreshUserData() {
        loadUserData();
    }

    /**
     * Checks if the user profile image (from the provided URI) is valid.
     * It checks the MIME type and size of the image to ensure it is a valid image and within the size limit.
     *
     * @param imageUri The URI of the image to be checked.
     * @return 0 if the image is valid, 1 if the file is not an image, 2 if the image size exceeds the limit.
     * @throws IOException If an error occurs while querying the image file.
     */
    public Integer checkUserProfileImage(Uri imageUri) throws IOException {
        try {
            ContentResolver contentResolver = getApplication().getContentResolver();
            String mimeType = contentResolver.getType(imageUri);
            String[] projection = {MediaStore.Images.Media.SIZE};
            try (Cursor cursor = contentResolver.query(imageUri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
                    long size = cursor.getLong(sizeIndex);
                    if (size > 4 * 1024 * 1024) {
                        return 2;
                    }
                }
            }
            if (mimeType != null && !mimeType.startsWith("image/")) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            Log.e("UserViewModel", e.getMessage(), e);
            return 1;
        }
    }

    /**
     * Updates the user's profile image in the database.
     * It retrieves the image from the provided URI, processes it (including rotation based on EXIF data),
     * and updates the user's record in the database.
     *
     * @param inputStream The input stream from which the image will be read.
     * @param imageUri The URI of the image to be processed and updated.
     * @throws IOException If an error occurs while reading or processing the image.
     */
    public void updateUserProfileImage(InputStream inputStream, Uri imageUri) throws IOException {
        try {
            byte[] imageBytes = BlobManager.getByteFromUri(inputStream);
            inputStream.close();

            InputStream inputStreamExif = getApplication().getContentResolver().openInputStream(imageUri);
            if (inputStreamExif != null) {
                ExifInterface exif = new ExifInterface(inputStreamExif);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        imageBytes = BlobManager.rotateImage(imageBytes, 90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        imageBytes = BlobManager.rotateImage(imageBytes, 180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        imageBytes = BlobManager.rotateImage(imageBytes, 270);
                        break;
                }
                inputStreamExif.close();
            }

            Cursor cursor_user = dbManager.getUserById(1);
            if(cursor_user.moveToFirst()){
                User user = new User(cursor_user.getInt(0), cursor_user.getString(1), imageBytes, cursor_user.getInt(3), cursor_user.getInt(4), cursor_user.getInt(5), cursor_user.getInt(6), cursor_user.getInt(7));
                dbManager.updateUser(user);
            }
            cursor_user.close();
        } catch (Exception e) {
            Log.e("UserViewModel", e.getMessage(), e);
        }
    }
}