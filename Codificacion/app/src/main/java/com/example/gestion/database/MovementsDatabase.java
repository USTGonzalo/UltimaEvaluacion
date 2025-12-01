package com.example.gestion.database;

import com.example.gestion.cache.Movements;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestion.database.MovementsContract.MovementsEntry;

import java.util.ArrayList;
import java.util.List;

public class MovementsDatabase {

    private final DbHelper dbHelper;

    public MovementsDatabase(Context context) {
        dbHelper = new DbHelper(context);
    }

    // ================================
    // INSERTAR MOVIMIENTO
    // ================================
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

    // ================================
    // ACTUALIZAR MOVIMIENTO
    // ================================
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

    // ================================
    // ELIMINAR MOVIMIENTO
    // ================================
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

    public List<String> getAllMovementIds() {
        List<String> ids = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + MovementsEntry.COLUMN_ID +
                        " FROM " + MovementsEntry.TABLE_NAME +
                        " ORDER BY " + MovementsEntry.COLUMN_ID + " DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ids;
    }

    // ================================
    // OBTENER MOVIMIENTO COMPLETO POR ID
    // ================================
    public Movements getMovementById(long id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + MovementsEntry.TABLE_NAME +
                        " WHERE " + MovementsEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        Movements m = null;

        if (cursor.moveToFirst()) {
            m = new Movements();
            m.id = cursor.getLong(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_ID));
            m.mount = cursor.getString(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_MOUNT));
            m.date = cursor.getString(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_DATE));
            m.type = cursor.getInt(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_TYPE)) == 1;
            m.category = cursor.getInt(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_CATEGORY));
            m.photo = cursor.getString(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_URLPHOTO));
        }

        cursor.close();
        db.close();
        return m;
    }

    //Obtener todos los movimientos
    public List<Movements> getAllMovementsList() {

        List<Movements> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + MovementsEntry.TABLE_NAME +
                        " ORDER BY " + MovementsEntry.COLUMN_ID + " DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Movements m = new Movements();
                m.id = cursor.getLong(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_ID));
                m.mount = cursor.getString(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_MOUNT));
                m.date = cursor.getString(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_DATE));
                m.type = cursor.getInt(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_TYPE)) == 1;
                m.category = cursor.getInt(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_CATEGORY));
                m.photo = cursor.getString(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_URLPHOTO));

                list.add(m);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public List<Movements> getMovementsByDateRange(String startDate) {

        List<Movements> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query;
        query = "SELECT * FROM " + MovementsEntry.TABLE_NAME +
                " WHERE " + MovementsEntry.COLUMN_DATE + " = ?" +
                " ORDER BY " + MovementsEntry.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{startDate});

        if (cursor.moveToFirst()) {
            do {
                Movements m = new Movements();
                m.id = cursor.getLong(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_ID));
                m.mount = cursor.getString(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_MOUNT));
                m.date = cursor.getString(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_DATE));
                m.type = cursor.getInt(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_TYPE)) == 1;
                m.category = cursor.getInt(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_CATEGORY));
                m.photo = cursor.getString(cursor.getColumnIndexOrThrow(MovementsEntry.COLUMN_URLPHOTO));
                list.add(m);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ============================================================
    // GASTOS DEL DÍA (dd/MM/yyyy)
    // ============================================================
    public double getGastosHoy(String today) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(CAST(" + MovementsEntry.COLUMN_MOUNT + " AS REAL)) FROM "
                        + MovementsEntry.TABLE_NAME +
                        " WHERE " + MovementsEntry.COLUMN_TYPE + " = 0 AND "
                        + MovementsEntry.COLUMN_DATE + " = ?",
                new String[]{today.replace("-", "/")} // ← ADAPTADO
        );

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.isNull(0) ? 0 : cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }


    // ============================================================
    // GASTOS DEL MES (dd/MM/yyyy) → busca "/MM/"
    // ============================================================
    public double getGastosMes(String monthTwoDigits) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String pattern = "%/" + monthTwoDigits + "/%"; // ejemplo: %/11/%

        Cursor cursor = db.rawQuery(
                "SELECT SUM(CAST(" + MovementsEntry.COLUMN_MOUNT + " AS REAL)) FROM " +
                        MovementsEntry.TABLE_NAME +
                        " WHERE " + MovementsEntry.COLUMN_TYPE + " = 0 AND " +
                        MovementsEntry.COLUMN_DATE + " LIKE ?",
                new String[]{pattern}
        );

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.isNull(0) ? 0 : cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }

    // ============================================================
    // INGRESOS DEL DÍA (dd/MM/yyyy)
    // ============================================================
    public double getIngresosHoy(String today) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(CAST(" + MovementsEntry.COLUMN_MOUNT + " AS REAL)) FROM "
                        + MovementsEntry.TABLE_NAME +
                        " WHERE " + MovementsEntry.COLUMN_TYPE + " = 1 AND "
                        + MovementsEntry.COLUMN_DATE + " = ?",
                new String[]{today.replace("-", "/")} // ← ADAPTADO
        );

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.isNull(0) ? 0 : cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }


    // ============================================================
    // INGRESOS DEL MES (dd/MM/yyyy) → busca "/MM/"
    // ============================================================
    public double getIngresosMes(String monthTwoDigits) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String pattern = "%/" + monthTwoDigits + "/%";

        Cursor cursor = db.rawQuery(
                "SELECT SUM(CAST(" + MovementsEntry.COLUMN_MOUNT + " AS REAL)) FROM " +
                        MovementsEntry.TABLE_NAME +
                        " WHERE " + MovementsEntry.COLUMN_TYPE + " = 1 AND " +
                        MovementsEntry.COLUMN_DATE + " LIKE ?",
                new String[]{pattern}
        );

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.isNull(0) ? 0 : cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }
}
