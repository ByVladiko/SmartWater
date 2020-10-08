package com.example.smartwater.ui.profile;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.smartwater.util.LoadingDialog;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class GetProfile extends AsyncTask<String, Void, String> {

    private LoadingDialog loadingDialog;

    private final String address = "http://smartaqua.mcdir.ru/bd/android/select_user.php";

    GetProfile(Activity activity) {
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
            loadingDialog.dismissDialog();
            e.printStackTrace();
            return "Internal error";
        }
    }

    @Override
    protected void onPostExecute(String json) {
        loadingDialog.dismissDialog();
        super.onPostExecute(json);
    }
}
