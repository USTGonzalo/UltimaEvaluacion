package com.example.gestion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ConfigsDAO {

    private final DbHelper dbHelper;

    public ConfigsDAO(Context context) {
        dbHelper = new DbHelper(context);
    }

    // devuelve true cuando ya existe configuración
    public boolean existsConfig() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + ConfigsContract.ConfigsEntry.TABLE_NAME, null
        );

        boolean exists = false;

        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;
        }
        cursor.close();
        db.close();
        return exists;
    }

    // crea configuración por defecto si no existe nada
    public void createDefaultConfig() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ConfigsContract.ConfigsEntry.COLUMN_CURRENT_TYPE, "USDCLP");
        values.put(ConfigsContract.ConfigsEntry.COLUMN_BACKGROUND, "");
        values.put(ConfigsContract.ConfigsEntry.COLUMN_THEME, 0);

        db.insert(ConfigsContract.ConfigsEntry.TABLE_NAME, null, values);
        db.close();
    }
}
