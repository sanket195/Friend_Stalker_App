package com.example.btril.friend_stalker.handlers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btril.friend_stalker.R;
import com.example.btril.friend_stalker.SignInSuccess.NavigationItem;

import java.util.ArrayList;

/**
 * Created by juhi on 7/29/17.
 */

public class DrawerAdapter extends BaseAdapter {


    Context mContext;
    ArrayList<NavigationItem> mNavItems;

    public DrawerAdapter(Context context, ArrayList<NavigationItem> mNavItems) {
        mContext = context;
        mNavItems = mNavItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view;
// we get the inflator in the constructor.
        if (converView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.ui_items, null);
        } else {
            view = converView;
        }


        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.subtitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon_only);


        titleView.setText(mNavItems.get(position).title);
        subtitleView.setText(mNavItems.get(position).subtitle);
        iconView.setImageResource(mNavItems.get(position).icon);

        return view;


    }
}
