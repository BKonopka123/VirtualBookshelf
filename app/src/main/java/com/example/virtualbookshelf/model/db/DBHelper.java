package com.example.virtualbookshelf.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    private static final String DATABASE_NAME = "bookshelf.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context){
        if(instance == null){
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String creteTableUser = "CREATE TABLE User (" +
                "User_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Username TEXT NOT NULL," +
                "Prfile_photo BLOB NOT NULL," +
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
                "Description CLOB NOT NULL," +
                "Genre TEXT NOT NULL," +
                "Date DATE NOT NULL," +
                "Link TEXT NOT NULL," +
                "Status TEXT NOT NULL," +
                "Edit_information CLOB NOT NULL," +
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS User");
        database.execSQL("DROP TABLE IF EXISTS Book");
        database.execSQL("DROP TABLE IF EXISTS Photo");
        onCreate(database);
    }
}
