package com.example.myfinance.model;

public class Category {
    private int id;
    private String name;

    public Category() { }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters & setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // 🔥 Spinner এ শুধু name দেখাবে
    @Override
    public String toString() {
        return name;
    }
}
