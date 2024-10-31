package com.example.virtualbookshelf.viewmodel;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.Photo;
import com.example.virtualbookshelf.model.ml.FoundObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
     //     * Constructor for MainPhotoHistoryViewModel.
     //     * @param application The application context.
     //     */
    public MainFoundBooksViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

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
}
