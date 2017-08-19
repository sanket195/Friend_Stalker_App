package com.example.btril.find_buddies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btril.find_buddies.handlers.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by btril on 8/1/2017.
 */

public class AcceptFriend extends AppCompatActivity {

    String mailID = null;
    String sharedPreferenceMail;
    private SessionHandler session;
    Button accptFriend;
    Button rjctFriend;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Handles item clicks on action bar including home and up button unless and until
        * we specify a parent activity in AndroidManifest.xml*/
        int id = item.getItemId();

        if (id == R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.signout) {
            session.setLogin(false);
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_friend);

        session = new SessionHandler(getApplicationContext());
        sharedPreferenceMail = session.getPreferenceName().toString().trim();//current user's email
        Log.v("shaed", sharedPreferenceMail);

        LongOperation lo = new LongOperation();
        lo.execute();

        accptFriend = (Button) findViewById(R.id.accept);
        rjctFriend = (Button) findViewById(R.id.reject);
        accptFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    String link = "http://nearbyfriendsapplogin.gear.host/include/accept_friend_true.php?user_email=" + sharedPreferenceMail + "&friend_email=" + mailID;

                    HttpURLConnection con = null;

                    URL u = new URL(link);
                    con = (HttpURLConnection) u.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Content-length", "0");
                    con.setUseCaches(false);
                    con.setAllowUserInteraction(false);
                    con.connect();
                    int status = con.getResponseCode();

                    switch (status) {
                        case 200:
                        case 201:
                            Log.v("accepted", "friend");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(AcceptFriend.this, "You have Accepted The Request", Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(AcceptFriend.this, SignInSuccess.class);
                startActivity(intent);
                finish();*/
                onBackPressed();

            }
        });
        rjctFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String link = "http://nearbyfriendsapplogin.gear.host/include/reject_friend_delete.php?user_email=" + sharedPreferenceMail + "&friend_email=" + mailID;

                    HttpURLConnection con = null;

                    URL u = new URL(link);
                    con = (HttpURLConnection) u.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Content-length", "0");
                    con.setUseCaches(false);
                    con.setAllowUserInteraction(false);
                    con.connect();
                    int status = con.getResponseCode();

                    switch (status) {
                        case 200:
                        case 201:
                            Log.v("deleted", "friend");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(AcceptFriend.this, "You have Rejected This Request", Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(AcceptFriend.this, SignInSuccess.class);
                startActivity(intent);
                finish();*/

                onBackPressed();
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        int byGetMethod = 0;

        @Override
        protected String doInBackground(String... params) {

            if (byGetMethod == 0) { 
                try {
                    String link = "http://nearbyfriendsapplogin.gear.host/include/accept_reject.php?user_email=" + sharedPreferenceMail;

                    HttpURLConnection con = null;

                    URL u = new URL(link);
                    con = (HttpURLConnection) u.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Content-length", "0");
                    con.setUseCaches(false);
                    con.setAllowUserInteraction(false);
                    con.connect();
                    int status = con.getResponseCode();

                    switch (status) {
                        case 200:
                        case 201:
                            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;

                            while ((line = br.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            Log.v("Answer", sb.toString());
                            JSONArray acceptObject = new JSONArray(sb.toString());
                            for (int i = 0; i < acceptObject.length(); i++) {
                                JSONObject single_friend = acceptObject.getJSONObject(i);

                                mailID = single_friend.getString("user_email");

                            }


                            br.close();
                    }
                } catch (Exception e) {
                    Log.v("String Builder", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView email_text = (TextView) findViewById(R.id.emailAccept);


            if (mailID != null) {
                email_text.setText(mailID);
            } else {
                Toast.makeText(AcceptFriend.this, "No Request To Display", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AcceptFriend.this, SignInSuccess.class);
                startActivity(intent);
                finish();
                //onBackPressed();
            }

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
