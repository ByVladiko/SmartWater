package com.example.smartwater.ui.login;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartwater.R;
import com.example.smartwater.util.InternetConnection;
import com.github.pinball83.maskededittext.MaskedEditText;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {

    private MaskedEditText textPhone;
    private EditText textName, textLastName, textEmail, textPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        textPhone = findViewById(R.id.phone);
        textName = findViewById(R.id.name);
        textLastName = findViewById(R.id.last_name);
        textEmail = findViewById(R.id.email);
        textPass = findViewById(R.id.password_registration);

        findViewById(R.id.button_registry).setOnClickListener(v -> {
            if (InternetConnection.isOnline(getBaseContext()) && checkInput()) {
                String phone = "79" + textPhone.getUnmaskedText();
                String name = textName.getText().toString();
                String lastName = textLastName.getText().toString();
                String email = textEmail.getText().toString();
                String password = textPass.getText().toString();

                createUser(UUID.randomUUID().toString(), name, lastName,
                        email, phone, DigestUtils.md5Hex(password));
            }
        });
    }

    private boolean checkInput() {
        String phone = textPhone.getUnmaskedText();
        String name = textName.getText().toString();
        String lastName = textLastName.getText().toString();
        String email = textEmail.getText().toString();
        String pass = textPass.getText().toString();

        ViewGroup group = findViewById(R.id.edit_text_registration);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                if (((EditText) view).getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Все поля ввода должны быть заполнены", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

        boolean corrEmail = Pattern
                .compile("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$")
                .matcher(email)
                .matches();
        Pattern pattern = Pattern.compile("[А-яЁё]{3,}");
        boolean corrName = pattern.matcher(name).matches();
        boolean corrLastName = pattern.matcher(lastName).matches();
        boolean corrPass = Pattern.compile("([0-9A-Za-z]){6,}").matcher(pass).matches();

        if (phone.length() != 9) {
            Toast.makeText(getBaseContext(), "Некорректный ввод телефона", Toast.LENGTH_LONG).show();
            return false;
        } else if (!corrEmail) {
            Toast.makeText(getBaseContext(), "Некорректный ввод email", Toast.LENGTH_LONG).show();
            return false;
        } else if (!corrPass) {
            Toast.makeText(getBaseContext(), "Пароль должен состоять из латиницы или цифр", Toast.LENGTH_LONG).show();
            return false;
        } else if (!corrName || !corrLastName) {
            Toast.makeText(getBaseContext(), "Имя и фамилия должны состоять из кирилицы и иметь не менее 3ех символов", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void createUser(String id, String name, String lastName,
                            String email, String phone, String pass) {
        try {
            SendRegistrationData sendRegistrationData = new SendRegistrationData(Registration.this);
            String json = sendRegistrationData.execute(id, name, lastName, email, phone, pass).get();
            if (json.contains("Success")) {
                Toast.makeText(getBaseContext(), "Успешно!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Внутрення ошибка", Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}


