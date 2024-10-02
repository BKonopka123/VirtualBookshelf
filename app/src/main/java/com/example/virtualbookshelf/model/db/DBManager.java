package com.example.virtualbookshelf.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.Book;
import com.example.virtualbookshelf.model.Photo;
import com.example.virtualbookshelf.model.User;

/**
 * DBManager class provides various methods to interact with the database,
 * including CRUD operations for User, Book, and Photo entities.
 * It uses an instance of DBHelper to perform these operations on the SQLite database.
 */
public class DBManager {

    /** Instance of DBHelper to manage the database connection */
    private final DBHelper dbHelper;

    /** SQLite database instance. */
    private SQLiteDatabase database;

    /** Context of the application. */
    private final Context context;

    /**
     * Private constructor to create a new instance of the DBManager class.
     *
     * @param context Context of the application
     */
    public DBManager(Context context) {
        dbHelper = DBHelper.getInstance(context);
        this.context = context.getApplicationContext();
    }

    /**
     * Open the database connection.
     */
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Reset the database by dropping and recreating the tables.
     */
    public void reset() { dbHelper.onUpgrade(database, 1, 1); }

    /**
     * Insert a new user into the database.
     *
     * @param user User object to be inserted
     */
    public void insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put("Username", user.getUsername());
        values.put("Profile_photo", user.getProfilePhoto());
        values.put("Books_number", user.getBooksNumber());
        values.put("Books_read_number", user.getBooksReadNumber());
        values.put("Books_unread_number", user.getBooksUnreadNumber());
        values.put("Books_currently_number", user.getBooksCurrentlyNumber());
        values.put("Books_queue_number", user.getBooksQueueNumber());

