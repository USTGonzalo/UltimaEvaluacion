package com.example.gestion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestion.cache.ConfigModel;

public class ConfigsDatabase {

    private final DbHelper dbHelper;

    public ConfigsDatabase(Context context) {
        dbHelper = new DbHelper(context);
    }

    // ==========================================
    // OBTENER CONFIGURACIÓN
    // ==========================================
    public ConfigModel getConfig() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + ConfigsContract.ConfigsEntry.TABLE_NAME + " LIMIT 1",
                null
        );

        if (cursor.moveToFirst()) {

            String type = cursor.getString(
                    cursor.getColumnIndexOrThrow(ConfigsContract.ConfigsEntry.COLUMN_CURRENT_TYPE)
            );

            String background = cursor.getString(
                    cursor.getColumnIndexOrThrow(ConfigsContract.ConfigsEntry.COLUMN_BACKGROUND)
            );

            int theme = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ConfigsContract.ConfigsEntry.COLUMN_THEME)
            );

            cursor.close();
            return new ConfigModel(type, background, theme);
        }

        cursor.close();
        return null;
    }

    // ==========================================
    // ACTUALIZAR CONFIGURACIÓN
    // ==========================================
    public void updateConfig(String type, String background, int theme) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ConfigsContract.ConfigsEntry.COLUMN_CURRENT_TYPE, type);
        cv.put(ConfigsContract.ConfigsEntry.COLUMN_BACKGROUND, background);
        cv.put(ConfigsContract.ConfigsEntry.COLUMN_THEME, theme);

        db.update(ConfigsContract.ConfigsEntry.TABLE_NAME, cv, null, null);
    }
}
