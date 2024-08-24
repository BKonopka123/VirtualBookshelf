package com.example.virtualbookshelf.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.Photo;
import com.example.virtualbookshelf.model.User;

public class DBManager {
    private final DBHelper dbHelper;
    private SQLiteDatabase database;

    public DBManager(Context context) {
        dbHelper = DBHelper.getInstance(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void close() {
        dbHelper.close();
    }

    public void insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put("Username", user.getUsername());
        values.put("Profile_photo", user.getProfilePhoto());
        values.put("Books_number", user.getBooksNumber());
        values.put("Books_read_number", user.getBooksReadNumber());
        values.put("Books_unread_number", user.getBooksUnreadNumber());
        values.put("Books_currently_number", user.getBooksCurrentlyNumber());
        values.put("Books_queue_number", user.getBooksQueueNumber());

        database.insert("User", null, values);
    }

    public Cursor getAllUsers() {
        String[] columns = {"User_id", "Username", "Profile_photo", "Books_number", "Books_read_number", "Books_unread_number", "Books_currently_number", "Books_queue_number"};
        return database.query("User", columns, null, null, null, null, null);
    }

    public void updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put("Username", user.getUsername());
        values.put("Profile_photo", user.getProfilePhoto());
        values.put("Books_number", user.getBooksNumber());
        values.put("Books_read_number", user.getBooksReadNumber());
        values.put("Books_unread_number", user.getBooksUnreadNumber());
        values.put("Books_currently_number", user.getBooksCurrentlyNumber());
        values.put("Books_queue_number", user.getBooksQueueNumber());

        database.update("User", values, "User_id = ?", new String[]{String.valueOf(user.getId())});
    }

    public void deleteUser(User user) {
        database.delete("User", "User_id = ?", new String[]{String.valueOf(user.getId())});
    }

    public void insertBook(Book book) {
        ContentValues values = new ContentValues();
        values.put("Photo_id", book.getPhotoId());
        values.put("User_id", book.getUserId());
        values.put("Title", book.getTitle());
        values.put("Author", book.getAuthor());
        values.put("Photo", book.getPhoto());
        values.put("Description", book.getDescription());
        values.put("Genre", book.getGenre());
        values.put("Date", book.getDate());
        values.put("Link", book.getLink());
        values.put("Status", book.getStatus());
        values.put("Edit_information", book.getEditInformation());
        values.put("Is_added", book.getIsAdded());

        database.insert("Book", null, values);
    }

    public Cursor getAllBooks() {
        String[] columns = {"Book_id", "Photo_id", "User_id", "Title", "Author", "Photo", "Description", "Genre", "Date", "Link", "Status", "Edit_information", "Is_added"};
        return database.query("Book", columns, null, null, null, null, null);
    }

    public void updateBook(Book book) {
        ContentValues values = new ContentValues();
        values.put("Photo_id", book.getPhotoId());
        values.put("User_id", book.getUserId());
        values.put("Title", book.getTitle());
        values.put("Author", book.getAuthor());
        values.put("Photo", book.getPhoto());
        values.put("Description", book.getDescription());
        values.put("Genre", book.getGenre());
        values.put("Date", book.getDate());
        values.put("Link", book.getLink());
        values.put("Status", book.getStatus());
        values.put("Edit_information", book.getEditInformation());
        values.put("Is_added", book.getIsAdded());

        database.update("Book", values, "Book_id = ?", new String[]{String.valueOf(book.getId())});
    }

    public void deleteBook(Book book) {
        database.delete("Book", "Book_id = ?", new String[]{String.valueOf(book.getId())});
    }

    public void insertPhoto(Photo photo) {
        ContentValues values = new ContentValues();
        values.put("Book_photo_number", photo.getPhotoNumber());
        values.put("Books_photo", photo.getPhoto());
        values.put("Date", photo.getDate());

        database.insert("Photo", null, values);
    }

    public Cursor getAllPhotos() {
        String[] columns = {"Photo_id", "Book_photo_number", "Books_photo", "Date"};
        return database.query("Photo", columns, null, null, null, null, null);
    }

    public void updatePhoto(Photo photo) {
        ContentValues values = new ContentValues();
        values.put("Book_photo_number", photo.getPhotoNumber());
        values.put("Books_photo", photo.getPhoto());
        values.put("Date", photo.getDate());

        database.update("Photo", values, "Photo_id = ?", new String[]{String.valueOf(photo.getId())});
    }

    public void deletePhoto(Photo photo) {
        database.delete("Photo", "Photo_id = ?", new String[]{String.valueOf(photo.getId())});
    }

    public void MockData() {
        Cursor cursor = database.rawQuery("SELECT * FROM User LIMIT 1", null);
        if (cursor.getCount() == 0) {
            User user = new User(1, "user1", new byte[0], 0, 0, 0, 0, 0);
            insertUser(user);
            Photo photo1 = new Photo(1, 3, new byte[0], "2023-05-01");
            insertPhoto(photo1);
            Photo photo2 = new Photo(2, 2, new byte[0], "2023-05-02");
            insertPhoto(photo2);
            Book book1 = new Book(1, 1, 1, "Book 1", "Author 1", new byte[0], "Description 1", "Genre 1", "2023-05-01", "Link 1", "Status 1", "aaa", true);
            insertBook(book1);
            Book book2 = new Book(2, 1, 1, "Book 2", "Author 2", new byte[0], "Description 2", "Genre 2", "2023-05-02", "Link 2", "Status 2", "bbb", true);
            insertBook(book2);
            Book book3 = new Book(3, 1, 1, "Book 3", "Author 3", new byte[0], "Description 3", "Genre 3", "2023-05-03", "Link 3", "Status 3", "ccc", true);
            insertBook(book3);
            Book book4 = new Book(4, 2, 1, "Book 4", "Author 4", new byte[0], "Description 4", "Genre 4", "2023-05-04", "Link 4", "Status 4", "ddd", true);
            insertBook(book4);
            Book book5 = new Book(5, 2, 1, "Book 5", "Author 5", new byte[0], "Description 5", "Genre 5", "2023-05-05", "Link 5", "Status 5", "eee", true);
            insertBook(book5);
        }
        cursor.close();
    }
}
