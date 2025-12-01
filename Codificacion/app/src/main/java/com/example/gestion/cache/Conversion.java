package com.example.gestion.cache;

import java.util.ArrayList;
import java.util.List;

public class Conversion {

    private String type;
    private double rate;

    // Constructor
    public Conversion(String type, double rate) {
        this.type = type;
        this.rate = rate;
    }

    // Getters y setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    // Lista estática que guardará todas las conversiones en memoria
    public static final List<Conversion> conversionList = new ArrayList<>();

    // Método auxiliar para obtener la tasa por tipo de moneda
    public static double getRateByType(String currencyType) {
        for (Conversion c : conversionList) {
            if (c.getType().equals(currencyType)) {
                return c.getRate();
            }
        }
        return 1.0; // Por defecto USD
    }

    // Método auxiliar para limpiar la lista (por si se recarga la API)
    public static void clearConversions() {
        conversionList.clear();
    }

    // Método auxiliar para agregar una nueva conversión
    public static void addConversion(String type, double rate) {
        conversionList.add(new Conversion(type, rate));
    }
}
