package com.example.btril.friend_stalker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
 * Created by btril on 08/01/17.
 */

public class AddFriend extends AppCompatActivity {
    private Button addFriendButton;
    private EditText friendMailID;

    public static final String TAG = AddFriend.class.getSimpleName();
    private ProgressDialog progress;
    private SessionHandler sh;

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

            sh.setLogin(false);
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
        setContentView(R.layout.add_friend);
        friendMailID = (EditText) findViewById(R.id.addFriendEmail);
        addFriendButton = (Button) findViewById(R.id.addFriendButton);
        progress = new ProgressDialog(this);
        sh = new SessionHandler(getApplicationContext());
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Log.d(TAG, sh.getPreferenceName().toString());
                    addFriendEMail(friendMailID.getText().toString().trim(), sh.getPreferenceName().toString().trim());
                    Log.d(TAG, friendMailID.getText().toString());


            }
        });

    }

    private void addFriendEMail(final String friendMailID,final String myEmail) {
        if (friendMailID.length()>0) {
            progress.setMessage("Adding Friend In....");
            showDialog();

            //method type,url,response
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Config.FRIEND_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            hideDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean error = jsonObject.getBoolean("error");

                                if (!error) {
                                    String user_id = jsonObject.getString("user_id");
                                    Log.d(TAG, "EmailID id there" + user_id);
                                } else {
                                    String error_msg = jsonObject.getString("error_msg");
                                    Log.e(TAG, "Error adding Friend" + error_msg);
                                    Toast.makeText(AddFriend.this, error_msg, Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Error adding Friend" + error.getMessage());

                        }
                    }) {

                @Override
                public Map<String, String> getParams() {
                    Map<String, String> p = new HashMap<String, String>();
                    p.put("tag", "invite");
                    p.put("emailfriend", friendMailID);
                    p.put("emailmy", myEmail);
                    return p;
                }

            };
            Controller.getController().addRequestQueue(stringRequest, "invite_request");
            Intent i = new Intent(AddFriend.this, SignInSuccess.class);
            startActivity(i);
            finish();

        }else {
            Toast.makeText(this,"Please enter mail id",Toast.LENGTH_LONG).show();
        }
    }
    public void showDialog(){
        if(!progress.isShowing()){
            progress.show();
        }
    }

    public void hideDialog(){
        if(progress!=null&&progress.isShowing()){
            progress.dismiss();
        }
    }
}

