package com.example.btril.friend_stalker.data;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by juhi on 7/29/17.
 */

public class FetchLocation{
    public static final String LOG_TAG = FetchLocation.class.getSimpleName();
    GpsTrackingActivity gps;



    public FetchLocation(){

    }

    public void updateLocationTable(final String email,Context context){
        //selecting query for email if exsit then update the value or else INSERT
        double latitudeDouble =0.0,longitudeDouble = 0.0;

        gps = new GpsTrackingActivity(context) {
            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(gps.canGetLocation()) {
            latitudeDouble = gps.getLatitude();
            longitudeDouble = gps.getLongitude();
        }

        final String latitude = Double.toString(latitudeDouble);
        final String longitude = Double.toString(longitudeDouble);

        String tag_req = "location_request";

        //method type,url,response
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Config.LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String uid = jsonObject.getString("uid");
                            if(!error){
                                Log.d(LOG_TAG,"location entered without error" + uid);
                            }
                            else{
                                String error_msg = jsonObject.getString("error_msg");
                                Log.e(LOG_TAG,"location entered response"+error_msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "location entered error" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "error in location" +error.getMessage());

                    }
                }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "location");
                p.put("email", email);
                p.put("lat", latitude);
                p.put("lon", longitude);
                return p;
            }

        };
        Controller.getController().addRequestQueue(stringRequest,tag_req);


    }

}
