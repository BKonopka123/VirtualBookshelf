package com.example.virtualbookshelf.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.User;

/**
 * DBHelper is a class that manages the creation of a database and its updating.
 * It extends SQLiteOpenHelper to handle the SQLite database.
 */
public class DBHelper extends SQLiteOpenHelper {

    /** Singleton instance of the DBHelper class. */
    private static DBHelper instance;

    /** Name of the database file */
    private static final String DATABASE_NAME = "bookshelf.db";
    /** Version of the database */
    private static final int DATABASE_VERSION = 1;
    /** Context of the application */
    private final Context context;

    /**
     * Private constructor to create a new instance of the DBHelper class.
     *
     * @param context Context of the application
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();
    }

    /**
     * Get the singleton instance of the DBHelper class.
     *
     * @param context Context of the application
     * @return Singleton instance of the DBHelper class
     */
    public static synchronized DBHelper getInstance(Context context){
        if(instance == null){
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Called when the database is created for the first time.
     * The function creates the required tables in the database and initializes the starting values, such as user or his initial profile picture and username.
     *
     * @param database SQLiteDatabase object representing the database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        // Enable foreign key constraints
        database.execSQL("PRAGMA foreign_keys=ON;");

        // SQL statements to create the tables
        String creteTableUser = "CREATE TABLE User (" +
                "User_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Username TEXT NOT NULL," +
                "Profile_photo BLOB NOT NULL," +
                "Books_number INTEGER NOT NULL," +
                "Books_read_number INTEGER NOT NULL," +
                "Books_unread_number INTEGER NOT NULL," +
                "Books_currently_number INTEGER NOT NULL," +
                "Books_queue_number INTEGER NOT NULL)";
        String creteTableBook = "CREATE TABLE Book (" +
                "Book_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Photo_id INTEGER NOT NULL," +
                "User_id INTEGER NOT NULL," +
                "Title TEXT NOT NULL," +
                "Author TEXT NOT NULL," +
                "Photo BLOB NOT NULL," +
                "Description TEXT NOT NULL," +
                "Genre TEXT NOT NULL," +
                "Date DATE NOT NULL," +
                "Status TEXT NOT NULL," +
                "Is_added BOOLEAN NOT NULL," +
                "FOREIGN KEY(Photo_id) REFERENCES Photo(Photo_id) ON DELETE CASCADE ON UPDATE NO ACTION," +
                "FOREIGN KEY(User_id) REFERENCES User(User_id) ON DELETE CASCADE ON UPDATE NO ACTION)";
        String creteTablePhoto = "CREATE TABLE Photo (" +
                "Photo_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Book_photo_number INTEGER NOT NULL," +
                "Books_photo BLOB," +
                "Date DATE NOT NULL)";
        try {
            database.execSQL(creteTableUser);
            database.execSQL(creteTableBook);
            database.execSQL(creteTablePhoto);
        } catch (Exception e) {
            Log.e("DBHelper", "Error creating tables: " + e.getMessage());
        }

        // Initialize the starting values
        User user = new User(1, "username", new byte[0], 0, 0, 0, 0, 0);
        BlobManager blobManager = new BlobManager(context, R.drawable.ic_user_start);
        blobManager.loadToUser(user);

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
            Log.e("DBHelper", "Error inserting values: " + e.getMessage());
        }
    }

    /**
     * Called when the database needs to be upgraded. Drops existing tables and recreates them.
     *
     * @param database The SQLite database instance.
     * @param oldVersion The old version of the database.
     * @param newVersion The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // Drop existing tables if they exist
        try {
            database.execSQL("DROP TABLE IF EXISTS User");
            database.execSQL("DROP TABLE IF EXISTS Book");
            database.execSQL("DROP TABLE IF EXISTS Photo");
        } catch (Exception e) {
            Log.e("DBHelper", "Error dropping tables: " + e.getMessage());
        }

        // Recreate the database
        onCreate(database);
    }
}
