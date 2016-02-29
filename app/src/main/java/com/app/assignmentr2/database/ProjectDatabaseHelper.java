package com.app.assignmentr2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Govind on 28-02-2016.
 */
public class ProjectDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "word.db";
    private static final int DATABASE_VERSION = 1;

    public ProjectDatabaseHelper(Context context, CursorFactory cursorFactory) {
        super(context, DATABASE_NAME, cursorFactory, DATABASE_VERSION);
    }

    String create_word_table = "CREATE TABLE IF NOT EXISTS " + TableConstants.Tables.WORDS + " (" + TableConstants.WordsColumn.ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + TableConstants.WordsColumn.WID + " INTEGER UNIQUE," + TableConstants.WordsColumn.WORD
            + " TEXT," + TableConstants.WordsColumn.VARIANT + " INTEGER," + TableConstants.WordsColumn.MEANING + " BLOB,"
            + TableConstants.WordsColumn.IMAGE_URL + " TEXT," + TableConstants.WordsColumn.IMAGE_FEED + " BLOB," + TableConstants.WordsColumn.ASPECT_RATIO
            + " REAL  );";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_word_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}