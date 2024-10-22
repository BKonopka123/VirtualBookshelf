package com.example.virtualbookshelf.viewmodel;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.ml.FoundObject;
import com.example.virtualbookshelf.model.ml.TesseractManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MainProcessImageViewModel is a ViewModel responsible for managing the main process of image processing.
 */
public class MainProcessImageViewModel extends MainViewModel {

    /**
     * LiveData to observe navigation state changes to the main found books screen.
     */
    protected final MutableLiveData<Boolean> navigateToMainFoundBooks = new MutableLiveData<>();

    /**
     * Constructor for MainViewModel.
     * Initializes the ViewModel by calling the constructor of BaseViewModel.
     *
     * @param application The application context.
     */
    public MainProcessImageViewModel(Application application) {
        super(application);
    }

    /**
     * Called when the main found books button is clicked.
     */
    public void onMainFoundBooksButtonClicked() { navigateToMainFoundBooks.setValue(true); }

    /**
     * Provides access to LiveData for observing navigation state changes to the main found books screen.
     *
     * @return LiveData to observe navigation state changes.
     */
    public LiveData<Boolean> getNavigateToMainFoundBooks() { return navigateToMainFoundBooks; }

    /**
     * Resets the LiveData value for navigation to the main found books screen.
     */
    public void resetNavigationMainFoundBooks() { navigateToMainFoundBooks.setValue(false); }

    /**
     * Process image. Rotate and resize image.
     * @param bitmap Bitmap of image.
     * @param uri Uri of image.
     * @return Processed image.
     */
    public Bitmap processImage(Bitmap bitmap, Uri uri) {
        if(bitmap == null || uri == null)
            return null;
        bitmap = rotateUri(uri, bitmap);
        bitmap = BlobManager.resizeImage(bitmap, 1080, 1920);

        return bitmap;
    }

    /**
     * Rotate image.
     * @param imageUri Uri of image.
     * @param bitmap Bitmap of image.
     * @return Processed image.
     */
    private Bitmap rotateUri(Uri imageUri, Bitmap bitmap){
        try{
            byte[] imageBytes = BlobManager.getByteFromBitmap(bitmap);
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
                bitmap = BlobManager.getBitmapFromBlob(imageBytes);
            }
        }catch(Exception e){
            Log.e("MainProcessImageViewModel", "Could not rotate the image - " + e.getMessage(), e);
            return null;
        }
        return bitmap;
    }

    /**
     * Find books in image.
     * @param bitmap Bitmap of image.
     * @param dataPath Path to data.
     * @param assetManager Asset manager.
     */
    public ArrayList<FoundObject> findBooks(Bitmap bitmap, String dataPath, AssetManager assetManager) {
        TesseractManager tesseractManager = new TesseractManager(bitmap, dataPath, assetManager);
        ArrayList<FoundObject> foundBooks = tesseractManager.findBooks();
        return foundBooks;
    }
}
