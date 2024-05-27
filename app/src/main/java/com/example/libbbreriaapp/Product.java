package com.example.libbbreriaapp;

public class Product {
    private int id;
    private String type;
    private String imgurl;
    private String title;
    private String description;
    private double price;
    private int qty;

    public Product(int id, String type, String imgurl, String title, String description, double price, int qty) {
        this.id = id;
        this.type = type;
        this.imgurl = imgurl;
        this.title = title;
        this.description = description;
        this.price = price;
        this.qty = qty;
    }

    public double calcTot(){
        return this.price * this.qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                '}';
    }
}
