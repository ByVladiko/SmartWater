package com.example.smartwater.firebase;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterToken extends AsyncTask<String, Void, String> {

    private final String address = "http://smartaqua.mcdir.ru/bd/android/token.php";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public RegisterToken() {
        super();
    }

    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", strings[0])
                .add("token", strings[1])
                .build();

        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
