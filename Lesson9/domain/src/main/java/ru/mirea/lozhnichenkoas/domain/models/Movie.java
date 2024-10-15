package ru.mirea.lozhnichenkoas.domain.models;

public class Movie {
    private  int id;
    private  String name;

    public Movie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String GetName() {return name;}

    public int GetId() {return id;}
}
