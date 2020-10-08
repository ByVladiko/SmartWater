package com.example.smartwater.ui.profile;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.smartwater.util.LoadingDialog;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class EditProfile extends AsyncTask<String, Void, String> {

    private LoadingDialog loadingDialog;

    private final String address = "http://smartaqua.mcdir.ru/bd/android/edit_user.php";

    EditProfile(Activity activity) {
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
                .add("password", params[4])
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
            return "Error";
        }
    }

    @Override
    protected void onPostExecute(String json) {
        loadingDialog.dismissDialog();
        super.onPostExecute(json);
    }
}
