package com.example.smartwater.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartwater.R;

public class ShoppingCartFragment extends Fragment {

    private ListView listView;
    private TextView totalSum;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        totalSum = root.findViewById(R.id.total);
        listView = root.findViewById(R.id.cartList);

        ShoppingCartAdapter shoppingCartAdapter = new ShoppingCartAdapter(getContext(), ShoppingCartHelper.getCartItems());

        listView.setAdapter(shoppingCartAdapter);

        float totalPrice = ShoppingCartHelper.getCart().getTotalPrice();
        totalSum.setText("Итого: " + totalPrice  + " ₽");

        return root;
    }
}
