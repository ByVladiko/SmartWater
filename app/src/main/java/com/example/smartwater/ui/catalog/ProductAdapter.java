package com.example.smartwater.ui.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartwater.R;
import com.example.smartwater.model.Product;
import com.example.smartwater.ui.cart.ShoppingCart;
import com.example.smartwater.ui.cart.ShoppingCartHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    Context context;
    private List<Product> list;
    private LayoutInflater layoutInflater;

    public ProductAdapter(Context context, List<Product> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.product_list_item, parent, false);
        }

        init(view, position);

        return view;
    }

    private void init(View view, int position) {

        Product product = (Product) getItem(position);
        ShoppingCart shoppingCart = ShoppingCartHelper.getCart();

        ((TextView) view.findViewById(R.id.item_name_text_view)).setText(product.getName());
        ((TextView) view.findViewById(R.id.description_text_view)).setText(product.getDescription());
        ((TextView) view.findViewById(R.id.cost_text_view)).setText(product.getPrice() + " ₽");
        ImageView imageView = view.findViewById(R.id.image_view);
        Picasso.with(context).load(product.getImage()).into(imageView);

        view.findViewById(R.id.addButton).setOnClickListener(v -> {
            shoppingCart.add(product);
        });
    }
}
