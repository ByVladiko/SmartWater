package com.example.smartwater.ui.profile.addresses;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.smartwater.util.LoadingDialog;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class AddAddress extends AsyncTask<String, Void, String> {

    private LoadingDialog loadingDialog;

    private final String address = "http://smartaqua.mcdir.ru/bd/android/new_address.php";

    AddAddress(Activity activity) {
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
                .add("city", params[1])
                .add("street", params[2])
                .add("building", params[3])
                .add("apartment", params[4])
                .add("user_id", params[5])
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
