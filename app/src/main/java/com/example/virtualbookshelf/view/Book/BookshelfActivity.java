package com.example.virtualbookshelf.view.Book;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.view.MainActivity;
import com.example.virtualbookshelf.view.User.UserActivity;
import com.example.virtualbookshelf.viewmodel.Book.BookshelfViewModel;

import java.util.ArrayList;

/**
 * BookshelfActivity is an activity representing the virtual bookshelf screen.
 * It provides UI for interacting with the user's bookshelf, handling navigation to other activities
 * such as the main screen, user profile, or another bookshelf screen.
 */
public class BookshelfActivity extends AppCompatActivity {

    /** The view model used to manage the bookshelf's data and actions. */
    private BookshelfViewModel bookshelfViewModel;

    /** The list of books in the bookshelf. */
    private ArrayList<Book> books;

    /** RecyclerView and adapter for displaying the bookshelf's items. */
    private RecyclerView recyclerView;

    /** Adapter for the RecyclerView. */
    private BookshelfItemAdapter adapter;

    /**
     * Launcher for starting the BookshelfDetailActivity.
     */
    private final ActivityResultLauncher<Intent> editBookLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    bookshelfViewModel.refreshBooksData();
                    adapter.updateBooks(new ArrayList<>(books));
                }
            }
    );

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * This method sets up the user interface, handles window insets for system bars,
     *  and manages button clicks for navigation.
     *
     * @param savedInstanceState Saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display.
        EdgeToEdge.enable(this);
        // Set the layout for this activity.
        setContentView(R.layout.activity_bookshelf);

        // Initialize the view model using ViewModelProvider to manage UI-related data.
        bookshelfViewModel = new ViewModelProvider(this).get(BookshelfViewModel.class);
        // Get the database manager from the view model.
        DBManager dbManager = bookshelfViewModel.getDbManager();

        // Initialize the books list.
        books = new ArrayList<>();
        bookshelfViewModel.refreshBooksData();

        //Initialize recyclerView and adapter
        recyclerView = findViewById(R.id.middleLayout_bookshelf);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookshelfItemAdapter(books, this, book->{
            Intent intent = new Intent(BookshelfActivity.this, BookshelfDetailActivity.class);
            intent.putExtra("book", book);
            editBookLauncher.launch(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        recyclerView.setAdapter(adapter);

        // Handle window insets to adjust padding for system bars (status bar, navigation bar, etc.).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bookshelf), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //-------------------------------------------------Navigation to Main
        // Set click listeners for main buttons in the bookshelf UI.
        findViewById(R.id.mainButton_bookshelf).setOnClickListener(v -> bookshelfViewModel.onMainButtonClicked());
        findViewById(R.id.cameraButton_bookshelf).setOnClickListener(v -> bookshelfViewModel.onMainButtonClicked());

        // Observe the navigation LiveData in the view model.
        bookshelfViewModel.getNavigateToMain().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookshelfActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookshelfViewModel.resetNavigationMain();
            }
        });

        //-------------------------------------------------Navigation to Books
        // Set click listeners for bookshelf buttons in the bookshelf UI.
        findViewById(R.id.bookshelfButton_bookshelf).setOnClickListener(v -> bookshelfViewModel.onBookshelfButtonClicked());

        // Observe the navigation LiveData in the view model.
        bookshelfViewModel.getNavigateToBookshelf().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookshelfActivity.this, BookshelfActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookshelfViewModel.resetNavigationBookshelf();
            }
        });

        //-------------------------------------------------Navigation to User
        // Set click listener for user button in the bookshelf UI.
        findViewById(R.id.userButton_bookshelf).setOnClickListener(v -> bookshelfViewModel.onUserButtonClicked());

        // Observe the navigation LiveData in the view model.
        bookshelfViewModel.getNavigateToUser().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookshelfActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookshelfViewModel.resetNavigationUser();
            }
        });

        //-------------------------------------------------Filling up the bookshelf
        bookshelfViewModel.getBooks().observe(this, books -> {
            this.books = books;
            adapter.updateBooks(new ArrayList<>(books));
        });
    }
}