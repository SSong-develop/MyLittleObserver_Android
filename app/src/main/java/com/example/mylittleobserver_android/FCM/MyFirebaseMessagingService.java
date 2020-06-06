package com.example.mylittleobserver_android.FCM;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {
        Log.d("Refreshed fuck token: ",token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Send Token to Server

    }
}
