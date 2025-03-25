package com.example.main.models;

import com.example.main.models.Category;

public class Service {
    public String Id;
    public String Image;
    public String Name;
    public String Description;
    public int Price;
    public boolean Active;
    public Category Category;

    public Service(String id, String image, String name, String description, int price, boolean active, Category category) {
        Id = id;
        Image = image;
        Name = name;
        Description = description;
        Price = price;
        Active = active;
        Category = category;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public Category getCategory() {
        return Category;
    }

    public void setCategory(Category category) {
        Category = category;
    }
}



