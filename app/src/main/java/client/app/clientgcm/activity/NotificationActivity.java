package client.app.clientgcm.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import client.app.clientgcm.R;
import client.app.clientgcm.adapter.NotificationAdapter;
import client.app.clientgcm.model.Notification;

public class NotificationActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    ListView lw;
    NotificationAdapter adapter;
    JSONArray jarray;
    ArrayList<Notification> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        lw = (ListView)findViewById(R.id.listView11);
        lw.setOnItemLongClickListener(this);
        list = new ArrayList<Notification>();
        adapter = new NotificationAdapter(this, getLayoutInflater(), list);
        lw.setAdapter(adapter);
        populateListData();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        Snackbar.make(view, "Are you sure to delete this?", Snackbar.LENGTH_LONG).setAction("Delete", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int jsonPos = jarray.length() - 1 - position;
                JSONArray j = new JSONArray();
                try {
                    for (int i = 0; i < jarray.length(); i++) {
                        if (jsonPos == i) {
                            continue;
                        }
                        JSONObject json = new JSONObject();
                        json = jarray.getJSONObject(i);
                        j.put(json);
                    }
                    SharedPreferences.Editor se = getSharedPreferences("info_push", 0).edit();
                    se.putString("data", j.toString());
                    se.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                populateListData();
            }
        }).show();
        return true;
    }

    private void populateListData()
    {
        SharedPreferences s = getSharedPreferences("info_push", 0);
        String data = s.getString("data", "[]");
        list.clear();
        try
        {
            jarray = new JSONArray(data);

            //Iteration in descending order  to put latest notification on top.
            for(int i = (jarray.length() - 1); i >= 0 ; i--)
            {
                Notification n = new Notification();
                n.title = "" + jarray.getJSONObject(i).getString("title");
                n.message = "" + jarray.getJSONObject(i).getString("message");
                n.date = "" + jarray.getJSONObject(i).getString("date");
                n.time = "" + jarray.getJSONObject(i).getString("time");
                list.add(n);
            }
        }
        catch (JSONException e) {

        }
       adapter.notifyDataSetChanged();
    }
}
