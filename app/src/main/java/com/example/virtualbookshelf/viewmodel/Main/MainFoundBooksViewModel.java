package com.example.virtualbookshelf.viewmodel.Main;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.Photo;
import com.example.virtualbookshelf.model.ml.FoundObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * MainPhotoHistoryViewModel is a ViewModel responsible for managing found books data.
 */
public class MainFoundBooksViewModel extends MainViewModel {

    /**
     * The application context.
     */
    private final Context context;

    /**
     * LiveData to indicate when to add books to database.
     */
    protected final MutableLiveData<Boolean> addBooksToDatabaseData = new MutableLiveData<>();

    /**
     //     * Constructor for MainPhotoHistoryViewModel.
     //     * @param application The application context.
     //     */
    public MainFoundBooksViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    /**
     * Sets the LiveData to add books to database.
     */
    public void onAddBooksToDatabaseDataClicked() { addBooksToDatabaseData.setValue(true); }

    /**
     * Gets the LiveData to add books to database.
     * @return LiveData to add books to database.
     */
    public LiveData<Boolean> getAddBooksToDatabaseData() { return addBooksToDatabaseData; }

    /**
     * Resets the LiveData to add books to database.
     */
    public void resetAddBooksToDatabaseData() { addBooksToDatabaseData.setValue(false); }

    /**
     * Converts found objects to books.
     * @param foundObjects List of found objects.
     * @return List of books.
     */
    public ArrayList<Book> convertFoundObjectsToBooks(ArrayList<FoundObject> foundObjects){
        ArrayList<Book> booksTmp = new ArrayList<>();
        int counter = 0;
        for (FoundObject foundObject : foundObjects) {

            byte[] image = foundObject.getImage() != null ? foundObject.getImage() : new byte[0];
            String title = foundObject.getTitle() != null ? foundObject.getTitle() : "";
            String authors = foundObject.getAuthors() != null && !foundObject.getAuthors().isEmpty() ? String.join(", ", foundObject.getAuthors()) : "";
            String publishedDate = foundObject.getPublishedDate() != null ? foundObject.getPublishedDate() : "";
            String description = foundObject.getDescription() != null ? foundObject.getDescription() : "";
            String categories = foundObject.getCategories() != null && !foundObject.getCategories().isEmpty() ? String.join(", ", foundObject.getCategories()) : "";
            Boolean isInDatabase = foundObject.getIsInDatabase();

            Book book = new Book(counter, 0, 1, title, authors, image, description, categories, publishedDate, "Unread", isInDatabase);
            if (image.length == 0){
                BlobManager blobManager = new BlobManager(context, R.drawable.ic_book_start);
                blobManager.loadToBook(book);
            }
            booksTmp.add(book);
            counter++;
        }
        return booksTmp;
    }

    /**
     * Creates a new photo.
     * @param foundObjects List of found objects.
     * @return New photo.
     */
    public Photo createNewPhoto(ArrayList<FoundObject> foundObjects) {

        Integer bookNumber = foundObjects.size();

        byte[] image = foundObjects.get(0).getImage() != null ? foundObjects.get(0).getImage() : new byte[0];

        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = formatter.format(currentDate);

        Photo photo = new Photo(0, bookNumber, image, formattedDate);
        if (image.length == 0){
            BlobManager blobManager = new BlobManager(context, R.drawable.ic_book_start);
            blobManager.loadToPhoto(photo);
        }
        return photo;
    }

    /**
     * Logs the books and Photo.
     * @param books List of books.
     * @param photo Photo.
     */
    public void LogBooks(ArrayList<Book> books, Photo photo) {
        Log.d("MainFoundBooksActivity", "------------------------");
        Log.d("MainFoundBooksActivity", "Photo id: " + photo.getId());
        Log.d("MainFoundBooksActivity", "Photo number: " + photo.getPhotoNumber());
        Log.d("MainFoundBooksActivity", "Photo photo: " + photo.getPhoto().length);
        Log.d("MainFoundBooksActivity", "Photo date: " + photo.getDate());
        Log.d("MainFoundBooksActivity", "------------------------");
        for(Book book : books){
            Log.d("MainFoundBooksActivity", "------------------------");
            Log.d("MainFoundBooksActivity", "Book id: " + book.getId());
            Log.d("MainFoundBooksActivity", "Book photo id: " + book.getPhotoId());
            Log.d("MainFoundBooksActivity", "Book user id: " + book.getUserId());
            Log.d("MainFoundBooksActivity", "Book title: " + book.getTitle());
            Log.d("MainFoundBooksActivity", "Book author: " + book.getAuthor());
            Log.d("MainFoundBooksActivity", "Book photo: " + book.getPhoto().length);
            Log.d("MainFoundBooksActivity", "Book description: " + book.getDescription());
            Log.d("MainFoundBooksActivity", "Book genre: " + book.getGenre());
            Log.d("MainFoundBooksActivity", "Book date: " + book.getDate());
            Log.d("MainFoundBooksActivity", "Book status: " + book.getStatus());
            Log.d("MainFoundBooksActivity", "Book isAdded: " + book.getIsAdded());
        }
    }

    /**
     * Checks if a book is already in the database.
     * @param books List of books.
     */
    public void checkIsAddedBook(ArrayList<Book> books) {
        Cursor cursor_books = dbManager.getAllBooks();
        ArrayList<Book> booksInDatabase = new ArrayList<>();
        try {
            for (cursor_books.moveToFirst(); !cursor_books.isAfterLast(); cursor_books.moveToNext()) {
                Book bookInDatabase = new Book(cursor_books.getInt(0), cursor_books.getInt(1), cursor_books.getInt(2), cursor_books.getString(3), cursor_books.getString(4), cursor_books.getBlob(5), cursor_books.getString(6), cursor_books.getString(7), cursor_books.getString(8), cursor_books.getString(9), cursor_books.getInt(10) > 0);
                booksInDatabase.add(bookInDatabase);
            }
            cursor_books.close();
        } catch(Exception e) {
            Log.e("BookshelfViewModel", e.getMessage(), e);
        }
        for (Book book : books) {
            for (Book bookInDatabase : booksInDatabase) {
                if (book.getTitle().equals(bookInDatabase.getTitle()) && book.getAuthor().equals(bookInDatabase.getAuthor())) {
                    book.setIsAdded(true);
                    break;
                }
            }
        }
    }

    /**
     * Adds books to database.
     * @param books List of books.
     * @param photo Photo.
     */
    public void addBooksToDatabase(ArrayList<Book> books, Photo photo) {
        photo.setPhotoNumber(0);
        int counter = 0;
        for (Book book : books) {
            if (!book.getIsAdded()) {
                dbManager.insertBook(book);
                counter++;
            }
        }
        photo.setPhotoNumber(counter);
        dbManager.insertPhoto(photo);
    }
}
