package client.app.clientgcm.adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import client.app.clientgcm.R;
import client.app.clientgcm.model.Notification;


public class NotificationAdapter extends ArrayAdapter<Notification> {

    Context context;
    LayoutInflater inflater;
    ArrayList<Notification> list;

    public NotificationAdapter(Context context, LayoutInflater inflater, ArrayList<Notification> list) {

        super(context, R.layout.list_item_notification, list);
        this.context = context;
        this.inflater = inflater;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.list_item_notification, null);
        TextView textDate = (TextView)view.findViewById(R.id.txtDate);
        TextView textTime = (TextView)view.findViewById(R.id.txtTime);
        TextView textMessage = (TextView)view.findViewById(R.id.txtNotification);

        textDate.setText("" + list.get(position).date);
        textTime.setText("" + list.get(position).time);
        textMessage.setText("" + list.get(position).message);

        //Make URL and Phone hyperlink (click and go).
        Linkify.addLinks(textMessage, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);

        return view;
    }
}
