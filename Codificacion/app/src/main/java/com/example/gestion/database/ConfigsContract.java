package com.example.gestion.database;

public class ConfigsContract {

    private ConfigsContract() { }

    public static class ConfigsEntry {
        public static final String TABLE_NAME = "Configs";
        public static final String COLUMN_CURRENT_TYPE = "CurrentType";
        public static final String COLUMN_BACKGROUND = "Background";
        public static final String COLUMN_THEME = "Theme";
    }
}