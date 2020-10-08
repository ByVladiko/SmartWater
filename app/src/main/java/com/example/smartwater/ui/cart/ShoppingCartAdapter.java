package com.example.smartwater.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartwater.MainActivity;
import com.example.smartwater.R;
import com.example.smartwater.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingCartAdapter extends BaseAdapter {

    private Context context;
    private List<ShoppingCartItemsModel> list;
    private LayoutInflater layoutInflater;

    public ShoppingCartAdapter(Context context, List<ShoppingCartItemsModel> list) {
        this.context = context;
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = layoutInflater.inflate(R.layout.shopping_cart_item, parent, false);
        }

        ShoppingCartItemsModel shoppingCartItemsModel = (ShoppingCartItemsModel) getItem(position);
        Product product = shoppingCartItemsModel.getProduct();

        ((TextView) view.findViewById(R.id.item_name)).setText(product.getName());
        ((TextView) view.findViewById(R.id.item_description)).setText(product.getDescription());
        ((TextView) view.findViewById(R.id.item_price)).setText(product.getPrice() + " ₽");
        ((TextView) view.findViewById(R.id.item_count)).setText(String.valueOf(shoppingCartItemsModel.getQuantity()));
        ((TextView) view.findViewById(R.id.item_total))
                .setText(product.getPrice() * shoppingCartItemsModel.getQuantity() + " ₽");

        ImageView imageView = view.findViewById(R.id.image_view);
        Picasso.with(context).load(product.getImage()).into(imageView);

        Button subtract = view.findViewById(R.id.button_min);
        subtract.setOnClickListener(v -> {
            subtractItem(shoppingCartItemsModel);
        });

        Button additive = view.findViewById(R.id.button_plus);
        additive.setOnClickListener(v -> {
            additiveItem(shoppingCartItemsModel);
        });

        return view;
    }

    private void subtractItem(ShoppingCartItemsModel shoppingCartItemsModel) {
        Product product = shoppingCartItemsModel.getProduct();
        ShoppingCartHelper.getCart().remove(product);
        shoppingCartItemsModel.setQuantity(shoppingCartItemsModel.getQuantity() - 1);
        if (shoppingCartItemsModel.getQuantity() == 0) {
            list.remove(shoppingCartItemsModel);
        }
        notifyDataSetChanged();
        refreshTotal();
    }

    private void additiveItem(ShoppingCartItemsModel shoppingCartItemsModel) {
        Product product = shoppingCartItemsModel.getProduct();
        ShoppingCartHelper.getCart().add(product);
        shoppingCartItemsModel.setQuantity(shoppingCartItemsModel.getQuantity() + 1);
        notifyDataSetChanged();
        refreshTotal();
    }

    private void refreshTotal() {
        String textTotal = "Итого: " + ShoppingCartHelper.getCart().getTotalPrice() + " ₽";
        ((TextView)((MainActivity)context).findViewById(R.id.total)).setText(textTotal);
    }

}
