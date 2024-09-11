package com.example.virtualbookshelf.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.virtualbookshelf.R;
import com.example.virtualbookshelf.model.BlobManager;
import com.example.virtualbookshelf.model.User;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    private static final String DATABASE_NAME = "bookshelf.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();
    }

    public static synchronized DBHelper getInstance(Context context){
        if(instance == null){
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("PRAGMA foreign_keys=ON;");

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
                "Link TEXT NOT NULL," +
                "Status TEXT NOT NULL," +
                "Edit_information TEXT NOT NULL," +
                "Is_added BOOLEAN NOT NULL," +
                "FOREIGN KEY(Photo_id) REFERENCES Photo(Photo_id) ON DELETE CASCADE ON UPDATE NO ACTION," +
                "FOREIGN KEY(User_id) REFERENCES User(User_id) ON DELETE CASCADE ON UPDATE NO ACTION)";
        String creteTablePhoto = "CREATE TABLE Photo (" +
                "Photo_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Book_photo_number INTEGER NOT NULL," +
                "Books_photo BLOB," +
                "Date DATE NOT NULL)";
        database.execSQL(creteTableUser);
        database.execSQL(creteTableBook);
        database.execSQL(creteTablePhoto);

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

        database.insert("User", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS User");
        database.execSQL("DROP TABLE IF EXISTS Book");
        database.execSQL("DROP TABLE IF EXISTS Photo");
        onCreate(database);
    }
}
