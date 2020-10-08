package com.example.smartwater.ui.profile.addresses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartwater.MainActivity;
import com.example.smartwater.R;
import com.example.smartwater.ui.login.SignInActivity;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewAddressActivity extends AppCompatActivity {

    private EditText textCity, textStreet, textBuilding, textApartment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addresses_activity);

        textCity = findViewById(R.id.address_city);
        textStreet = findViewById(R.id.address_street);
        textBuilding = findViewById(R.id.address_building);
        textApartment = findViewById(R.id.address_apartment);

        findViewById(R.id.button_add_address).setOnClickListener(v -> {
            if (checkInput()) {
                newAddress();
            }
        });
    }

    private boolean checkInput() {
        ViewGroup group = findViewById(R.id.linear_layout_add_address);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                if (((EditText) view).getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Все поля ввода должны быть заполнены", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        return true;
    }

    private void newAddress() {
        String id = UUID.randomUUID().toString();
        String city = textCity.getText().toString();
        String street = textStreet.getText().toString();
        String building = textBuilding.getText().toString();
        String apartment = textApartment.getText().toString();
        String user = getSharedPreferences("SmartWater", MODE_PRIVATE).getString("User", "");

        try {
            AddAddress addAddress = new AddAddress(NewAddressActivity.this);
            String result = addAddress.execute(id, city, street, building, apartment, user).get();

            if (result.contains("Success")) {
                Toast.makeText(getBaseContext(), "Успешно!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NewAddressActivity.this, AddressActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Внутрення ошибка", Toast.LENGTH_LONG).show();
        }
    }
}
