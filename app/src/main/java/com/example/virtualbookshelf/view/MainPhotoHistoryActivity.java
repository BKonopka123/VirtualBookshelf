//package com.example.virtualbookshelf.view;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.virtualbookshelf.R;
//import com.example.virtualbookshelf.model.db.DBManager;
//import com.example.virtualbookshelf.viewmodel.MainPhotoHistoryViewModel;
//
///**
// * Activity representing the photo history screen of the application.
// */
//public class MainPhotoHistoryActivity extends AppCompatActivity {
//
//    /** The view model for managing UI-related data and user interactions. */
//    private MainPhotoHistoryViewModel mainPhotoHistoryViewModel;
//
//    /**
//     * Called when the activity is created. Initializes the activity's layout, sets up the ViewModel,
//     * handles the window insets, and defines click listeners for navigation buttons.
//     *
//     * @param savedInstanceState If the activity is being re-created from a previous state, this bundle contains the saved state.
//     */
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main_photo_history);
//
//        // Initializing the ViewModel to manage UI-related data and logic.
//        mainPhotoHistoryViewModel = new ViewModelProvider(this).get(MainPhotoHistoryViewModel.class);
//        DBManager dbManager = mainPhotoHistoryViewModel.getDbManager();
//
//        // Adjusts the padding of the main view to account for system bars (like the status bar).
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_photo_history), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        //-------------------------------------------------Navigation to Main
//        // Setting click listeners for navigation buttons in the main view.
//        findViewById(R.id.mainButton_main_photo_history).setOnClickListener(v -> mainPhotoHistoryViewModel.onMainButtonClicked());
//        findViewById(R.id.cameraButton_main_photo_history).setOnClickListener(v -> mainPhotoHistoryViewModel.onMainButtonClicked());
//        findViewById(R.id.backButton_main_photo_history).setOnClickListener(v -> mainPhotoHistoryViewModel.onMainButtonClicked());
//
//        // Observing the navigation LiveData in the ViewModel to handle navigation events.
//        mainPhotoHistoryViewModel.getNavigateToMain().observe(this, navigate -> {
//            if (navigate) {
//                Intent intent = new Intent(MainPhotoHistoryActivity.this, MainActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                mainPhotoHistoryViewModel.resetNavigationMain();
//            }
//        });
//
//        //-------------------------------------------------Navigation to Books
//        // Setting click listener for the bookshelf button in the main view.
//        findViewById(R.id.bookshelfButton_main_photo_history).setOnClickListener(v -> mainPhotoHistoryViewModel.onBookshelfButtonClicked());
//
//        // Observing the navigation LiveData in the ViewModel to handle navigation events.
//        mainPhotoHistoryViewModel.getNavigateToBookshelf().observe(this, navigate -> {
//            if (navigate) {
//                Intent intent = new Intent(MainPhotoHistoryActivity.this, BookshelfActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                mainPhotoHistoryViewModel.resetNavigationBookshelf();
//            }
//        });
//
//        //-------------------------------------------------Navigation to User
//        // Setting click listener for the user button in the main view.
//        findViewById(R.id.userButton_main_photo_history).setOnClickListener(v -> mainPhotoHistoryViewModel.onUserButtonClicked());
//
//        // Observing the navigation LiveData in the ViewModel to handle navigation events.
//        mainPhotoHistoryViewModel.getNavigateToUser().observe(this, navigate -> {
//            if (navigate) {
//                Intent intent = new Intent(MainPhotoHistoryActivity.this, UserActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                mainPhotoHistoryViewModel.resetNavigationUser();
//            }
//        });
//    }
//
//}
