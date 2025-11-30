package com.example.gestion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestion.cache.Categories;

import java.util.ArrayList;
import java.util.List;

public class CategoriesDatabase {

    private DbHelper dbHelper;

    public CategoriesDatabase(Context context) {
        dbHelper = new DbHelper(context);
    }

    // INSERTAR CATEGORÍA
    public long insertCategory(String name, String description) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CategoriesContract.CategoriesEntry.COLUMN_NAME, name);
        values.put(CategoriesContract.CategoriesEntry.COLUMN_DESCRIPTION, description);

        long id = db.insert(CategoriesContract.CategoriesEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // OBTENER UNA CATEGORÍA POR ID
    public Categories getCategoryById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                CategoriesContract.CategoriesEntry.TABLE_NAME,
                null,
                CategoriesContract.CategoriesEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        Categories category = null;

        if (cursor.moveToFirst()) {
            category = new Categories(
                    cursor.getInt(cursor.getColumnIndexOrThrow(CategoriesContract.CategoriesEntry.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CategoriesContract.CategoriesEntry.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CategoriesContract.CategoriesEntry.COLUMN_DESCRIPTION))
            );
        }

        cursor.close();
        db.close();
        return category;
    }

    // OBTENER TODAS LAS CATEGORÍAS
    public List<Categories> getAllCategories() {
        List<Categories> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + CategoriesContract.CategoriesEntry.TABLE_NAME +
                        " ORDER BY " + CategoriesContract.CategoriesEntry.COLUMN_NAME + " ASC",
                null
        );

        while (cursor.moveToNext()) {
            list.add(new Categories(
                    cursor.getInt(cursor.getColumnIndexOrThrow(CategoriesContract.CategoriesEntry.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CategoriesContract.CategoriesEntry.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CategoriesContract.CategoriesEntry.COLUMN_DESCRIPTION))
            ));
        }

        cursor.close();
        db.close();
        return list;
    }

    // ACTUALIZAR CATEGORÍA
    public int updateCategory(int id, String name, String description) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CategoriesContract.CategoriesEntry.COLUMN_NAME, name);
        values.put(CategoriesContract.CategoriesEntry.COLUMN_DESCRIPTION, description);

        int rows = db.update(
                CategoriesContract.CategoriesEntry.TABLE_NAME,
                values,
                CategoriesContract.CategoriesEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
        return rows;
    }

    // BORRAR CATEGORÍA
    public int deleteCategory(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(
                CategoriesContract.CategoriesEntry.TABLE_NAME,
                CategoriesContract.CategoriesEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
        return rows;
    }
}
