package client.app.clientgcm.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.widget.Toast;

import client.app.clientgcm.utils.ASyncActivateDevice;
import client.app.clientgcm.utils.Constants;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    Context ctxt;
    public SmsBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Client App SMS Received", Toast.LENGTH_LONG).show();

        ctxt = context;
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get("pdus");
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();


                if (!TextUtils.isEmpty(smsBody) && !TextUtils.isEmpty(address)) {
                    if ((smsBody.contains(Constants.ACTIVATION_VALIDATION_MSG)) || (address.contains(Constants.ACTIVATION_VALICATION_NUMBER))) {

                        Toast.makeText(context, "" + address + "\n" + smsBody, Toast.LENGTH_LONG).show();

                        SharedPreferences s = context.getSharedPreferences("info_cache", 0);
                        String pref = s.getString("REGISTER_GCM", "NO").trim();
                        //check if the GCM id is registered in database.
                        if(pref.equals("NO"))
                        {
                            return;
                        }

                        //HTTP handling to activate register device id.
                        ASyncActivateDevice actDev = new ASyncActivateDevice(context);
                        actDev.execute("");

                        SharedPreferences.Editor se = context.getSharedPreferences("info_cache", 0).edit();
                        se.putString("REGISTER_SMS","YES");
                        se.commit();

                    }
                }
            }
        }
    }




}
