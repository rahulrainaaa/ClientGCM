package client.app.clientgcm.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import client.app.clientgcm.R;
import client.app.clientgcm.utils.ASyncActivateDevice;
import client.app.clientgcm.utils.Constants;
import client.app.clientgcm.utils.Utility;


public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private ImageView imgNotification;
    private ImageView imgEmail;
    private ImageView imgCall;
    private ImageView imgWebsite;
    private ImageView imgLocation;
    private ImageView imgMaps;

    private TextView t1,t2,t3,t4,t5,t6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        t1 = (TextView)findViewById(R.id.id_text_address);
        t2 = (TextView)findViewById(R.id.id_text_call);
        t3 = (TextView)findViewById(R.id.id_text_email);
        t4 = (TextView)findViewById(R.id.id_text_notification);
        t5 = (TextView)findViewById(R.id.id_text_web);
        t6 = (TextView)findViewById(R.id.id_text_maps);

        imgCall = (ImageView) findViewById(R.id.id_call);
        imgNotification = (ImageView) findViewById(R.id.id_notification);
        imgEmail = (ImageView) findViewById(R.id.id_email);
        imgWebsite = (ImageView) findViewById(R.id.id_website);
        imgLocation = (ImageView) findViewById(R.id.id_location);
        imgMaps = (ImageView) findViewById(R.id.id_maps);

        Animation animNotification = AnimationUtils.loadAnimation(this, R.anim.anim_pop_notification);
        Animation animEmail = AnimationUtils.loadAnimation(this, R.anim.anim_pop_mail);
        Animation animCall = AnimationUtils.loadAnimation(this, R.anim.anim_pop_call);
        Animation animWeb = AnimationUtils.loadAnimation(this, R.anim.anim_pop_web);
        Animation animLocation = AnimationUtils.loadAnimation(this, R.anim.anim_pop_location);
        Animation animMaps = AnimationUtils.loadAnimation(this, R.anim.anim_pop_maps);

        imgNotification.startAnimation(animNotification);
        imgEmail.startAnimation(animEmail);
        imgCall.startAnimation(animCall);
        imgWebsite.startAnimation(animWeb);
        imgLocation.startAnimation(animLocation);
        imgMaps.startAnimation(animMaps);

        Animation animtaion = AnimationUtils.loadAnimation(this, R.anim.anim_label_appear);
        t1.startAnimation(animtaion);
        t2.startAnimation(animtaion);
        t3.startAnimation(animtaion);
        t4.startAnimation(animtaion);
        t5.startAnimation(animtaion);
        t6.startAnimation(animtaion);

        imgNotification.setOnClickListener(this);
        imgEmail.setOnClickListener(this);
        imgWebsite.setOnClickListener(this);
        imgCall.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        imgMaps.setOnClickListener(this);

        imgNotification.setOnLongClickListener(this);
        imgEmail.setOnLongClickListener(this);
        imgWebsite.setOnLongClickListener(this);
        imgCall.setOnLongClickListener(this);
        imgLocation.setOnLongClickListener(this);
        imgMaps.setOnLongClickListener(this);

        SharedPreferences s = getSharedPreferences("info_cache", 0);
        String pref = s.getString("REGISTER_GCM", "NO").trim();
        if(!pref.equals("NO"))
        {
            if(s.getString("ACTIVATE_SMS","NO").equals("NO"))
            {
                if(!s.getString("REGISTER_SMS", "NO").equals("NO"))
                {
                    ASyncActivateDevice info = new ASyncActivateDevice(this);
                    info.execute("");
                }
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //Check if the SMS Permission is given or not
                int check1 = ContextCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.RECEIVE_SMS);
                int check2 = ContextCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.READ_SMS);

                if((check1 != PackageManager.PERMISSION_GRANTED) || (check2 != PackageManager.PERMISSION_GRANTED))
                {
                    Toast.makeText(getApplicationContext(), "Go to permissions and \nEnable the SMS permission", Toast.LENGTH_LONG).show();
                    Utility.startInstalledAppDetailsActivity(DashboardActivity.this);
                }

            }
        }, 1200);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.id_call)
        {
            String uri = "tel:" + Constants.PHONE;
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
            startActivity(callIntent);
        }
        else if(v.getId() == R.id.id_email)
        {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{Constants.MAIL});
            i.putExtra(Intent.EXTRA_SUBJECT, "");
            i.putExtra(Intent.EXTRA_TEXT   , "");
            try
            {
                startActivity(Intent.createChooser(i, ""));
            } catch (android.content.ActivityNotFoundException ex)
            {
                Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId() == R.id.id_location)
        {
            startActivity(new Intent(DashboardActivity.this, AddressActivity.class));
        }
        else if(v.getId() == R.id.id_notification)
        {
            startActivity(new Intent(DashboardActivity.this, NotificationActivity.class));
        }
        else if(v.getId() == R.id.id_website)
        {
            try
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.WEB));
                startActivity(browserIntent);
            } catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Invalid URL\n Cannot open web.", Toast.LENGTH_SHORT).show();
            }

        }
        else if(v.getId() == R.id.id_maps)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + Constants.ADDRESS_LAT_LONG));
            startActivity(intent);
        }

    }

    @Override
    public boolean onLongClick(View v) {

        if(v.getId() == R.id.id_call)
        {
            Snackbar.make(v, "Call Us", Snackbar.LENGTH_LONG).show();
        }
        else if(v.getId() == R.id.id_email)
        {
            Snackbar.make(v, "Email Us", Snackbar.LENGTH_LONG).show();
        }
        else if(v.getId() == R.id.id_location)
        {
            Snackbar.make(v, "Address", Snackbar.LENGTH_LONG).show();
        }
        else if(v.getId() == R.id.id_notification)
        {
            Snackbar.make(v, "Offers and Notifications", Snackbar.LENGTH_LONG).show();
        }
        else if(v.getId() == R.id.id_website)
        {
            Snackbar.make(v, "Visit Us", Snackbar.LENGTH_LONG).show();
        }
        else if(v.getId() == R.id.id_maps)
        {
            Snackbar.make(v, "Map Location", Snackbar.LENGTH_LONG).show();
        }
        return true;
    }

}