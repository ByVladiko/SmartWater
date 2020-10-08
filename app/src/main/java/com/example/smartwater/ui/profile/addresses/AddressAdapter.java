package com.example.smartwater.ui.profile.addresses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartwater.R;
import com.example.smartwater.model.Address;

import java.util.List;

public class AddressAdapter extends BaseAdapter {
    private List<Address> list;
    private LayoutInflater layoutInflater;

    public AddressAdapter(Context context, List<Address> list) {
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
           view = layoutInflater.inflate(R.layout.address_list_item, parent, false);
        }

        Address product = (Address) getItem(position);

        TextView textView = view.findViewById(R.id.text_address_item);
        textView.setText(product.toString());

        return view;
    }
}
