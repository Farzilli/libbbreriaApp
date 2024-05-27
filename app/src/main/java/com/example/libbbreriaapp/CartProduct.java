package com.example.libbbreriaapp;

public class CartProduct {
    private int id;
    private String imgurl;
    private String title;
    private double price;
    private int qty;

    public CartProduct(int id, String imgurl, String title, double price, int qty) {
        this.id = id;
        this.imgurl = imgurl;
        this.title = title;
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
        return "CartProduct{" +
                "id=" + id +
                ", imgurl='" + imgurl + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                '}';
    }
}
