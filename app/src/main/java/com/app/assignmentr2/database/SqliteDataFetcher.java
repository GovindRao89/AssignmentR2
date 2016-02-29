package com.app.assignmentr2.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * Created by Govind on 28-02-2016.
 */
public class SqLiteDataFetcher {

    private ProjectDatabaseHelper mDatabaseHelper;

    public SqLiteDataFetcher(Context context) {
        SQLiteDatabase.CursorFactory cursorFactory = new SQLiteDatabase.CursorFactory() {
            @Override
            public Cursor newCursor(SQLiteDatabase db,
                                    SQLiteCursorDriver masterQuery, String editTable,
                                    SQLiteQuery query) {
                Log.d("Query", "Query: " + query);
                return new SQLiteCursor(masterQuery, editTable, query);
            }
        };
        mDatabaseHelper = new ProjectDatabaseHelper(context, cursorFactory);
    }

    public boolean isTableEmpty() {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TableConstants.Tables.WORDS;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        int iCount = cursor.getInt(0);
        if (cursor != null && iCount > 0) {
            cursor.close();
            return false;
        }
        return true;
    }

    public Cursor queryDatabase(int tableIdentifier, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        Cursor cursor = null;

        switch (tableIdentifier) {

            case TableConstants.TABLE_WORD:
                queryBuilder.setTables(TableConstants.Tables.WORDS);
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                Log.v("Content provider : ", DatabaseUtils.dumpCursorToString(cursor));
                break;

            default:
                throw new IllegalArgumentException("Unknown Constant : " + tableIdentifier);
        }
        return cursor;
    }

    public long insertRecord(int tableIdentifier, ContentValues values) {

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long id = 0;

        switch (tableIdentifier) {
            case TableConstants.TABLE_WORD:
                try {
                    id = db.insertWithOnConflict(TableConstants.Tables.WORDS, null, values, SQLiteDatabase.CONFLICT_FAIL);
                } catch (Exception e) {
                    String selection = null;
                    String selectColumnName = null;
                    if (values.containsKey(TableConstants.WordsColumn.WID)) {
                        selection = TableConstants.WordsColumn.WID + " = ?";
                        selectColumnName = TableConstants.WordsColumn.WID;
                    } else if (values.containsKey(TableConstants.WordsColumn.ID)) {
                        selection = TableConstants.WordsColumn.ID + " = ?";
                        selectColumnName = TableConstants.WordsColumn.ID;
                    }
                    String[] selectionArgs = {(values.get(selectColumnName).toString())};
                    updateRecord(TableConstants.TABLE_WORD, values, selection, selectionArgs);
                    return id;
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + tableIdentifier);

        }
        return id;
    }

    public int updateRecord(int tableIdentifier, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        int updated_rows = 0;
        switch (tableIdentifier) {
            case TableConstants.TABLE_WORD:
                try {
                    updated_rows = db.update(TableConstants.Tables.WORDS, values, selection, selectionArgs);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + tableIdentifier);
        }
        return updated_rows;
    }


    public int delete(int tableIdentifier, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        int deleted_rows = 0;
        switch (tableIdentifier) {
            case TableConstants.TABLE_WORD:
                deleted_rows = db.delete(TableConstants.Tables.WORDS, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + tableIdentifier);
        }
        return deleted_rows;
    }

}
