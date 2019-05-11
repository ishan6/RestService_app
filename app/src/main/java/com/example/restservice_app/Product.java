package com.example.restservice_app;

public class Product {
    private int id;
    private String name;
    private String description;
    private Double price;
    private String imgurl;
    private Double medium_price;
    private Double large_price;

    public Product() {

    }

    public Product(int id, String name, String description, Double price, String imgurl, Double medium_price, Double large_price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgurl = imgurl;
        this.medium_price = medium_price;
        this.large_price = large_price;
    }

    public Double getMedium_price() {
        return medium_price;
    }

    public void setMedium_price(Double medium_price) {
        this.medium_price = medium_price;
    }

    public Double getLarge_price() {
        return large_price;
    }

    public void setLarge_price(Double large_price) {
        this.large_price = large_price;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}