        try {
            database.insert("User", null, values);
        } catch (Exception e) {
            Log.e("DBManager", "Error inserting user: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve all users from the database.
     *
     * @return Cursor object containing the result set
     */
    public Cursor getAllUsers() {
        String[] columns = {"User_id", "Username", "Profile_photo", "Books_number", "Books_read_number", "Books_unread_number", "Books_currently_number", "Books_queue_number"};
        try {
            return database.query("User", columns, null, null, null, null, null);
        } catch (Exception e) {
            Log.e("DBManager", "Error getting all users: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Retrieve a user by their ID from the database.
     *
     * @param userId ID of the user to retrieve
     * @return Cursor object containing the result set
     */
    public Cursor getUserById(int userId) {
        String[] columns = {"User_id", "Username", "Profile_photo", "Books_number", "Books_read_number", "Books_unread_number", "Books_currently_number", "Books_queue_number"};
        try {
            return database.query("User", columns, "User_id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        } catch (Exception e) {
            Log.e("DBManager", "Error getting user by id - " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Update an existing user in the database.
     *
     * @param user User object to be updated
     */
    public void updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put("Username", user.getUsername());
        values.put("Profile_photo", user.getProfilePhoto());
        values.put("Books_number", user.getBooksNumber());
        values.put("Books_read_number", user.getBooksReadNumber());
        values.put("Books_unread_number", user.getBooksUnreadNumber());
        values.put("Books_currently_number", user.getBooksCurrentlyNumber());
        values.put("Books_queue_number", user.getBooksQueueNumber());

        try {
            database.update("User", values, "User_id = ?", new String[]{String.valueOf(user.getId())});
        } catch (Exception e) {
            Log.e("DBManager", "Error updating user - " + e.getMessage(), e);
        }
    }

    /**
     * Delete a user from the database.
     *
     * @param user User object to be deleted
     */
    public void deleteUser(User user) {
        try {
            database.delete("User", "User_id = ?", new String[]{String.valueOf(user.getId())});
        } catch (Exception e) {
            Log.e("DBManager", "Error deleting user - " + e.getMessage(), e);
        }
    }

    /** Insert a new book into the database.
     *
     * @param book Book object to be inserted
     */
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

        try {
            database.insert("Book", null, values);
        } catch (Exception e) {
            Log.e("DBManager", "Error inserting book - " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve all books from the database.
     *
     * @return Cursor object containing the result set
     */
    public Cursor getAllBooks() {
        String[] columns = {"Book_id", "Photo_id", "User_id", "Title", "Author", "Photo", "Description", "Genre", "Date", "Link", "Status", "Edit_information", "Is_added"};
        try {
            return database.query("Book", columns, null, null, null, null, null);
        } catch (Exception e) {
            Log.e("DBManager", "Error getting all books - " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Retrieve a book by its ID from the database.
     *
     * @param bookId ID of the book to retrieve
     * @return Cursor object containing the result set
     */
    public Cursor getBookById(int bookId) {
        String[] columns = {"Book_id", "Photo_id", "User_id", "Title", "Author", "Photo", "Description", "Genre", "Date", "Link", "Status", "Edit_information", "Is_added"};
        try {
            return database.query("Book", columns, "Book_id = ?", new String[]{String.valueOf(bookId)}, null, null, null);
        } catch (Exception e) {
            Log.e("DBManager", "Error getting book by id - " + e.getMessage(), e);
        return null;
        }
    }

    /**
     * Update an existing book in the database.
     *
     * @param book Book object to be updated
     */
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

        try {
            database.update("Book", values, "Book_id = ?", new String[]{String.valueOf(book.getId())});
        } catch (Exception e) {
            Log.e("DBManager", "Error updating book - " + e.getMessage(), e);
        }
    }

    /**
     * Delete a book from the database.
     *
     * @param book Book object to be deleted
     */
    public void deleteBook(Book book) {
        try {
            database.delete("Book", "Book_id = ?", new String[]{String.valueOf(book.getId())});
        } catch (Exception e) {
            Log.e("DBManager", "Error deleting book - " + e.getMessage(), e);
        }
    }

    /**
     * Insert a new photo into the database.
     *
     * @param photo Photo object to be inserted
     */
    public void insertPhoto(Photo photo) {
        ContentValues values = new ContentValues();
        values.put("Book_photo_number", photo.getPhotoNumber());
        values.put("Books_photo", photo.getPhoto());
        values.put("Date", photo.getDate());

        try {
            database.insert("Photo", null, values);
        } catch (Exception e) {
            Log.e("DBManager", "Error inserting photo - " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve all photos from the database.
     *
     * @return Cursor object containing the result set
     */
    public Cursor getAllPhotos() {
        String[] columns = {"Photo_id", "Book_photo_number", "Books_photo", "Date"};
        try {
            return database.query("Photo", columns, null, null, null, null, null);
        } catch (Exception e) {
            Log.e("DBManager", "Error getting all photos - " + e.getMessage(), e);
        return null;
        }
    }

    /**
     * Retrieve a photo by its ID from the database.
     *
     * @param photoId ID of the photo to retrieve
     * @return Cursor object containing the result set
     */
    public Cursor getPhotoById(int photoId) {
        String[] columns = {"Photo_id", "Book_photo_number", "Books_photo", "Date"};
        try {
            return database.query("Photo", columns, "Photo_id = ?", new String[]{String.valueOf(photoId)}, null, null, null);
        } catch (Exception e) {
            Log.e("DBManager", "Error getting photo by id - " + e.getMessage(), e);
        return null;
        }
    }

    /**
     * Update an existing photo in the database.
     *
     * @param photo Photo object to be updated
     */
    public void updatePhoto(Photo photo) {
        ContentValues values = new ContentValues();
        values.put("Book_photo_number", photo.getPhotoNumber());
        values.put("Books_photo", photo.getPhoto());
        values.put("Date", photo.getDate());

        try {
            database.update("Photo", values, "Photo_id = ?", new String[]{String.valueOf(photo.getId())});
        } catch (Exception e) {
            Log.e("DBManager", "Error updating photo - " + e.getMessage(), e);
        }
    }

    /**
     * Delete a photo from the database.
     *
     * @param photo Photo object to be deleted
     */
    public void deletePhoto(Photo photo) {
        try {
            database.delete("Photo", "Photo_id = ?", new String[]{String.valueOf(photo.getId())});
        } catch (Exception e) {
            Log.e("DBManager", "Error deleting photo - " + e.getMessage(), e);
        }
    }

    /**
     * Mock data into the database.
     */
    public void mockData() {
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM Book LIMIT 1", null);
            if (cursor.getCount() == 0) {

                Cursor cursor_user = getUserById(1);
                cursor_user.moveToFirst();
                User user = new User(1, "username", new byte[0], 0, 0, 0, 0, 0);
                user.setId(cursor_user.getInt(0));
                user.setUsername(cursor_user.getString(1));
                user.setProfilePhoto(cursor_user.getBlob(2));
                user.setBooksNumber(5);
                user.setBooksReadNumber(2);
                user.setBooksUnreadNumber(1);
                user.setBooksCurrentlyNumber(1);
                user.setBooksQueueNumber(1);
                updateUser(user);
                cursor_user.close();

                BlobManager blobManager = new BlobManager(context, R.drawable.ic_book_start);

                Photo photo1 = new Photo(1, 3, new byte[0], "2024-08-25");
                blobManager.loadToPhoto(photo1);
                insertPhoto(photo1);
                Photo photo2 = new Photo(2, 2, new byte[0], "2024-08-25");
                blobManager.loadToPhoto(photo2);
                insertPhoto(photo2);
                Book book1 = new Book(1, 1, 1, "Book 1", "Author 1", new byte[0], "Description 1", "Genre 1", "2023-05-01", "Link 1", "Read", "aaa", true);
                blobManager.loadToBook(book1);
                insertBook(book1);
                Book book2 = new Book(2, 1, 1, "Book 2", "Author 2", new byte[0], "Description 2", "Genre 1", "2023-05-02", "Link 2", "Read", "bbb", false);
                blobManager.loadToBook(book2);
                insertBook(book2);
                Book book3 = new Book(3, 1, 1, "Book 3", "Author 3", new byte[0], "Description 3", "Genre 2", "2023-05-03", "Link 3", "Unread", "ccc", true);
                blobManager.loadToBook(book3);
                insertBook(book3);
                Book book4 = new Book(4, 2, 1, "Book 4", "Author 4", new byte[0], "Description 4", "Genre 2", "2023-05-04", "Link 4", "Currently", "ddd", true);
                blobManager.loadToBook(book4);
                insertBook(book4);
                Book book5 = new Book(5, 2, 1, "Book 5", "Author 5", new byte[0], "Description 5", "Genre 3", "2023-05-05", "Link 5", "Queue", "eee", true);
                blobManager.loadToBook(book5);
                insertBook(book5);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("DBManager", "Error mocking data - " + e.getMessage(), e);
        }
    }
}
