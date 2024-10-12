package com.example.virtualbookshelf.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.viewmodel.MainViewModel;

import java.io.File;
import java.io.IOException;

/**
 * The main activity of the application, representing the primary screen users interact with.
 * This activity provides navigation to other parts of the app, including the bookshelf, user profile,
 * and camera features. It uses a ViewModel to manage UI-related data and handle user interactions.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The view model for managing UI-related data and user interactions.
     */
    private MainViewModel mainViewModel;

    /**
     * The URI for the image captured by the camera.
     */
    private Uri imageUri;

    /**
     * The launcher for launching the camera activity.
     */
    private ActivityResultLauncher<Intent> cameraLauncher;

    /**
     * Called when the activity is created. Initializes the activity's layout, sets up the ViewModel,
     * handles the window insets, and defines click listeners for navigation buttons.
     *
     * @param savedInstanceState If the activity is being re-created from a previous state, this bundle contains the saved state.
     */
    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initializing the ViewModel to manage UI-related data and logic.
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        DBManager dbManager = mainViewModel.getDbManager();


        // Adjusts the padding of the main view to account for system bars (like the status bar).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //-------------------------------------------------Navigation to Main
        // Setting click listeners for navigation buttons in the main view.
        findViewById(R.id.mainButton_main).setOnClickListener(v -> mainViewModel.onMainButtonClicked());
        findViewById(R.id.cameraButton_main).setOnClickListener(v -> mainViewModel.onMainButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainViewModel.getNavigateToMain().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                mainViewModel.resetNavigationMain();
            }
        });

        //-------------------------------------------------Navigation to Books
        // Setting click listener for the bookshelf button in the main view.
        findViewById(R.id.bookshelfButton_main).setOnClickListener(v -> mainViewModel.onBookshelfButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainViewModel.getNavigateToBookshelf().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(MainActivity.this, BookshelfActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                mainViewModel.resetNavigationBookshelf();
            }
        });

        //-------------------------------------------------Navigation to User
        // Setting click listener for the user button in the main view.
        findViewById(R.id.userButton_main).setOnClickListener(v -> mainViewModel.onUserButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainViewModel.getNavigateToUser().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                mainViewModel.resetNavigationUser();
            }
        });

//        //-------------------------------------------------Navigation to Photo history
//        // Setting click listener for the history button in the main view.
//        findViewById(R.id.history_button_main).setOnClickListener(v -> mainViewModel.onMainPhotoHistoryButtonClicked());
//
//        // Observing the navigation LiveData in the ViewModel to handle navigation events.
//        mainViewModel.getNavigateToMainPhotoHistory().observe(this, navigate -> {
//            if (navigate) {
//                Intent intent = new Intent(MainActivity.this, MainPhotoHistoryActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                mainViewModel.resetNavigationMainPhotoHistory();
//            }
//        });

        //-------------------------------------------------Camera
        // Setting click listener for the history button in the main view.
        findViewById(R.id.takePhotoButton_main).setOnClickListener(v -> mainViewModel.onMainProcessImageButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainViewModel.getNavigateToMainProcessImage().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    imageUri = createFileImageUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    cameraLauncher.launch(intent);
                }

                mainViewModel.resetNavigationMainProcessImage();
            }
        });

        //Camera launcher, which after capturing image launch MainProcessImageActivity and send imageUri
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = new Intent(MainActivity.this, MainProcessImageActivity.class);
                        if (imageUri != null) {
                            intent.putExtra("imageUri", imageUri.toString());
                        }
                        else {
                            intent.putExtra("imageUri", "null");
                        }
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );
    }

    /**
     * Creates a temporary file URI for storing the captured image.
     * @return The URI for the created file.
     */
    private Uri createFileImageUri() {
        File imageFile = null;
        try {
            String fileName = "image";
            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
        } catch (IOException e) {
            Log.e("MainActivity", "Could not save image to file - " +  e.getMessage(), e);
        }

        if (imageFile != null){
            return FileProvider.getUriForFile(this, "com.example.virtualbookshelf.fileprovider", imageFile);
        } else {
            return null;
        }
    }
}