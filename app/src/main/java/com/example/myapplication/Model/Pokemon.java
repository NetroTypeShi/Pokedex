package com.example.myapplication.Model;

public class Pokemon {
    private int id;
    private String name;
    private int height;
    private int weight;
    private String imageUrl;
    private String description;

    // Constructor antiguo mantenido si lo usan en otras partes
    public Pokemon(int id, String name, int height, int weight, String imageUrl) {
        this(id, name, height, weight, imageUrl, null);
    }

    // Nuevo constructor con description
    public Pokemon(int id, String name, int height, int weight, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getHeight() { return height; }
    public int getWeight() { return weight; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
}
