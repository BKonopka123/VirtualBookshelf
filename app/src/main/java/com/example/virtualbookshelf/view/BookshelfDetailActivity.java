package com.example.virtualbookshelf.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.viewmodel.BookshelfDetailViewModel;

/**
 * BookshelfDetailActivity is an activity representing the detail view of a book in the bookshelf.
 */
public class BookshelfDetailActivity extends AppCompatActivity implements BookStatusChangeDialog.OnBookStatusChangeListener {

    /**
     * The ViewModel associated with this activity.
     */
    private BookshelfDetailViewModel bookshelfDetailViewModel;

    /**
     * The book object representing the book being displayed in the detail view.
     */
    private Book book;

    /**
     * UI elements for displaying book details.
     */
    private ImageView bookPhoto;
    private TextView bookTitle;
    private TextView bookAuthor;
    private ImageView bookStatusPhoto;
    private TextView bookDescription;
    private TextView bookGenre;
    private TextView bookDate;

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookshelf_detail);

        // Initializing the ViewModel to manage UI-related data and logic.
        bookshelfDetailViewModel = new ViewModelProvider(this).get(BookshelfDetailViewModel.class);
        DBManager dbManager = bookshelfDetailViewModel.getDbManager();

        this.book = (Book) getIntent().getSerializableExtra("book");

        bookPhoto = findViewById(R.id.book_image_bookshelf_detail);
        bookTitle = findViewById(R.id.book_title_bookshelf_detail);
        bookAuthor = findViewById(R.id.book_author_bookshelf_detail);
        bookStatusPhoto = findViewById(R.id.status_image_bookshelf_detail);
        bookDescription = findViewById(R.id.book_description_text_bookshelf_detail);
        bookGenre = findViewById(R.id.book_genre_text_bookshelf_detail);
        bookDate = findViewById(R.id.book_date_text_bookshelf_detail);

        updateUIBook();

        // Adjusts the padding of the main view to account for system bars (like the status bar).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bookshelf_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //-------------------------------------------------Navigation to Main
        // Setting click listeners for navigation buttons in the main view.
        findViewById(R.id.mainButton_bookshelf_detail).setOnClickListener(v -> bookshelfDetailViewModel.onMainButtonClicked());
        findViewById(R.id.cameraButton_bookshelf_detail).setOnClickListener(v -> bookshelfDetailViewModel.onMainButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        bookshelfDetailViewModel.getNavigateToMain().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookshelfDetailActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookshelfDetailViewModel.resetNavigationMain();
            }
        });

        //-------------------------------------------------Navigation to Books
        // Setting click listener for the bookshelf button in the main view.
        findViewById(R.id.bookshelfButton_bookshelf_detail).setOnClickListener(v -> bookshelfDetailViewModel.onBookshelfButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        bookshelfDetailViewModel.getNavigateToBookshelf().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookshelfDetailActivity.this, BookshelfActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookshelfDetailViewModel.resetNavigationBookshelf();
            }
        });

        //-------------------------------------------------Navigation to User
        // Setting click listener for the user button in the main view.
        findViewById(R.id.userButton_bookshelf_detail).setOnClickListener(v -> bookshelfDetailViewModel.onUserButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        bookshelfDetailViewModel.getNavigateToUser().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookshelfDetailActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookshelfDetailViewModel.resetNavigationUser();
            }
        });

        //-------------------------------------------------saving and going back
        findViewById(R.id.backButton_bookshelf_detail).setOnClickListener(v -> bookshelfDetailViewModel.onBackButtonClicked());

        bookshelfDetailViewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                bookshelfDetailViewModel.updateBook(book);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updated_book", book);
                setResult(RESULT_OK, resultIntent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookshelfDetailViewModel.resetNavigationBack();
            }
        });

        //-------------------------------------------------Deleting book
        // Set click listener for the delete database button.
        findViewById(R.id.deleteBook_button_bookshelf_detail).setOnClickListener(delete -> {
            BookDeleteDialog dialog = new BookDeleteDialog(bookshelfDetailViewModel, dbManager, book);
            dialog.show(getSupportFragmentManager(), "DeleteBookDialog");
        });

        //-------------------------------------------------Changing book status
        // Set click listener for the delete database button.
        findViewById(R.id.editStatusBook_bookshelf_detail).setOnClickListener(delete -> {
            BookStatusChangeDialog dialog = new BookStatusChangeDialog(bookshelfDetailViewModel, dbManager, book);
            dialog.show(getSupportFragmentManager(), "ChangeBookStatusDialog");
        });
    }

    /**
     * Called when the status of a book is changed.
     * @param updatedBook The updated book object.
     */
    @Override
    public void onStatusChanged(Book updatedBook) {
        this.book = updatedBook;
        updateUIBook();
    }

    /**
     * Updates the UI to display the details of the book.
     */
    private void updateUIBook() {
        if (book != null) {
            bookPhoto.setImageBitmap(BlobManager.getBitmapFromBlob(book.getPhoto()));
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());

            switch (book.getStatus()) {
                case "Read":
                    bookStatusPhoto.setImageResource(R.drawable.ic_status_read);
                    break;
                case "Unread":
                    bookStatusPhoto.setImageResource(R.drawable.ic_status_unread);
                    break;
                case "Currently":
                    bookStatusPhoto.setImageResource(R.drawable.ic_status_currently);
                    break;
                case "Queue":
                    bookStatusPhoto.setImageResource(R.drawable.ic_status_queue);
                    break;
            }

            bookDescription.setText(book.getDescription());
            bookGenre.setText(book.getGenre());
            bookDate.setText(book.getDate());
        }
        else {
            bookshelfDetailViewModel.onBookshelfButtonClicked();
        }
    }
}
