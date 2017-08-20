package com.example.btril.find_buddies;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.btril.find_buddies.data.FriendsDetails;
import com.example.btril.find_buddies.handlers.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by sanket on 8/2/2017.
 */

public class MyFriends extends AppCompatActivity {

    ListView lv;
    FriendAdapter friend_adapter;
    String emailFromSharedPref;
    //String mine_lat, mine_lon;
    FriendsDetails this_is_me;
    private SessionHandler session;
    private ImageView viewAllOnMap;
    public static ArrayList<FriendsDetails> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_friends);
        friendsList=new ArrayList<>();
        //List to be displayed
        ArrayList<FriendsDetails> friends_List = new ArrayList<FriendsDetails>();
        viewAllOnMap = (ImageView) findViewById(R.id.view_all_on_map);

        lv = (ListView) findViewById(R.id.friends_display_list);
        friend_adapter = new FriendAdapter(this, friends_List);
        //Binding listview to list
        lv.setAdapter(friend_adapter);

        session = new SessionHandler(getApplicationContext());
        //current user's email
        emailFromSharedPref = session.getPreferenceName().toString().trim();

        Log.v("renfce ", emailFromSharedPref);///debug
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                FriendsDetails f = (FriendsDetails) o;
                Intent myIntent = new Intent(MyFriends.this, MapFragment.class);
                myIntent.putExtra("email", f.getEmail()); //Optional parameters
                myIntent.putExtra("lat", Double.toString(f.getLatitude())); //Optional parameters
                myIntent.putExtra("lon", Double.toString(f.getLongitude())); //Optional parameters

                MyFriends.this.startActivity(myIntent);
            }
        });

        viewAllOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MyFriends.this, MapFragment.class);
                MyFriends.this.startActivity(myIntent);

            }
        });
    }

    public void fetch_data() {
        Fetch_Data c = new Fetch_Data();
        //run background task for fetching data
        c.execute();
    }

    public void connect_to_db(View view) {
        fetch_data();
    }

    public double calculate_distance(String my_lat, String my_lon, String friend_lat, String friend_lon) {

        Location my_location = new Location("");
        my_location.setLatitude(Double.parseDouble(my_lat));
        my_location.setLongitude(Double.parseDouble(my_lon));

        Location friend_location = new Location("");
        friend_location.setLatitude(Double.parseDouble(friend_lat));
        friend_location.setLongitude(Double.parseDouble(friend_lon));

        float distanceInMeters = my_location.distanceTo(friend_location);

        String x = String.format("%.2f", distanceInMeters / 1000);
        DecimalFormat df = new DecimalFormat();

        double distance = Double.parseDouble(x);

        return distance;
    }

    class Fetch_Data extends AsyncTask<Object, Object, ArrayList<FriendsDetails>> {
        private int byGetOrPost = 0;

        @Override
        protected void onPostExecute(ArrayList<FriendsDetails> friends_list) {
            super.onPostExecute(friends_list);

            friend_adapter.clear();

            if (friends_list.size() > 0) {
                viewAllOnMap.setVisibility(View.VISIBLE);
                friendsList.addAll(friends_list);

            } else {
                viewAllOnMap.setVisibility(View.GONE);
            }
            lv = (ListView) findViewById(R.id.friends_display_list);
            friend_adapter = new FriendAdapter(MyFriends.this, friends_list);
            //refresh and display the list
            lv.setAdapter(friend_adapter);
        }

        @Override
        protected ArrayList<FriendsDetails> doInBackground(Object... params) {

            ArrayList<FriendsDetails> friends_list_return = new ArrayList<FriendsDetails>();

            if (byGetOrPost == 0) {
                try {
                    String link = "http://nearbyfriendsapplogin.gear.host/include/get_friends.php?user_email=" + emailFromSharedPref;

                    HttpURLConnection c = null;

                    URL u = new URL(link);
                    c = (HttpURLConnection) u.openConnection();
                    c.setRequestMethod("GET");
                    c.setRequestProperty("Content-length", "0");
                    c.setUseCaches(false);
                    c.setAllowUserInteraction(false);
                    c.connect();
                    int status = c.getResponseCode();

                    switch (status) {
                        case 200:
                        case 201:
                            BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;

                            while ((line = br.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            Log.v("Answer", sb.toString());
                            JSONArray friendObject = new JSONArray(sb.toString());

                            JSONArray myLocationObject = getMyLocation(emailFromSharedPref);
                            Log.v("mylocation", String.valueOf(myLocationObject));

                            for (int i = 0; i < myLocationObject.length(); i++) {//Iterate through the current user's details
                                JSONObject jsonObject = myLocationObject.getJSONObject(i);
                                String friend_email = jsonObject.getString("email");
                                this_is_me = new FriendsDetails();
                                this_is_me.setEmail(friend_email);
                                this_is_me.setLatitude(Double.parseDouble(jsonObject.getString("lat")));
                                this_is_me.setLongitude(Double.parseDouble(jsonObject.getString("lon")));


                            }
                            for (int i = 0; i < friendObject.length(); i++) {

                                JSONObject jsonObject = friendObject.getJSONObject(i);
                                String friend_email = jsonObject.getString("email");
                                String lat = jsonObject.getString("lat");
                                String lon = jsonObject.getString("lon");
                                FriendsDetails f = new FriendsDetails();
                                f.setEmail(friend_email);
                                f.setLatitude(Double.parseDouble(lat));
                                f.setLongitude(Double.parseDouble(lon));
                                Log.v("me lat", Double.toString(this_is_me.getLatitude()));
                                Log.v("me lon", Double.toString(this_is_me.getLongitude()));
                                Log.v("your lat", lat);
                                Log.v("your lat", lon);

                                Double distance = calculate_distance(Double.toString(this_is_me.getLatitude()), Double.toString(this_is_me.getLongitude()), lat, lon);
                                f.setDistance(distance);
                                friends_list_return.add(f);

                            }
                            Log.v("String Builder", sb.toString());
                            br.close();
                    }
                } catch (Exception e) {
                    Log.v("String Builder", e.getMessage());
                }
            }
            return friends_list_return;
        }
    }

    private JSONArray getMyLocation(String emailFromSharedPref) {
        String link1 = "http://nearbyfriendsapplogin.gear.host/include/get_user_lat_lon.php?user_email=" + emailFromSharedPref;

        HttpURLConnection c1 = null;

        try {
            URL u1 = new URL(link1);
            c1 = (HttpURLConnection) u1.openConnection();
            c1.setRequestMethod("GET");
            c1.setRequestProperty("Content-length", "0");
            c1.setUseCaches(false);
            c1.setAllowUserInteraction(false);
            c1.connect();
            int status1 = c1.getResponseCode();

            switch (status1) {
                case 200:
                case 201:
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(c1.getInputStream()));
                    StringBuilder sb1 = new StringBuilder();
                    String line1;

                    while ((line1 = br1.readLine()) != null) {
                        sb1.append(line1 + "\n");
                    }
                    Log.v("Answer", sb1.toString());
                    JSONArray myLocationObject = new JSONArray(sb1.toString());
                    return myLocationObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


/*Handles click events for the action bar like */
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.signout) {
            session.setLogin(false);
            Intent i = new Intent(MyFriends.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
