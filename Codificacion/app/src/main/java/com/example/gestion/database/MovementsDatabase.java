package com.example.gestion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestion.database.MovementsContract.MovementsEntry;

public class MovementsDatabase {

    private final DbHelper dbHelper;

    public MovementsDatabase(Context context) {
        dbHelper = new DbHelper(context);
    }

    // INSERTAR MOVIMIENTO
    public long insertMovement(String mount, String date, boolean type,
                               int idCategory, String photoUrl) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MovementsEntry.COLUMN_MOUNT, mount);
        values.put(MovementsEntry.COLUMN_DATE, date);
        values.put(MovementsEntry.COLUMN_TYPE, type ? 1 : 0);
        values.put(MovementsEntry.COLUMN_CATEGORY, idCategory);
        values.put(MovementsEntry.COLUMN_URLPHOTO, photoUrl);

        long newId = db.insert(MovementsEntry.TABLE_NAME, null, values);
        db.close();

        return newId;
    }

    // ACTUALIZAR MOVIMIENTO
    public int updateMovement(long id, String mount, String date,
                              boolean type, int idCategory, String photoUrl) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MovementsEntry.COLUMN_MOUNT, mount);
        values.put(MovementsEntry.COLUMN_DATE, date);
        values.put(MovementsEntry.COLUMN_TYPE, type ? 1 : 0);
        values.put(MovementsEntry.COLUMN_CATEGORY, idCategory);
        values.put(MovementsEntry.COLUMN_URLPHOTO, photoUrl);

        int rows = db.update(
                MovementsEntry.TABLE_NAME,
                values,
                MovementsEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();
        return rows;
    }

    // ELIMINAR MOVIMIENTO
    public int deleteMovement(long id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(
                MovementsEntry.TABLE_NAME,
                MovementsEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();
        return rows;
    }

    // OBTENER UN MOVIMIENTO POR ID
    public Cursor getMovement(long id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + MovementsEntry.TABLE_NAME +
                        " WHERE " + MovementsEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // OBTENER TODOS LOS MOVIMIENTOS
    public Cursor getAllMovements() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + MovementsEntry.TABLE_NAME +
                        " ORDER BY " + MovementsEntry.COLUMN_ID + " DESC",
                null
        );
    }
}
