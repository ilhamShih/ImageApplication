package com.example.imageapplication.basket;

public class Basket {

    private String product_id;
    private String product_image;

    public Basket(String menuId, String menuName ) {
        product_id = menuId;
        product_image = menuName;
    }
    public String geId() {
        return product_id;
    }
    public void setId(String menuId) {
        product_id = menuId;
    }
    public String getName() {
        return product_image;
    }
    public void setName(String menuName) {
        product_image = menuName;
    }


}
