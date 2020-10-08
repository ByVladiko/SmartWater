package com.example.smartwater.ui.cart;

import com.example.smartwater.model.Product;

class ShoppingCartItemsModel {

    private Product product;
    private int quantity;

    int getQuantity() {
        return quantity;
    }

    void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    Product getProduct() {
        return product;
    }

    void setProduct(Product product) {
        this.product = product;
    }
}
