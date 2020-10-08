package com.example.smartwater.ui.cart;

import com.example.smartwater.model.Product;
import com.example.smartwater.ui.cart.exceptions.ProductNotFoundException;
import com.example.smartwater.ui.cart.exceptions.QuantityOutOfRangeException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 42L;

    private Map<Product, Integer> cartItemMap = new HashMap<>();
    private float totalPrice = 0;
    private int totalQuantity = 0;

    public void add(final Product product) {
        if (cartItemMap.containsKey(product)) {
            cartItemMap.put(product, cartItemMap.get(product) + 1);
        } else {
            cartItemMap.put(product, 1);
        }

        totalPrice = totalPrice + product.getPrice();
        totalQuantity++;
    }

    public void update(final Product product, int quantity) throws ProductNotFoundException, QuantityOutOfRangeException {
        if (!cartItemMap.containsKey(product)) throw new ProductNotFoundException();
        if (quantity < 0)
            throw new QuantityOutOfRangeException(quantity + " is not a valid quantity. It must be non-negative.");

        int productQuantity = cartItemMap.get(product);
        float productPrice = product.getPrice() * productQuantity;

        cartItemMap.put(product, quantity);

        totalQuantity = totalQuantity - productQuantity + quantity;
        totalPrice = totalPrice - productPrice + product.getPrice() * quantity;
    }

    public void remove(final Product product) throws ProductNotFoundException {
        if (!cartItemMap.containsKey(product)) throw new ProductNotFoundException();

        int productQuantity = cartItemMap.get(product);

        if (productQuantity == 1) {
            cartItemMap.remove(product);
        } else {
            cartItemMap.put(product, productQuantity - 1);
        }

        totalPrice = totalPrice - product.getPrice();
        totalQuantity -= 1;
    }

    public void clear() {
        cartItemMap.clear();
        totalPrice = 0;
        totalQuantity = 0;
    }

    public int getQuantity(final Product product) throws ProductNotFoundException {
        if (!cartItemMap.containsKey(product)) throw new ProductNotFoundException();
        return cartItemMap.get(product);
    }

    public float getCost(final Product product) throws ProductNotFoundException {
        if (!cartItemMap.containsKey(product)) throw new ProductNotFoundException();
        return product.getPrice() * cartItemMap.get(product);
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public Set<Product> getProducts() {
        return cartItemMap.keySet();
    }

    public Map<Product, Integer> getItemWithQuantity() {
        Map<Product, Integer> cartItemMap = new HashMap<Product, Integer>();
        cartItemMap.putAll(this.cartItemMap);
        return cartItemMap;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : cartItemMap.entrySet()) {
            strBuilder.append(String.format("Product: %s, Unit Price: %f, Quantity: %d%n",
                    entry.getKey().getName(),
                    entry.getKey().getPrice(),
                    entry.getValue()));
        }
        strBuilder.append(String.format("Total Quantity: %d, Total Price: %f",
                totalQuantity,
                totalPrice));

        return strBuilder.toString();
    }
}
