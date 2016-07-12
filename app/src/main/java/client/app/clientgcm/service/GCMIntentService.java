package client.app.clientgcm.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import client.app.clientgcm.R;
import client.app.clientgcm.activity.NotificationActivity;
import client.app.clientgcm.broadcast.MyBroadcastReceiver;

public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String TAG = "GCMNotificationIntentService";

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                sendNotification("Send Error", extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
            {
                sendNotification("Deleted Message: ", extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                String byteEncodeString = extras.getString("message").toString();
                System.out.println("***********************************************" +byteEncodeString);
                String actualMessage = new String(Base64.decodeBase64(byteEncodeString.getBytes()));
                saveData("" + extras.getString("title"), "" + actualMessage.toString());
                sendNotification("" + extras.getString("title"), "" + actualMessage.toString());
            }
        }
        MyBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void sendNotification(String title, String msg) {

       mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        notificationIntent.putExtra("NotificationIntent", true);
        notificationIntent.setFlags(Notification.FLAG_AUTO_CANCEL);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.notification)
                .setContentTitle("" + title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg.toString());

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void saveData(String title, String msg)
    {
        try
        {
            SharedPreferences s = getSharedPreferences("info_push", 0);
            String str = s.getString("data", "[]");
            JSONArray j = new JSONArray(str);
            JSONObject json = new JSONObject();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMMM-yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm a");
            String datee = sdfDate.format(c.getTime());
            String timee = sdfTime.format(c.getTime());
            json.put("message", "" + msg);
            json.put("title", "" + title);
            json.put("date", "" + datee);
            json.put("time", "" + timee);
            j.put(json);
            SharedPreferences.Editor se = getSharedPreferences("info_push",0).edit();
            se.putString("data", j.toString());
            se.commit();
            System.out.println("\nSaved message\n");
            Log.d("MyLogggggggggggggggg","Saved Message");
            s = getSharedPreferences("info_push", 0);
            String dataaaa = s.getString("data", "[]");
            JSONArray jaaaaaa = new JSONArray(dataaaa);
        }
        catch (Exception e) {}

    }

}
