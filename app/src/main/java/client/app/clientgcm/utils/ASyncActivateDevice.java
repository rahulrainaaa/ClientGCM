package client.app.clientgcm.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ASyncActivateDevice extends AsyncTask<String, String, String> {

    private Context context;
    private JSONObject jsonRequest;

    public ASyncActivateDevice(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        //Toast.makeText(context, "Registering", Toast.LENGTH_LONG).show();
        jsonRequest = new JSONObject();
        try
        {
            SharedPreferences s = context.getSharedPreferences("info_cache", 0);
            String pref = s.getString("GCM_ID", "NO").trim();
            if(!pref.equals("NO"))
            {
                jsonRequest.put("DEVICEID", pref.toString());
            }
        }
        catch (Exception e){}
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            //Create HttpURLConnection and set request properties
            URL url = new URL(Constants.REGISTER_DEVICE);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");

            //Create output stream and write data-request
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonRequest.toString()); // should be fine if my getQuery is encoded right yes?
            writer.flush();
            writer.close();
            os.close();

            //Open and check the connectivity
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            String str = urlConnection.getResponseMessage();
            if (statusCode ==  200)
            {
                InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader read = new InputStreamReader(it);
                BufferedReader buff = new BufferedReader(read);
                StringBuilder dta = new StringBuilder();
                String chunks ;
                while((chunks = buff.readLine()) != null)
                {
                    dta.append(chunks);
                }
                return dta.toString();
            }
            else
            {
                return "ERROR";
            }

        }
        catch (Exception e)
        {
            return "EXCEPTION";
        }

    }

    @Override
    protected void onPostExecute(String s) {

        try {
            JSONObject response = new JSONObject(s.toString());
            if (response.getString("statusCode").contains("200"))
            {
                SharedPreferences.Editor se = context.getSharedPreferences("info_cache", 0).edit();
                se.putString("REGISTER","YES");
                se.commit();
                Toast.makeText(context, "Device Registered Successfully.", Toast.LENGTH_SHORT).show();
            }
            else if (response.getString("statusCode").contains("300"))
            {
                Toast.makeText(context, "" + response.getString("data").toString(), Toast.LENGTH_SHORT).show();
            }
            else if (response.getString("statusCode").contains("400"))
            {
                Toast.makeText(context, "" + response.getString("data").toString(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Please check internet connection.", Toast.LENGTH_LONG).show();
        }

        super.onPostExecute("");
    }
}
