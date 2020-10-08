package com.example.smartwater.ui.catalog;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.smartwater.util.LoadingDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class LoadCatalog extends AsyncTask<Void, Void, String> {

    private LoadingDialog loadingDialog;

    private final String address = "http://smartaqua.mcdir.ru/bd/android/select_catalog.php";

    LoadCatalog(Activity activity) {
        super();
        this.loadingDialog = new LoadingDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        loadingDialog.startLoadingDialog();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json).append("\n");
            }
            return sb.toString().trim();
        } catch (Exception e) {
            loadingDialog.dismissDialog();
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        loadingDialog.dismissDialog();
        super.onPostExecute(s);
    }
}
