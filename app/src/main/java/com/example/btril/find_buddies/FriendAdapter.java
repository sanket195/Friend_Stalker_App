package com.example.btril.find_buddies;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.btril.find_buddies.data.FriendsDetails;

import java.util.ArrayList;

/**
 * Created by juhi on 7/24/17.
 */
public class FriendAdapter extends ArrayAdapter<FriendsDetails> {

    ArrayList<FriendsDetails> friends_list = null;
    Context context;

    public FriendAdapter(Context context, ArrayList<FriendsDetails> resource) {
        super(context, R.layout.friends_detail, resource);

        this.context = context;
        this.friends_list = resource;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.friends_detail, parent, false);


        TextView friend_fn = (TextView) convertView.findViewById(R.id.friend_fn);
        TextView friend_ln = (TextView) convertView.findViewById(R.id.friend_ln);
        TextView friend_email = (TextView) convertView.findViewById(R.id.friend_email);
        TextView friend_phone_number = (TextView) convertView.findViewById(R.id.friend_phone_number);
        TextView friend_distance = (TextView) convertView.findViewById(R.id.friend_distance);

        TextView friend_latitude = (TextView) convertView.findViewById(R.id.friend_latitude);
        TextView friend_longitude = (TextView) convertView.findViewById(R.id.friend_longitute);

        friend_latitude.setVisibility(View.GONE);
        friend_longitude.setVisibility(View.GONE);

        friend_ln.setText(friends_list.get(position).getLname());
        friend_fn.setText("\n          Name:  " + friends_list.get(position).getEmail().split("@")[0].toUpperCase());
        friend_ln.setText(friends_list.get(position).getLname());
        friend_email.setText("          E-Mail:  " + friends_list.get(position).getEmail() + "\n");
        if (friends_list.get(position).getDistance() == 0.0) {
            friend_ln.setText(String.valueOf("\n         Wave a Hi..!"));
        } else {
            friend_ln.setText(String.valueOf("\n         " + friends_list.get(position).getDistance() + " miles"));
        }

        friend_latitude.setText("Latitude :" + String.valueOf(friends_list.get(position).getLatitude()));
        friend_longitude.setText("Longitude :" + String.valueOf(friends_list.get(position).getLongitude()));

        return convertView;
    }
}


