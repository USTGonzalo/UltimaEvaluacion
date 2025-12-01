package com.example.gestion.cache;

public class Movements {

    public long id;
    public String mount;
    public String date;
    public boolean type;
    public int category;
    public String photo;

    // CONSTRUCTOR EXACTO QUE NECESITA TU DATABASE
    public Movements(long id, String mount, String date, boolean type, int category, String photo) {
        this.id = id;
        this.mount = mount;
        this.date = date;
        this.type = type;
        this.category = category;
        this.photo = photo;
    }

    // Constructor vacío (por si Android lo necesita)
    public Movements() {
    }

    // GETTERS
    public long getId() { return id; }
    public String getMount() { return mount; }
    public String getDate() { return date; }
    public boolean isType() { return type; }
    public int getCategory() { return category; }
    public String getPhoto() { return photo; }

    // SETTERS
    public void setId(long id) { this.id = id; }
    public void setMount(String mount) { this.mount = mount; }
    public void setDate(String date) { this.date = date; }
    public void setType(boolean type) { this.type = type; }
    public void setCategory(int category) { this.category = category; }
    public void setPhoto(String photo) { this.photo = photo; }
}
