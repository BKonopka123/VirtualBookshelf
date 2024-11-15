package com.example.virtualbookshelf.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.viewmodel.BookFoundDetailViewModel;

/**
 * Activity for displaying detailed information about a book.
 */
public class BookFoundDetailActivity extends AppCompatActivity
        implements BookFoundTitleChangeDialog.OnBookFoundTitleChangeListener,
        BookFoundAuthorChangeDialog.OnBookFoundAuthorChangeListener,
        BookFoundStatusChangeDialog.OnBookFoundStatusChangeListener,
        BookFoundDescriptionChangeDialog.OnBookFoundDescriptionChangeListener,
        BookFoundGenreChangeDialog.OnBookFoundGenreChangeListener,
        BookFoundDateChangeDialog.OnBookFoundDateChangeListener {

    /**
     * ViewModel associated with this activity.
     */
    private BookFoundDetailViewModel bookFoundDetailViewModel;

    /**
     * The book object associated with this activity.
     */
    private Book book;

    /** UI elements */
    private ImageView bookPhoto;
    private TextView bookTitle;
    private TextView bookAuthor;
    private ImageView bookStatusPhoto;
    private TextView bookDescription;
    private TextView bookGenre;
    private TextView bookDate;

    /**
     * Initializes the activity and sets up the UI.
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_found_detail);

        // Initializing the ViewModel to manage UI-related data and logic.
        bookFoundDetailViewModel = new ViewModelProvider(this).get(BookFoundDetailViewModel.class);
        DBManager dbManager = bookFoundDetailViewModel.getDbManager();

        // Retrieving the book object from the intent.
        this.book = (Book) getIntent().getSerializableExtra("book");

        // Initializing UI elements.
        bookPhoto = findViewById(R.id.book_image_book_found_detail);
        bookTitle = findViewById(R.id.book_title_book_found_detail);
        bookAuthor = findViewById(R.id.book_author_book_found_detail);
        bookStatusPhoto = findViewById(R.id.status_image_book_found_detail);
        bookDescription = findViewById(R.id.book_description_text_book_found_detail);
        bookGenre = findViewById(R.id.book_genre_text_book_found_detail);
        bookDate = findViewById(R.id.book_date_text_book_found_detail);

        updateUIBook();

        // Adjusts the padding of the main view to account for system bars (like the status bar).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bookshelf_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //-------------------------------------------------Navigation to Main
        // Setting click listeners for navigation buttons in the main view.
        findViewById(R.id.mainButton_book_found_detail).setOnClickListener(v -> bookFoundDetailViewModel.onMainButtonClicked());
        findViewById(R.id.cameraButton_book_found_detail).setOnClickListener(v -> bookFoundDetailViewModel.onMainButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        bookFoundDetailViewModel.getNavigateToMain().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookFoundDetailActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookFoundDetailViewModel.resetNavigationMain();
            }
        });

        //-------------------------------------------------Navigation to Books
        // Setting click listener for the bookshelf button in the main view.
        findViewById(R.id.bookshelfButton_book_found_detail).setOnClickListener(v -> bookFoundDetailViewModel.onBookshelfButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        bookFoundDetailViewModel.getNavigateToBookshelf().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookFoundDetailActivity.this, BookshelfActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookFoundDetailViewModel.resetNavigationBookshelf();
            }
        });

        //-------------------------------------------------Navigation to User
        // Setting click listener for the user button in the main view.
        findViewById(R.id.userButton_book_found_detail).setOnClickListener(v -> bookFoundDetailViewModel.onUserButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        bookFoundDetailViewModel.getNavigateToUser().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(BookFoundDetailActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookFoundDetailViewModel.resetNavigationUser();
            }
        });

        //-------------------------------------------------saving and going back
        findViewById(R.id.backButton_book_found_detail).setOnClickListener(v -> bookFoundDetailViewModel.onBackButtonClicked());

        bookFoundDetailViewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updated_book", book);
                setResult(RESULT_OK, resultIntent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                bookFoundDetailViewModel.resetNavigationBack();
            }
        });

        //-------------------------------------------------Changing book information
        // Set click listener for the delete database button.
        findViewById(R.id.editTitleBook_book_found_detail).setOnClickListener(delete -> {
            BookFoundTitleChangeDialog dialog = new BookFoundTitleChangeDialog(bookFoundDetailViewModel, dbManager, book);
            dialog.show(getSupportFragmentManager(), "ChangeBookFoundTitleDialog");
        });

        findViewById(R.id.editAuthorBook_book_found_detail).setOnClickListener(delete -> {
            BookFoundAuthorChangeDialog dialog = new BookFoundAuthorChangeDialog(bookFoundDetailViewModel, dbManager, book);
            dialog.show(getSupportFragmentManager(), "ChangeBookFoundAuthorDialog");
        });

        findViewById(R.id.editStatusBook_book_found_detail).setOnClickListener(delete -> {
            BookFoundStatusChangeDialog dialog = new BookFoundStatusChangeDialog(bookFoundDetailViewModel, dbManager, book);
            dialog.show(getSupportFragmentManager(), "ChangeBookFoundStatusDialog");
        });

        findViewById(R.id.editDescriptionBook_book_found_detail).setOnClickListener(delete -> {
            BookFoundDescriptionChangeDialog dialog = new BookFoundDescriptionChangeDialog(bookFoundDetailViewModel, dbManager, book);
            dialog.show(getSupportFragmentManager(), "ChangeBookFoundDescriptionDialog");
        });

        findViewById(R.id.editGenreBook_book_found_detail).setOnClickListener(delete -> {
            BookFoundGenreChangeDialog dialog = new BookFoundGenreChangeDialog(bookFoundDetailViewModel, dbManager, book);
            dialog.show(getSupportFragmentManager(), "ChangeBookFoundGenreDialog");
        });

        findViewById(R.id.editDateBook_book_found_detail).setOnClickListener(delete -> {
            BookFoundDateChangeDialog dialog = new BookFoundDateChangeDialog(bookFoundDetailViewModel, dbManager, book);
            dialog.show(getSupportFragmentManager(), "ChangeBookFoundDateDialog");
        });
    }

    /**
     * Called when the title of the book is changed.
     * @param updatedBook The updated book object.
     */
    @Override
    public void onFoundAuthorChanged(Book updatedBook) {
        this.book = updatedBook;
        updateUIBook();
    }

    /**
     * Called when the author of the book is changed.
     * @param updatedBook The updated book object.
     */
    @Override
    public void onFoundDateChanged(Book updatedBook) {
        this.book = updatedBook;
        updateUIBook();
    }

    /**
     * Called when the description of the book is changed.
     * @param updatedBook The updated book object.
     */
    @Override
    public void onFoundDescriptionChanged(Book updatedBook) {
        this.book = updatedBook;
        updateUIBook();
    }

    /**
     * Called when the genre of the book is changed.
     * @param updatedBook The updated book object.
     */
    @Override
    public void onFoundGenreChanged(Book updatedBook) {
        this.book = updatedBook;
        updateUIBook();
    }

    /**
     * Called when the status of the book is changed.
     * @param updatedBook The updated book object.
     */
    @Override
    public void onFoundStatusChanged(Book updatedBook) {
        this.book = updatedBook;
        updateUIBook();
    }

    /**
     * Called when the title of the book is changed.
     * @param updatedBook The updated book object.
     */
    @Override
    public void onFoundTitleChanged(Book updatedBook) {
        this.book = updatedBook;
        updateUIBook();
    }

    /**
     * Updates the UI with the current book information.
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
            bookFoundDetailViewModel.onMainButtonClicked();
        }
    }
}
