package com.example.smartwater.ui.cart;

import com.example.smartwater.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingCartHelper {

    private static ShoppingCart cartEntity = new ShoppingCart();

    public static ShoppingCart getCart() {
        if (cartEntity == null) {
            cartEntity = new ShoppingCart();
        }
        return cartEntity;
    }

    public static List<ShoppingCartItemsModel> getCartItems() {
        List<ShoppingCartItemsModel> cartItems = new ArrayList<>();
        Map<Product, Integer> itemMap = getCart().getItemWithQuantity();

        for (Map.Entry<Product, Integer> entry : itemMap.entrySet()) {
            ShoppingCartItemsModel cartItem = new ShoppingCartItemsModel();
            cartItem.setProduct(entry.getKey());
            cartItem.setQuantity(entry.getValue());
            cartItems.add(cartItem);
        }

        return cartItems;
    }
}
