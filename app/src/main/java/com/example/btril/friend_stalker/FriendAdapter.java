package com.example.btril.friend_stalker;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by juhi on 7/24/17.
 */
public class FriendAdapter extends ArrayAdapter<Friend> {

    ArrayList<com.example.btril.friend_stalker.Friend> friends_list=null;
    Context context;

    public FriendAdapter(Context context, ArrayList<com.example.btril.friend_stalker.Friend> resource) {
        super(context, R.layout.friends_detail,resource);

        this.context=context;
        this.friends_list=resource;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater= ((Activity)context).getLayoutInflater();
        convertView= inflater.inflate(R.layout.friends_detail,parent, false);


        TextView friend_fn=(TextView)convertView.findViewById(R.id.friend_fn);
        TextView friend_ln=(TextView) convertView.findViewById(R.id.friend_ln);
        TextView friend_email=(TextView)convertView.findViewById(R.id.email);
        TextView friend_phone_number=(TextView)convertView.findViewById(R.id.friend_phone_number);
        TextView friend_distance=(TextView)convertView.findViewById(R.id.friend_distance);

        TextView friend_latitude=(TextView)convertView.findViewById(R.id.friend_latitude);
        TextView friend_longitude=(TextView)convertView.findViewById(R.id.friend_longitute);

        friend_fn.setText(friends_list.get(position).getfn());
        friend_ln.setText(friends_list.get(position).getLn());
        friend_email.setText(friends_list.get(position).getEmail());
        friend_ln.setText(String.valueOf(friends_list.get(position).getNumber()));
        friend_ln.setText(String.valueOf(friends_list.get(position).getdistance()));
        friend_ln.setText(String.valueOf(friends_list.get(position).getlatitude()));
        friend_ln.setText(String.valueOf(friends_list.get(position).getlongitude()));

        return convertView;




    }
}


