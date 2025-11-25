package com.example.gestion.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "moneytrack.db";

    // tabla de las categorias
    private static final String SQL_CREATE_ENTRIES_CATEGORIES =
            "CREATE TABLE " + CategoriesContract.CategoriesEntry.TABLE_NAME + " (" +
                    CategoriesContract.CategoriesEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CategoriesContract.CategoriesEntry.COLUMN_NAME + " VARCHAR(64) NOT NULL," +
                    CategoriesContract.CategoriesEntry.COLUMN_DESCRIPTION + " TEXT" +
                    ");";

    // tabla de los movimientos
    private static final String SQL_CREATE_ENTRIES_MOVEMENTS =
            "CREATE TABLE " + MovementsContract.MovementsEntry.TABLE_NAME + " (" +
                    MovementsContract.MovementsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MovementsContract.MovementsEntry.COLUMN_MOUNT + " TEXT NOT NULL," +
                    MovementsContract.MovementsEntry.COLUMN_DATE + " DATE NOT NULL," +
                    MovementsContract.MovementsEntry.COLUMN_TYPE + " BOOLEAN NOT NULL," +
                    MovementsContract.MovementsEntry.COLUMN_CATEGORY + " INTEGER," +
                    MovementsContract.MovementsEntry.COLUMN_URLPHOTO + " TEXT," +
                    "FOREIGN KEY(" + MovementsContract.MovementsEntry.COLUMN_CATEGORY + ") REFERENCES " +
                    CategoriesContract.CategoriesEntry.TABLE_NAME + "(" +
                    CategoriesContract.CategoriesEntry.COLUMN_ID + ")" +
                    ");";

    // tabla de las configuraciones
    private static final String SQL_CREATE_ENTRIES_CONFIGS =
            "CREATE TABLE " + ConfigsContract.ConfigsEntry.TABLE_NAME + " (" +
                    ConfigsContract.ConfigsEntry.COLUMN_CURRENT_TYPE + " VARCHAR(16) NOT NULL DEFAULT 'USDCLP'," +
                    ConfigsContract.ConfigsEntry.COLUMN_BACKGROUND + " TEXT DEFAULT ''," +
                    ConfigsContract.ConfigsEntry.COLUMN_THEME + " BOOLEAN NOT NULL DEFAULT 0" +
                    ");";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_CATEGORIES);
        db.execSQL(SQL_CREATE_ENTRIES_MOVEMENTS);
        db.execSQL(SQL_CREATE_ENTRIES_CONFIGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovementsContract.MovementsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesContract.CategoriesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ConfigsContract.ConfigsEntry.TABLE_NAME);
        onCreate(db);
    }
}