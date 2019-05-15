package com.example.restservice_app;

public class Cart {

    private  int id;
    private int user_id;
    private int pizza_id;
    private String pizzaname;
    private String size;
    private String description;
    private int item;
    private Double total;
    private String img_url;
    private int cart_status;


    public Cart(int id, int user_id, int pizza_id, String pizzaname, String size, String description, int item, Double total, String img_url, int cart_status) {
        this.id = id;
        this.user_id = user_id;
        this.pizza_id = pizza_id;
        this.pizzaname = pizzaname;
        this.size = size;
        this.description = description;
        this.item = item;
        this.total = total;
        this.img_url = img_url;
        this.cart_status = cart_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPizza_id() {
        return pizza_id;
    }

    public void setPizza_id(int pizza_id) {
        this.pizza_id = pizza_id;
    }

    public String getPizzaname() {
        return pizzaname;
    }

    public void setPizzaname(String pizzaname) {
        this.pizzaname = pizzaname;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getCart_status() {
        return cart_status;
    }

    public void setCart_status(int cart_status) {
        this.cart_status = cart_status;
    }
}
