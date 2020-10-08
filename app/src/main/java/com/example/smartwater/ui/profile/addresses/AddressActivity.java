package com.example.smartwater.ui.profile.addresses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartwater.MainActivity;
import com.example.smartwater.R;
import com.example.smartwater.model.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddressActivity extends AppCompatActivity {

    private ListView addressesListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_listview);

        addressesListView = findViewById(R.id.address_list);

        findViewById(R.id.new_address).setOnClickListener(v -> {
            startActivity(new Intent(AddressActivity.this, NewAddressActivity.class));
        });

        String idUser = getSharedPreferences("SmartWater", MODE_PRIVATE).getString("User", "");

        loadAddresses(idUser);
    }

    private void loadAddresses(String user) {
        GetAddresses getAddresses = new GetAddresses(AddressActivity.this);
        try {
            String json = getAddresses.execute(user).get();
            loadIntoListView(json);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadIntoListView(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            List<Address> addressList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                addressList.add(new Address(obj.getString("id"),
                        obj.getString("city"),
                        obj.getString("street"),
                        obj.getInt("building"),
                        obj.getInt("apartment")));
            }
            AddressAdapter addressAdapter = new AddressAdapter(this, addressList);
            addressesListView.setAdapter(addressAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
