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

    public void insertPhoto(Photo photo) {
        ContentValues values = new ContentValues();
        values.put("Book_photo_number", photo.getPhotoNumber());
        values.put("Books_photo", photo.getPhoto());
        values.put("Date", photo.getDate());

        database.insert("Photo", null, values);
    }

}
