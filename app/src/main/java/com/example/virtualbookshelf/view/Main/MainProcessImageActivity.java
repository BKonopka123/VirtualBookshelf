package com.example.virtualbookshelf.view.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.model.ml.FoundObject;
import com.example.virtualbookshelf.view.Book.BookshelfActivity;
import com.example.virtualbookshelf.view.MainActivity;
import com.example.virtualbookshelf.view.User.UserActivity;
import com.example.virtualbookshelf.viewmodel.Main.MainProcessImageViewModel;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Activity for showing taken image and processing it.
 */
public class MainProcessImageActivity extends AppCompatActivity {

    /**
     * The view model for managing UI-related data and user interactions.
     */
    private MainProcessImageViewModel mainProcessImageViewModel;

    /**
     * Image view for displaying found image.
     */
    private ImageView foundImageView;

    /**
     * Text view for displaying message if no photo found.
     */
    private TextView noPhotoFoundTextView;

    /**
     * Button for processing image.
     */
    private AppCompatButton processImageButton;

    /**
     * Called when the activity is created. Initializes the activity's layout, sets up the ViewModel,
     * @param savedInstanceState If the activity is being re-created from a previous state, this bundle contains the saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_process_image);

        // Initializing the ViewModel to manage UI-related data and logic.
        mainProcessImageViewModel = new ViewModelProvider(this).get(MainProcessImageViewModel.class);
        DBManager dbManager = mainProcessImageViewModel.getDbManager();
        String filePath = getIntent().getStringExtra("imageUri");
        Bitmap processedImage = showImage(filePath);

        // Adjusts the padding of the main view to account for system bars (like the status bar).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_photo_history), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //-------------------------------------------------Navigation to Main
        // Setting click listeners for navigation buttons in the main view.
        findViewById(R.id.mainButton_main_process_image).setOnClickListener(v -> mainProcessImageViewModel.onMainButtonClicked());
        findViewById(R.id.cameraButton_main_process_image).setOnClickListener(v -> mainProcessImageViewModel.onMainButtonClicked());
        findViewById(R.id.backButton_main_process_image).setOnClickListener(v -> mainProcessImageViewModel.onMainButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainProcessImageViewModel.getNavigateToMain().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(MainProcessImageActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                mainProcessImageViewModel.resetNavigationMain();
            }
        });

        //-------------------------------------------------Navigation to Books
        // Setting click listener for the bookshelf button in the main view.
        findViewById(R.id.bookshelfButton_main_process_image).setOnClickListener(v -> mainProcessImageViewModel.onBookshelfButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainProcessImageViewModel.getNavigateToBookshelf().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(MainProcessImageActivity.this, BookshelfActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                mainProcessImageViewModel.resetNavigationBookshelf();
            }
        });

        //-------------------------------------------------Navigation to User
        // Setting click listener for the user button in the main view.
        findViewById(R.id.userButton_main_process_image).setOnClickListener(v -> mainProcessImageViewModel.onUserButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainProcessImageViewModel.getNavigateToUser().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(MainProcessImageActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                mainProcessImageViewModel.resetNavigationUser();
            }
        });

        //-------------------------------------------------Processing images
        // Setting click listener for the process button in the main view.
        findViewById(R.id.process_button_main_process_image).setOnClickListener(v -> mainProcessImageViewModel.onMainFoundBooksButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainProcessImageViewModel.getNavigateToMainFoundBooks().observe(this, navigate -> {
            if (navigate) {
                if(!checkInternetAvailability()) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    mainProcessImageViewModel.resetNavigationMainFoundBooks();
                    return;
                }
                processImageButton.setBackgroundResource(R.drawable.rounded_button_background_invalid);
                processImageButton.setTextColor(Color.parseColor("#0D0D0D"));
                processImageButton.setEnabled(false);
                processImageButton.setClickable(false);
                mainProcessImageViewModel.findBooks(processedImage, getFilesDir() + "/tesseract/", getAssets());
            }
        });

        // Observing the found books LiveData in the ViewModel to handle navigation events.
        mainProcessImageViewModel.getFoundBooksLiveData().observe(this, new Observer<ArrayList<FoundObject>>() {
            @Override
            public void onChanged(ArrayList<FoundObject> foundBooks) {
                mainProcessImageViewModel.deleteDuplicates(foundBooks);
                Intent intent = new Intent(MainProcessImageActivity.this, MainFoundBooksActivity.class);
                intent.putExtra("foundBooks", foundBooks);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                mainProcessImageViewModel.resetNavigationMainFoundBooks();
            }
        });

        // Observing the show toast event LiveData in the ViewModel to handle navigation events.
        mainProcessImageViewModel.getShowToastEvent().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Deletes the file from the given URI.
     * @param uri The URI of the file to be deleted.
     */
    private void deleteFileFromUri(Uri uri) {
        try {
            getContentResolver().delete(uri, null, null);
            Log.d("MainProcessImageActivity", "File deleted");
        } catch (Exception e){
            Log.e("MainProcessImageActivity", "Error deleting file - " + e.getMessage(), e);
        }
    }

    /**
     * Shows image/text with information that no photo found.
     * @param filePath Path to image.
     * @return Processed image.
     */
    private Bitmap showImage(String filePath){
        Bitmap image = null;
        Uri imageUri = null;
        if(filePath != null && !filePath.equals("null")){
            imageUri = Uri.parse(filePath);
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                Log.e("MainProcessImageActivity", "Error getting image - " + e.getMessage(), e);
            }
        }

        Bitmap processedImage = mainProcessImageViewModel.processImage(image, imageUri);
        deleteFileFromUri(imageUri);

        foundImageView = findViewById(R.id.image_main_process_image);
        noPhotoFoundTextView = findViewById(R.id.text_noPhoto_main_process_image);
        processImageButton = findViewById(R.id.process_button_main_process_image);

        if(processedImage != null){
            foundImageView.setImageBitmap(processedImage);
            noPhotoFoundTextView.setVisibility(View.GONE);
            foundImageView.setVisibility(View.VISIBLE);
        } else {
            noPhotoFoundTextView.setVisibility(View.VISIBLE);
            foundImageView.setVisibility(View.GONE);
            processImageButton.setBackgroundResource(R.drawable.rounded_button_background_invalid);
            processImageButton.setTextColor(Color.parseColor("#0D0D0D"));
            processImageButton.setEnabled(false);
            processImageButton.setClickable(false);
        }
        return processedImage;
    }

    /**
     * Checks if there is internet connection.
     * @return True if there is internet connection, false otherwise.
     */
    private boolean checkInternetAvailability() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            return networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        }
        return false;
    }
}
