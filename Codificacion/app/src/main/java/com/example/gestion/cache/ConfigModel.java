package com.example.gestion.cache;

public class ConfigModel {
    public String type;        // tipo de moneda
    public String background;  // nombre ruta del fondo
    public int theme;          // 0 claro, 1 oscuro

    public ConfigModel(String type, String background, int theme) {
        this.type = type;
        this.background = background;
        this.theme = theme;
    }
}
