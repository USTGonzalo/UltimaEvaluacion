package com.example.gestion.database;

public class MovementsContract {

    private MovementsContract()
    {

    }

    public static class MovementsEntry
    {
        public static final String TABLE_NAME = "Movements";
        public static final String COLUMN_ID = "ID_movement";
        public static final String COLUMN_MOUNT = "Mount";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_CATEGORY = "ID_category";
        public static final String COLUMN_URLPHOTO = "Photo";
    }
}