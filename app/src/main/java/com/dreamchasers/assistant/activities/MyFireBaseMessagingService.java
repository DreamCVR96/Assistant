package com.dreamchasers.assistant.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.activities.shortcut.testas;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import butterknife.BindView;

/**
 * Created by ka on 12/9/2016.
 */









public class MyFireBaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.v("verbose wasaa xD ", "wazaaaa xD");


    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String str= remoteMessage.getData().toString();
        Log.v("Gauta zinute " , str );

        System.out.println("intent Received");
        String jsonString = str;
        Intent RTReturn = new Intent(MainActivity.RECEIVE_JSON);
        RTReturn.putExtra("json", jsonString);
        LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);

      //  Toast toast = Toast.makeText(, "test", Toast.LENGTH_LONG);
      //  toast.show();



     //   if(1==1)return;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
       // Log.v("musu id yra : ", FirebaseInstanceId.getInstance().getToken());

       // Log.v("tagas? XD " , " gavau zinute is ateieties man.....");
        notificationBuilder.setContentTitle("FCM XD ");
        notificationBuilder.setContentText(str);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.fb_m);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notifactionManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifactionManager.notify(0,notificationBuilder.build()) ;
        ijunkBroadCasta();
    }



    private void ijunkBroadCasta() {
        Intent i = new Intent();
        i.setAction("com.dreamchasers.assistant");
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(i);
    }


}


