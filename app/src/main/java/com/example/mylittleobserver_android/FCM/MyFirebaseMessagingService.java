package com.example.mylittleobserver_android.FCM;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.mylittleobserver_android.Activities.MainActivity;
import com.example.mylittleobserver_android.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static final String HEARTWARNING = " Heart warning!!";
    private static final String DECIBELWARNING = " Decibel warning!!";
    private static final String TUMBLEWARNING = " Tumble warning!!";

    private SharedPreferences heartpreferences;
    private SharedPreferences decibelpreferences;
    private SharedPreferences tumblepreferences;

    private boolean heartBoolean;
    private boolean decibelBoolean;
    private boolean tumbleBoolean;

    @Override
    public void onNewToken(String token) {
        Log.d("Refreshed fuck token: ", token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        heartpreferences = getSharedPreferences("heart", MODE_PRIVATE);
        decibelpreferences = getSharedPreferences("decibel", MODE_PRIVATE);
        tumblepreferences = getSharedPreferences("tumble", MODE_PRIVATE);
        // 뒤에 true는 key에 해당하는 데이터가 없거나 읽어오는데 실패했을 때 기본으로 얻게될 데이터userA
        heartBoolean = heartpreferences.getBoolean("heart",true);
        decibelBoolean = decibelpreferences.getBoolean("decibel", true);
        tumbleBoolean = tumblepreferences.getBoolean("tumble", true);
        Log.d("data please!!!", String.valueOf(heartBoolean));
        Log.d("data2 please!!!", String.valueOf(decibelBoolean));
        Log.d("data3 please!!!", String.valueOf(tumbleBoolean));

        if(TextUtils.equals(remoteMessage.getNotification().getTitle(),HEARTWARNING) && heartBoolean == false){
            Log.d("hello!", String.valueOf(TextUtils.equals(remoteMessage.getNotification().getTitle(),HEARTWARNING) && heartBoolean == false));
            return;
        }else if(TextUtils.equals(remoteMessage.getNotification().getTitle(),DECIBELWARNING) && decibelBoolean == false){
            Log.d("hello2!", String.valueOf(TextUtils.equals(remoteMessage.getNotification().getTitle(),DECIBELWARNING) && decibelBoolean == false));
            return;
        }else if(TextUtils.equals(remoteMessage.getNotification().getTitle(),TUMBLEWARNING) && tumbleBoolean == false){
            Log.d("hello3!", String.valueOf(TextUtils.equals(remoteMessage.getNotification().getTitle(),TUMBLEWARNING) && tumbleBoolean == false));
            return;
        } else {
            sendingMessage(remoteMessage);
        }
    }


    public void sendingMessage(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String messageTitle = remoteMessage.getNotification().getTitle();
            String messageBody = remoteMessage.getNotification().getBody();
            Log.d("FCM Log", "알림 메세지 :" + remoteMessage.getNotification().getTitle());

            if(TextUtils.equals(messageTitle,HEARTWARNING)){
                messageTitle = "심박수가 높습니다";
            }else if(TextUtils.equals(messageTitle,DECIBELWARNING)){
                messageTitle = "주변소리가 높습니다";
            }else if(TextUtils.equals(messageTitle,TUMBLEWARNING)){
                messageTitle = "넘어졌습니다";
            }
            Intent intent;
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Channel ID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder
                    = new NotificationCompat.Builder(this, channelId);
            notificationBuilder.setSmallIcon(R.mipmap.logo_main_round);
            notificationBuilder.setContentTitle(messageTitle);
            notificationBuilder.setContentText(messageBody);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = "Channel Name";
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notificationBuilder.build());
        }
    }


}
