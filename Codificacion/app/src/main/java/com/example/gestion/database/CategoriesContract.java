package com.example.gestion.database;

public class CategoriesContract {

    private CategoriesContract()
    {

    }

    public static class CategoriesEntry
    {
        public static final String TABLE_NAME = "Categories";
        public static final String COLUMN_ID = "ID_category";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_DESCRIPTION = "Description";
    }
}