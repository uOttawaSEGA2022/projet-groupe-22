package com.example.mealer.model;


public class CartModel  {
    private String key, name, chefUid, price;
    private  int quantity;
    private float totalPrice;
    private boolean status ;

    public CartModel() {
        this.status=false;
    }

    public void setKey(String key) {this.key = key;}
    public void setName(String name) {this.name = name;}
    public void setChefName(String chefUid) {this.chefUid = chefUid;}
    public void setPrice(String price) {this.price = price;}
    public  void setQuantity(int quantity) {this.quantity = quantity;}
    public void setTotalPrice(float totalPrice) {this.totalPrice = totalPrice;}
    public void setStatus(boolean status) {this.status = status;}


    public String getKey() {return key;}
    public String getName() {return name;}
    public String getChefName() {return chefUid;}
    public String getPrice() {return price;}
    public int getQuantity() {return quantity;}
    public float getTotalPrice() {return totalPrice;}
    public boolean isStatus() {return status;}

}
