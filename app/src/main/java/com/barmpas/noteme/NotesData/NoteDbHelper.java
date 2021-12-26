package com.barmpas.noteme.NotesData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The helper function class for the local SQLite Database class where the user stores the notes.
 * Here we define the variables and the acceptable form of the entries in the database.
 * @author Konstantinos Barmpas.
 */
public class NoteDbHelper extends SQLiteOpenHelper {

    /**
     * The database's name
     */
    private static final String DATABASE_NAME = "noteme.db";
    /**
     * The version of the SQL Database
     */
    private static final int DATABASE_VERSION = 1;


    /**
     * The Helper of the SQL Database
     */
    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate method of the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + NoteContract.UserEntry.TABLE_NAME + " ("
                + NoteContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NoteContract.UserEntry.COLUMN_DATE + " TEXT NOT NULL , "
                + NoteContract.UserEntry.COLUMN_NOTE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * Upgrade versions of the databse
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Still at version 1, no upgrade required
    }
}
