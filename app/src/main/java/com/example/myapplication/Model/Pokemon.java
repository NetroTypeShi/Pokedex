package com.example.myapplication.Model;

public class Pokemon {
    private int id;
    private String name;
    private int height;
    private int weight;
    private String imageUrl;

    public Pokemon(int id, String name, int height, int weight, String imageUrl) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getHeight() { return height; }
    public int getWeight() { return weight; }
    public String getImageUrl() { return imageUrl; }
}
