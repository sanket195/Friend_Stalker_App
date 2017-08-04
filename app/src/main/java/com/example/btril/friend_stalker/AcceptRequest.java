package com.example.btril.friend_stalker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.btril.friend_stalker.data.Config;
import com.example.btril.friend_stalker.data.Controller;
import com.example.btril.friend_stalker.handlers.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ravi on 8/3/2017.
 */

public class AcceptRequest extends AppCompatActivity {
    private Button accpt,rjct;
    private TextView disp;

    private SessionHandler sessionHandler;
    public static final String LOG_TAG = AcceptRequest.class.getSimpleName();



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            startActivity(new Intent(this, SignInSuccess.class));
            return true;
        }

        if (id == R.id.signout) {
            sessionHandler.setLogin(false);
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_request);
        sessionHandler = new SessionHandler(getApplicationContext());

        disp= (TextView) findViewById(R.id.friendMailID);
        accpt = (Button) findViewById(R.id.accptReq);
        rjct = (Button) findViewById(R.id.rjctReq);
        fetchRequest(sessionHandler.getPreferenceName().toString().trim());


        accpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAccept();
                //perform accept thing
            }
        });

        rjct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //performDeny();
                //perform deny thing
            }
        });



    }


    private void performAccept(){
        final String myFriendEmail = disp.getText().toString();
        final String myEmail = sessionHandler.getPreferenceName().toString().trim();

        String tag_req = "acceptrequest_request";


        //method type,url,response
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Config.FRIEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");

                            if(!error){
                                String uid = jsonObject.getString("uid");
//                                final String freindEmail = uid;
                                disp.setText(uid.toString());
                                Log.d(LOG_TAG, "worked" + uid);
                            }
                            else{
                                String error_msg = jsonObject.getString("error_msg");
                                Log.e(LOG_TAG,"does not" + error_msg);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Friend accept request exception" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "error response in Friend accept request" +error.getMessage());

                    }
                }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "acceptrequest");
                p.put("myemail", myEmail);
                p.put("femail", myFriendEmail);
                return p;
            }

        };
        Controller.getController().addRequestQueue(stringRequest, tag_req);

    }

    private void fetchRequest(final String email) {

        String tag_req = "accept_request";


        //method type,url,response
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Config.FRIEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");

                            if(!error){
                                String uid = jsonObject.getString("uid");
//                                final String freindEmail = uid;
                                disp.setText(uid.toString());
                                Log.d(LOG_TAG, "worked" + uid);
                            }
                            else{
                                String error_msg = jsonObject.getString("error_msg");
                                Log.e(LOG_TAG,"does not" + error_msg);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Friend accept request exception" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "error response in Friend accept request" +error.getMessage());

                    }
                }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "accept");
                p.put("email", email);
                return p;
            }

        };
        Controller.getController().addRequestQueue(stringRequest, tag_req);
    }



}

