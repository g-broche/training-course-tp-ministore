package com.gbroche.model;

public class Product {

    private final int id;
    private final String category;
    private final String title;
    private final String actor;
    private final Double price;
    private final int special;

    public Product(int id, String category, String title, String actor, Double price, int special) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.actor = actor;
        this.price = price;
        this.special = special;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getActor() {
        return actor;
    }

    public Double getPrice() {
        return price;
    }

    public int getSpecial() {
        return special;
    }
}
