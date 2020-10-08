package com.example.smartwater.ui.login;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.smartwater.util.LoadingDialog;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class SendRegistrationData extends AsyncTask<String, Void, String> {

    private LoadingDialog loadingDialog;

    private final String address = "http://smartaqua.mcdir.ru/bd/android/new_user.php";

    SendRegistrationData(Activity activity) {
        super();
        this.loadingDialog = new LoadingDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        loadingDialog.startLoadingDialog();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("id", params[0])
                .add("name", params[1])
                .add("last_name", params[2])
                .add("email", params[3])
                .add("phone", params[4])
                .add("password", params[5])
                .build();

        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            loadingDialog.dismissDialog();
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        loadingDialog.dismissDialog();
        super.onPostExecute(result);
    }
}
