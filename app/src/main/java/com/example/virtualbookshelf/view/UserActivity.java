package com.example.virtualbookshelf.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TextView;

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
import com.example.virtualbookshelf.model.User;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.viewmodel.UserViewModel;

import java.io.IOException;
import java.io.InputStream;

public class UserActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private ImageView profileImage_user;
    private TextView username_user;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @SuppressLint("IntentReset")
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        DBManager dbManager = userViewModel.getDbManager();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //-------------------------------------------------Navigation to Main
        findViewById(R.id.mainButton_user).setOnClickListener(v -> userViewModel.onMainButtonClicked());

        findViewById(R.id.cameraButton_user).setOnClickListener(v -> userViewModel.onMainButtonClicked());

        userViewModel.getNavigateToMain().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                userViewModel.resetNavigationMain();
            }
        });

        //-------------------------------------------------Navigation to Books
        findViewById(R.id.bookshelfButton_user).setOnClickListener(v -> userViewModel.onBookshelfButtonClicked());

        userViewModel.getNavigateToBookshelf().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(UserActivity.this, BookshelfActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                userViewModel.resetNavigationBookshelf();
            }
        });

        //-------------------------------------------------Navigation to User
        findViewById(R.id.userButton_user).setOnClickListener(v -> userViewModel.onUserButtonClicked());

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
                profileImage_user = findViewById(R.id.profileImage_user);
                username_user = findViewById(R.id.usernameText_user);
                Bitmap bitmap = BlobManager.getBitmapFromBlob(user.getProfilePhoto());
                profileImage_user.setImageBitmap(bitmap);
                username_user.setText(user.getUsername());
            }
        });

        //-------------------------------------------------Updating profile Photo
        findViewById(R.id.editProfileImage_user).setOnClickListener(v -> openGallery());

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    if (imageUri != null) {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        if (inputStream == null) {
                            return;
                        }
                        byte[] imageBytes = BlobManager.getByteFromUri(inputStream);
                        inputStream.close();

                        Cursor cursor_user = dbManager.getUserById(1);
                        if(cursor_user.moveToFirst()){
                        User user = new User(cursor_user.getInt(0), cursor_user.getString(1), imageBytes, cursor_user.getInt(3), cursor_user.getInt(4), cursor_user.getInt(5), cursor_user.getInt(6), cursor_user.getInt(7));
                        dbManager.updateUser(user);
                        }
                        cursor_user.close();
                        userViewModel.refreshUserData();
                    }
                } catch (IOException e) {
                    Log.e("ImageError", "Error getting image", e);
                }
            }
        });

        //-------------------------------------------------Updating username
        findViewById(R.id.editUsername_user).setOnClickListener(update -> {
            UsernameChangeDialog dialog = new UsernameChangeDialog(userViewModel, dbManager);
            dialog.show(getSupportFragmentManager(), "UsernameChangeDialog");
        });
    }
}
