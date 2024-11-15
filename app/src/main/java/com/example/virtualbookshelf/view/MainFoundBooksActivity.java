package com.example.virtualbookshelf.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.Photo;
import com.example.virtualbookshelf.model.db.DBManager;
import com.example.virtualbookshelf.model.ml.FoundObject;
import com.example.virtualbookshelf.viewmodel.MainFoundBooksViewModel;

import java.util.ArrayList;

/**
 * Activity to show the found books
 */
public class MainFoundBooksActivity extends AppCompatActivity {

    /**
     * The view model for managing UI-related data and user interactions.
     */
    private MainFoundBooksViewModel mainFoundBooksViewModel;

    /**
     * TextView to show that no books were found.
     */
    private TextView noBooksFoundTextView;

    /**
     * List of books.
     */
    private ArrayList<Book> books = new ArrayList<>();

    /**
     * Photo of the books.
     */
    private Photo photo;

    /**
     * RecyclerView to show the list of books.
     */
    private RecyclerView recyclerView;

    /**
     * Adapter for the RecyclerView.
     */
    private BookFoundItemAdapter adapter;

    /**
     * Launcher for editing a book.
     */
    private final ActivityResultLauncher<Intent> editBookLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Book updatedBook = (Book) result.getData().getSerializableExtra("updated_book");
                    if (updatedBook != null) {
                        updateBookInList(updatedBook);
                    }
                }
            }
    );

    AppCompatButton addToDatabaseButton;

    /**
     * Called when the activity is created. Initializes the activity's layout, sets up the ViewModel,
     * @param savedInstanceState If the activity is being re-created from a previous state, this bundle contains the saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_found_books);

        // Initializing the ViewModel to manage UI-related data and logic.
        mainFoundBooksViewModel = new ViewModelProvider(this).get(MainFoundBooksViewModel.class);
        DBManager dbManager = mainFoundBooksViewModel.getDbManager();
        addToDatabaseButton = findViewById(R.id.addToDatabase_button_bookshelf_detail);

        // Getting and managing received books
        ArrayList<FoundObject> foundBooks = getIntent().getParcelableArrayListExtra("foundBooks");
        checkFoundBooks(foundBooks);

        //Initialize recyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView_main_found_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookFoundItemAdapter(books, this, book->{
            Intent intent = new Intent(MainFoundBooksActivity.this, BookFoundDetailActivity.class);
            intent.putExtra("book", book);
            editBookLauncher.launch(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        recyclerView.setAdapter(adapter);

        // Adjusts the padding of the main view to account for system bars (like the status bar).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_found_books), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //-------------------------------------------------Navigation to Main
        // Setting click listeners for navigation buttons in the main view.
        findViewById(R.id.mainButton_main_found_books).setOnClickListener(v -> mainFoundBooksViewModel.onMainButtonClicked());
        findViewById(R.id.cameraButton_main_found_books).setOnClickListener(v -> mainFoundBooksViewModel.onMainButtonClicked());
        findViewById(R.id.backButton_main_found_books).setOnClickListener(v -> mainFoundBooksViewModel.onMainButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainFoundBooksViewModel.getNavigateToMain().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(MainFoundBooksActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                mainFoundBooksViewModel.resetNavigationMain();
            }
        });

        //-------------------------------------------------Navigation to Books
        // Setting click listener for the bookshelf button in the main view.
        findViewById(R.id.bookshelfButton_main_found_books).setOnClickListener(v -> mainFoundBooksViewModel.onBookshelfButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainFoundBooksViewModel.getNavigateToBookshelf().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(MainFoundBooksActivity.this, BookshelfActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                mainFoundBooksViewModel.resetNavigationBookshelf();
            }
        });

        //-------------------------------------------------Navigation to User
        // Setting click listener for the user button in the main view.
        findViewById(R.id.userButton_main_found_books).setOnClickListener(v -> mainFoundBooksViewModel.onUserButtonClicked());

        // Observing the navigation LiveData in the ViewModel to handle navigation events.
        mainFoundBooksViewModel.getNavigateToUser().observe(this, navigate -> {
            if (navigate) {
                Intent intent = new Intent(MainFoundBooksActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                mainFoundBooksViewModel.resetNavigationUser();
            }
        });

        //-------------------------------------------------Adding Books to Database
        findViewById(R.id.addToDatabase_button_bookshelf_detail).setOnClickListener(v -> mainFoundBooksViewModel.onAddBooksToDatabaseDataClicked());

        mainFoundBooksViewModel.getAddBooksToDatabaseData().observe(this, navigate -> {
            if (navigate) {
                addToDatabaseButton.setBackgroundResource(R.drawable.rounded_button_background_invalid);
                addToDatabaseButton.setEnabled(false);
                addToDatabaseButton.setClickable(false);
                mainFoundBooksViewModel.addBooksToDatabase(this.books, this.photo);
                mainFoundBooksViewModel.onBookshelfButtonClicked();
                mainFoundBooksViewModel.resetAddBooksToDatabaseData();
            }
        });
    }

    /**
     * Checks if there are any found books.
     * @param foundBooks List of found books.
     */
    public void checkFoundBooks(ArrayList<FoundObject> foundBooks){
        noBooksFoundTextView = findViewById(R.id.text_noBooks_main_found_books);
        //The case where no books were found
        if(foundBooks == null || foundBooks.isEmpty()) {
            noBooksFoundTextView.setVisibility(View.VISIBLE);
            addToDatabaseButton.setVisibility(View.GONE);
        // The case where books were found
        } else {
            noBooksFoundTextView.setVisibility(View.GONE);

            this.photo = mainFoundBooksViewModel.createNewPhoto(foundBooks);

            ArrayList<Book> booksTmp = mainFoundBooksViewModel.convertFoundObjectsToBooks(foundBooks);
            for (Book book : booksTmp) {
                String bookTitle = book.getTitle();;
                if (bookTitle == null) {
                    bookTitle = " ";
                }
                String bookAuthor = book.getAuthor();
                if (bookAuthor == null) {
                    bookAuthor = " ";
                }
                String bookDescription = book.getDescription();
                if (bookDescription == null) {
                    bookDescription = " ";
                }
                String bookGenre = book.getGenre();
                if (bookGenre == null) {
                    bookGenre = " ";
                }
                String bookDate = book.getDate();
                if (bookDate == null) {
                    bookDate = " ";
                }
                this.books.add(new Book(book.getId(), book.getPhotoId(), book.getUserId(), bookTitle, bookAuthor, book.getPhoto(), bookDescription, bookGenre, bookDate, book.getStatus(), book.getIsAdded()));
            }

            mainFoundBooksViewModel.checkIsAddedBook(this.books);

            mainFoundBooksViewModel.LogBooks(this.books, this.photo);
        }
    }

    /**
     * Updates a book in the list.
     * @param updatedBook The updated book.
     */
    private void updateBookInList(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(updatedBook.getId())) {
                books.set(i, updatedBook);
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }
}
