package com.example.virtualbookshelf.view.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.view.Book.BookshelfActivity;
import com.example.virtualbookshelf.view.MainActivity;
import com.example.virtualbookshelf.viewmodel.User.UserViewModel;

import java.io.IOException;
import java.io.InputStream;

/**
 * The UserActivity class represents the user's profile screen where they can view and edit their profile information,
 * including their profile image, username, and book statistics. It uses a ViewModel to manage user data and interact
 * with the database. The activity also provides functionality for navigating between other sections of the app.
 */
public class UserActivity extends AppCompatActivity {

    /** The ViewModel for managing user-related data. */
    private UserViewModel userViewModel;

    /** UI components for displaying user's profile information. */
    private ImageView profileImage_user;
    private TextView username_user;
    private TextView allBooks_user;
    private TextView readBooks_user;
    private TextView unreadBooks_user;
    private TextView currentlyBooks_user;
    private TextView queueBooks_user;

    /** Launcher for picking an image from the gallery. */
    private ActivityResultLauncher<Intent> pickImageLauncher;

    /**
     * Opens the device's gallery to allow the user to select a profile image.
     */
    @SuppressLint("IntentReset")
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    /**
     * Called when the activity is created. Sets up the layout, initializes the ViewModel, handles user interactions,
     * and observes changes in user data to update the UI.
     *
     * @param savedInstanceState If the activity is being re-created from a previous state, this bundle contains the saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        // Initialize the ViewModel and refresh user data.
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        DBManager dbManager = userViewModel.getDbManager();
        userViewModel.refreshUserData();

        // Handle window insets to adjust padding for system bars (status bar, navigation bar, etc.).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //-------------------------------------------------Navigation to Main
        // Set click listeners for main buttons in the user UI.
        findViewById(R.id.mainButton_user).setOnClickListener(v -> userViewModel.onMainButtonClicked());
        findViewById(R.id.cameraButton_user).setOnClickListener(v -> userViewModel.onMainButtonClicked());

        // Observe the navigation LiveData in the view model.
        userViewModel.getNavigateToMain().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                userViewModel.resetNavigationMain();
            }
        });

        //-------------------------------------------------Navigation to Books
        // Set click listeners for bookshelf buttons in the user UI.
        findViewById(R.id.bookshelfButton_user).setOnClickListener(v -> userViewModel.onBookshelfButtonClicked());

        // Observe the navigation LiveData in the view model.
        userViewModel.getNavigateToBookshelf().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(UserActivity.this, BookshelfActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                userViewModel.resetNavigationBookshelf();
            }
        });

        //-------------------------------------------------Navigation to User
        // Set click listener for user button in the user UI.
        findViewById(R.id.userButton_user).setOnClickListener(v -> userViewModel.onUserButtonClicked());

        // Observe the navigation LiveData in the view model.
        userViewModel.getNavigateToUser().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(UserActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                userViewModel.resetNavigationUser();
            }
        });

        //-------------------------------------------------Live Updating User
        userViewModel.getUser().observe(this, user -> {
            if (user != null && user.getProfilePhoto() != null) {
                // Get the UI components for displaying user information.
                profileImage_user = findViewById(R.id.profileImage_user);
                username_user = findViewById(R.id.usernameText_user);
                allBooks_user = findViewById(R.id.allBooks_user);
                readBooks_user = findViewById(R.id.readBooks_user);
                unreadBooks_user = findViewById(R.id.unreadBooks_user);
                currentlyBooks_user = findViewById(R.id.currentlyReadingBooks_user);
                queueBooks_user = findViewById(R.id.queueBooks_user);

                // Set the user's profile image, username, and book statistics.
                Bitmap bitmap = BlobManager.getBitmapFromBlob(user.getProfilePhoto());
                profileImage_user.setImageBitmap(bitmap);
                username_user.setText(user.getUsername());

                // Set the user's book statistics.
                String allBooks_user_text = "All Books: " + user.getBooksNumber();
                allBooks_user.setText(allBooks_user_text);
                String readBooks_user_text = "Read Books: " + user.getBooksReadNumber();
                readBooks_user.setText(readBooks_user_text);
                String unreadBooks_user_text = "Unread Books: " + user.getBooksUnreadNumber();
                unreadBooks_user.setText(unreadBooks_user_text);
                String currentlyBooks_user_text = "Books currently being read: " + user.getBooksCurrentlyNumber();
                currentlyBooks_user.setText(currentlyBooks_user_text);
                String queueBooks_user_text = "Books in queue: " + user.getBooksQueueNumber();
                queueBooks_user.setText(queueBooks_user_text);
            }
        });

        //-------------------------------------------------Updating profile Photo
        // Set click listener for the edit profile image button.
        findViewById(R.id.editProfileImage_user).setOnClickListener(v -> openGallery());

        // Register an activity result launcher to handle the selected image from the gallery.
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    if (imageUri != null) {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        if (inputStream == null) {
                            return;
                        }

                        // Check if the image is valid and update the user's profile image if it is.
                        Integer UserProfileImageCheck = userViewModel.checkUserProfileImage(imageUri);
                        if (UserProfileImageCheck == 0) {
                            userViewModel.updateUserProfileImage(inputStream, imageUri);
                        } else if (UserProfileImageCheck == 1){
                            Toast toast = Toast.makeText(this, "Wrong file type", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            inputStream.close();
                        } else if (UserProfileImageCheck == 2){
                            Toast toast = Toast.makeText(this, "Image is too big", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            inputStream.close();
                        }
                        userViewModel.refreshUserData();
                    }
                } catch (IOException e) {
                    Log.e("ImageError", "Error getting image", e);
                }
            }
        });

        //-------------------------------------------------Updating username
        // Set click listener for the edit username button.
        findViewById(R.id.editUsername_user).setOnClickListener(update -> {
            UsernameChangeDialog dialog = new UsernameChangeDialog(userViewModel, dbManager);
            dialog.show(getSupportFragmentManager(), "UsernameChangeDialog");
        });

        //-------------------------------------------------Deleting database
        // Set click listener for the delete database button.
        findViewById(R.id.deleteDatabase_user).setOnClickListener(delete -> {
            DatabaseDeleteDialog dialog = new DatabaseDeleteDialog(userViewModel, dbManager);
            dialog.show(getSupportFragmentManager(), "DeleteDatabaseDialog");
        });
    }
}

