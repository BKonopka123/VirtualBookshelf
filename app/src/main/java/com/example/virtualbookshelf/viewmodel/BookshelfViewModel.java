package com.example.virtualbookshelf.viewmodel;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.virtualbookshelf.model.Book;

import java.util.ArrayList;

/**
 * BookshelfViewModel is a ViewModel responsible for managing bookshelf-related data.
 * It inherits from BaseViewModel and doesn't currently implement any additional logic.
 */
public class BookshelfViewModel extends BaseViewModel {

    /** LiveData containing the list of books in the bookshelf. */
    private final MutableLiveData<ArrayList<Book>> booksLiveData;

    /**
     * Constructor for BookshelfViewModel.
     * Initializes the ViewModel by calling the constructor of BaseViewModel.
     *
     * @param application The application context.
     */
    public BookshelfViewModel(Application application) {
        super(application);
        this.booksLiveData = new MutableLiveData<>();
        loadBooksData();
    }

    /**
     * Loads the data for the bookshelf.
     */
    public void loadBooksData() {
        Cursor cursor_books = dbManager.getAllBooks();
        ArrayList<Book> books = new ArrayList<>();
        try {
            for (cursor_books.moveToFirst(); !cursor_books.isAfterLast(); cursor_books.moveToNext()) {
                Book book = new Book(cursor_books.getInt(0), cursor_books.getInt(1), cursor_books.getInt(2), cursor_books.getString(3), cursor_books.getString(4), cursor_books.getBlob(5), cursor_books.getString(6), cursor_books.getString(7), cursor_books.getString(8), cursor_books.getString(9), cursor_books.getInt(10) > 0);
                books.add(book);
            }
            booksLiveData.setValue(books);
            cursor_books.close();
        } catch(Exception e) {
            Log.e("BookshelfViewModel", e.getMessage(), e);
        }
    }

    /**
     * Returns the LiveData containing the list of books in the bookshelf.
     *
     * @return The LiveData containing the list of books in the bookshelf.
     */
    public MutableLiveData<ArrayList<Book>> getBooks() { return booksLiveData; }

    /**
     * Refreshes the data for the bookshelf.
     */
    public void refreshBooksData() { loadBooksData(); }

}