package com.example.foodapp.model;

public class PromoDish {

    private int dishImage;
    private String dishTitle;
    private String dishDescription;
    private String dishPrice;

    public PromoDish(int dishImage, String dishTitle, String dishDescription, String dishPrice) {
        this.dishImage = dishImage;
        this.dishTitle = dishTitle;
        this.dishDescription = dishDescription;
        this.dishPrice = dishPrice;
    }

    public int getDishImage() {
        return dishImage;
    }

    public void setDishImage(int dishImage) {
        this.dishImage = dishImage;
    }

    public String getDishTitle() {
        return dishTitle;
    }

    public void setDishTitle(String dishTitle) {
        this.dishTitle = dishTitle;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public String getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(String dishPrice) {
        this.dishPrice = dishPrice;
    }
}
