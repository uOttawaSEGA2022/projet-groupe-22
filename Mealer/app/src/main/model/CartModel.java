package com.example.mealer.model;


public class CartModel  {
    private String key, name, chefName, price;
    private  int quantity;
    private float totalPrice;

    public CartModel() {}

    public CartModel(String key,String name,String chefName,String price,int quantity,float totalPrice){
        this.key=key;
        this.name=name;
        this.chefName=chefName;
        this.price=price;
        this.quantity=quantity;
        this.totalPrice=totalPrice;
    }

    public void setKey(String key) {this.key = key;}
    public void setName(String name) {this.name = name;}
    public void setChefName(String chefName) {this.chefName = chefName;}
    public void setPrice(String price) {this.price = price;}
    public  void setQuantity(int quantity) {this.quantity = quantity;}
    public void setTotalPrice(float totalPrice) {this.totalPrice = totalPrice;}

    public String getKey() {return key;}
    public String getName() {return name;}
    public String getChefName() {return chefName;}
    public String getPrice() {return price;}
    public int getQuantity() {return quantity;}
    public float getTotalPrice() {return totalPrice;}

}