package com.example.smartwater.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartwater.R;
import com.example.smartwater.model.User;
import com.example.smartwater.ui.profile.addresses.AddressActivity;
import com.example.smartwater.util.InternetConnection;
import com.github.pinball83.maskededittext.MaskedEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private EditText textName;
    private EditText textLastName;
    private EditText textEmail;
    private EditText textPassword;
    private Button buttonEditProfile;
    private Button buttonAddresses;
    private MaskedEditText textPhone;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        if(!InternetConnection.isOnline(getContext())) {
            ViewGroup group = root.findViewById(R.id.edit_text_linear);

            for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                View view = group.getChildAt(i);
                view.setVisibility(View.GONE);
            }
            return root;
        }

        textPhone = root.findViewById(R.id.phone);
        textName = root.findViewById(R.id.name);
        textLastName = root.findViewById(R.id.last_name);
        textEmail = root.findViewById(R.id.email);
        textPassword = root.findViewById(R.id.password);
        buttonEditProfile = root.findViewById(R.id.button_edit);
        buttonAddresses = root.findViewById(R.id.button_addresses);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SmartWater", MODE_PRIVATE);
        String idUser = sharedPreferences.getString("User", "");

        getProfile(idUser);
        init(root);

        return root;
    }

    private void init(View root) {
        ViewGroup group = root.findViewById(R.id.edit_text_linear);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                buttonEditProfile.setVisibility(View.VISIBLE);
            }
        };

        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).addTextChangedListener(textWatcher);
            }
        }

        buttonEditProfile.setOnClickListener(v -> {
            if (InternetConnection.isOnline(getContext()) && correctInput()) {
                String name = textName.getText().toString();
                String lastName = textLastName.getText().toString();
                String email = textEmail.getText().toString();
                String pass = textPassword.getText().toString();
                editProfile(name, lastName, email, pass);
            }
        });

        buttonAddresses.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddressActivity.class));
        });
    }

    private boolean correctInput() {
        String email = textEmail.getText().toString();
        String name = textName.getText().toString();
        String lastName = textLastName.getText().toString();
        String password = textPassword.getText().toString();

        boolean corrEmail = Pattern
                .compile("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$")
                .matcher(email)
                .matches();
        Pattern pattern = Pattern.compile("[A-zА-яЁё]{3,}");
        boolean corrName = pattern.matcher(name).matches();
        boolean corrLastName = pattern.matcher(lastName).matches();
        boolean corrPass = Pattern.compile("([0-9A-Za-z]){6,}").matcher(password).matches();

        if (!corrEmail) {
            Toast.makeText(getContext(), "Некорректный ввод email", Toast.LENGTH_LONG).show();
            return false;
        } else if(!corrName || !corrLastName) {
            Toast.makeText(getContext(), "Имя и фамилия должны состоять из кирилицы и иметь не менее 3ех символов", Toast.LENGTH_LONG).show();
            return false;
        } else if (!corrPass) {
            Toast.makeText(getContext(), "Пароль должен состоять из латиницы или цифр", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void fillIn(User user) {
        textPhone.setMaskedText(user.getPhone());
        textName.setText(user.getName());
        textLastName.setText(user.getLastName());
        textEmail.setText(user.getEmail());
    }

    private void editProfile(String name, String lastName, String email, String pass) {
        try {
            EditProfile editProfile = new EditProfile(getActivity());
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("SmartWater", MODE_PRIVATE);
            String idUser = sharedPreferences.getString("User", "");
            String json = editProfile.execute(idUser, name, lastName, email, pass).get();

            if (json.contains("Success")) {
                Toast.makeText(getContext(), "Успешно!", Toast.LENGTH_LONG).show();
                buttonEditProfile.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(getContext(), "Внутрення ошибка", Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getProfile(String userId) {
        try {
            GetProfile getProfile = new GetProfile(getActivity());
            String json = getProfile.execute(userId).get();

            JSONArray jsonArray = new JSONArray(json);
            JSONObject obj = jsonArray.getJSONObject(0);

            String name = obj.getString("name");
            String lastName = obj.getString("last_name");
            String phone = obj.getString("phone");
            String email = obj.getString("email");

            User user = new User(userId, phone, name, lastName, email);
            fillIn(user);
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
