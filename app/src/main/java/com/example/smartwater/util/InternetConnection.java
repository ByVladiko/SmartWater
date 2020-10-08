package com.example.smartwater.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class InternetConnection {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {
            return true;
        }
        Toast.makeText(context, "Отсутствует подключение", Toast.LENGTH_SHORT).show();
        return false;
    }

}
