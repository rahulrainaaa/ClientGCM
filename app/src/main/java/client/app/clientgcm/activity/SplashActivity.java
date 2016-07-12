package client.app.clientgcm.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import client.app.clientgcm.R;
import client.app.clientgcm.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    public GoogleCloudMessaging gcm = null;
    public String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        SharedPreferences s = getSharedPreferences("info_cache", 0);
        String pref = s.getString("REGISTER_GCM", "NO").trim();

        GCMRegistrar.checkDevice(getApplicationContext());

        if(ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()))
        {
            if(pref.equals("NO"))
            {
               information info = new information();
                info.execute("");
            }
            else
            {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                        finish();
                    }
                }, 1200);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Error: Google Cloud Messaging not supported.", Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                    finish();
                }
            }, 2000);
        }
    }

    public class information extends AsyncTask<String, String, String>
    {
       @Override
        protected void onPreExecute() {
           Toast.makeText(getApplicationContext(), "Registering device for first time", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {

            try
            {
                //Register GCM first
                if (gcm == null)
                {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }
                msg = "" + gcm.register(Constants.PROJECT_NUMBER.toString().trim());

                if(msg.contains("invalid")
                        || msg.contains("error")
                        || msg.contains("exception")
                        || msg.contains("fail")
                        || msg.contains("misplace")
                        || msg.contains("missing")
                        || msg.contains("exceed"))
                {
                    return "ERROR";
                }

               return msg.toString();

            }
            catch (Exception e)
            {
                return "ERROR";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("ERROR"))
            {
                Toast.makeText(getApplicationContext(), "Check internet connection.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                SharedPreferences.Editor se = getSharedPreferences("info_cache",0).edit();
                se.putString("REGISTER_GCM","YES");
                se.putString("GCM_ID", "" + msg.toString());
                se.commit();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                    finish();
                }
            }, 2000);
        }
    }
}