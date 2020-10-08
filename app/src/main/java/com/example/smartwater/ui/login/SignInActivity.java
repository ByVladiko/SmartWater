package com.example.smartwater.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartwater.MainActivity;
import com.example.smartwater.R;
import com.example.smartwater.firebase.RegisterToken;
import com.example.smartwater.util.InternetConnection;
import com.github.pinball83.maskededittext.MaskedEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class SignInActivity extends AppCompatActivity {

    private MaskedEditText textLogin;
    private EditText textPass;
    private Button btnSignIn, btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        if (checkPreferences()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        textLogin = findViewById(R.id.phone);
        textPass = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.log_in_button);
        btnRegister = findViewById(R.id.registration);

        btnSignIn.setOnClickListener(v -> {
            if (InternetConnection.isOnline(getBaseContext()) && correctInput()) {
                String login = "79" + textLogin.getUnmaskedText();
                String password = textPass.getText().toString();
                signIn(login, DigestUtils.md5Hex(password));
            }
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, Registration.class);
            startActivity(intent);
        });

    }

    private boolean correctInput() {
        String login = textLogin.getUnmaskedText();
        String password = textPass.getText().toString();
        boolean corrLogin = Pattern.compile("^\\d{9}$").matcher(login).matches();
        boolean corrPass = Pattern.compile("[a-z0-9]{6,}").matcher(password).matches();
        if (login.length() != 9 || !corrLogin) {
            Toast.makeText(getBaseContext(), "Некорректный ввод телефона", Toast.LENGTH_LONG).show();
            return false;
        } else if (!corrPass) {
            Toast.makeText(getBaseContext(), "Пароль должен иметь 6 символов и состоять из латиницы", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void signIn(String login, String password) {
        try {
            SignIn sign = new SignIn(SignInActivity.this);
            String json = sign.execute(login, password).get();

            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() == 0) {
                Toast.makeText(getBaseContext(), "Неверный логин/пароль", Toast.LENGTH_LONG).show();
                return;
            }
            JSONObject obj = jsonArray.getJSONObject(0);
            String id = obj.getString("id");

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnSuccessListener(SignInActivity.this, instanceIdResult -> {
                        String newToken = instanceIdResult.getToken();
                        RegisterToken registerToken = new RegisterToken();
                        try {
                            String result = registerToken.execute(id, newToken).get();
                            if (!result.contains("Success")) {
                                Toast.makeText(getBaseContext(), "Внутренняя ошибка", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                            Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


            SharedPreferences.Editor editor = getSharedPreferences("SmartWater", MODE_PRIVATE).edit();
            editor.putString("User", id);
            editor.apply();


        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean checkPreferences() {
        String user = getSharedPreferences("SmartWater", MODE_PRIVATE).getString("User", "");
        return !user.isEmpty();
    }
}
