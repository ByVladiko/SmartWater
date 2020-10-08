package com.example.smartwater.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.smartwater.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "MyTag";

    private String CHANNEL_ID = "IMPORTANCE_DEFAULT";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d(TAG, "onMessageReceived: called");
        Log.d(TAG, "onMessageReceived: Message received from: " + remoteMessage.getFrom());

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
                if (notificationChannel == null) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    notificationChannel = new NotificationChannel(CHANNEL_ID, "Датчик воды", importance);
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .build();
                notificationManager.notify(1002, notification);
            } else {
                Notification notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .build();
                notificationManager.notify(1002, notification);
            }

        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "onMessageReceived: Size: " + remoteMessage.getData().size());

            Log.d(TAG, "onMessageReceived: Data: " + remoteMessage.getData().toString());
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG, "onDeleteMessages: called");
    }

    @Override
    public void onNewToken(@NonNull String result) {
        super.onNewToken(result);
//        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("SmartWater", MODE_PRIVATE);
//        String idUser = sharedPreferences.getString("User", "");
//        if(!idUser.isEmpty()) {
//            RegisterToken registerToken = new RegisterToken();
//            try {
//                registerToken.execute(idUser, result).get();
//            } catch (ExecutionException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        Log.d(TAG, "onNewToken: called " + result);
    }
}
