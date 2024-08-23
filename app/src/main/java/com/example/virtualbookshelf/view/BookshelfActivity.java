package com.example.virtualbookshelf.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.viewmodel.BookshelfViewModel;

public class BookshelfActivity extends AppCompatActivity {

    private BookshelfViewModel bookshelfViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookshelf);

        bookshelfViewModel = new ViewModelProvider(this).get(BookshelfViewModel.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bookshelf), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.mainButton_bookshelf).setOnClickListener(v -> bookshelfViewModel.onMainButtonClicked());

        findViewById(R.id.cameraButton_bookshelf).setOnClickListener(v -> bookshelfViewModel.onMainButtonClicked());

        bookshelfViewModel.getNavigateToMain().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookshelfActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookshelfViewModel.resetNavigationMain();
            }
        });

        findViewById(R.id.bookshelfButton_bookshelf).setOnClickListener(v -> bookshelfViewModel.onBookshelfButtonClicked());

        bookshelfViewModel.getNavigateToBookshelf().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookshelfActivity.this, BookshelfActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookshelfViewModel.resetNavigationBookshelf();
            }
        });

        findViewById(R.id.userButton_bookshelf).setOnClickListener(v -> bookshelfViewModel.onUserButtonClicked());

        bookshelfViewModel.getNavigateToUser().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookshelfActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookshelfViewModel.resetNavigationUser();
            }
        });
    }
}