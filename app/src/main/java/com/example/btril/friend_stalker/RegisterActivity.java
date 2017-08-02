package com.example.btril.friend_stalker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.btril.friend_stalker.handlers.SQLiteHandler;
import com.example.btril.friend_stalker.handlers.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deeksha on 7/21/2017.
 */

public class RegisterActivity extends Activity {

    private EditText fullname, email, password;
    private Button register; // created a button object
    private Button signinto;
    private ProgressDialog progressDialog;

    private SessionHandler session;
    private SQLiteHandler handler;


    public static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        fullname = (EditText) findViewById(R.id.register_fullname);
        email = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        register = (Button) findViewById(R.id.register_button);
        signinto = (Button) findViewById(R.id.sign_into);


        progressDialog = new ProgressDialog(this);
        session = new SessionHandler(getApplicationContext());
        handler = new SQLiteHandler(getApplicationContext());

        if(session.isLoggedIn())
        {
            Intent intent = new Intent(this,SignInSuccess.class);
            startActivity(intent);
            finish();
        }

        signinto.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = fullname.getText().toString();
                String emailid = email.getText().toString();
                String pswd = password.getText().toString();

                /*Checks if user has entered information in all three mandatory fields
                * else shows an error for each value that has not been assigned yet*/
                if (name.isEmpty()) {
                    fullname.setError("Mandatory Field");
                } else if (emailid.isEmpty()) {
                    email.setError("Mandatory Field");
                } else if (pswd.isEmpty()) {
                    password.setError("Mandatory Field");
                } else {
                    register_validate(name,emailid,pswd);
                    //TODO implement the fetchlocationupdate methods over here
                                  }
            }
        });
    }

    private void register_validate(final String name, final String emailid, final String pswd) {

        //Validation for name emailid and password
        String reg_requst="register_request";
        progressDialog.setMessage("Will Register in  ...");
        showDialog();

        final StringRequest stringRequest = new StringRequest (Request.Method.POST,
                Config.REGISTER_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            if(!error)
                            {
                                String uid = jsonObject.getString("uid");

                                JSONObject userJsonObject = jsonObject.getJSONObject("user");
                                String name = userJsonObject.getString("name");
                                String email = userJsonObject.getString("email");
                                String time_on = userJsonObject.getString("time_on");
                                handler.addUser(name, email, uid, time_on);
                                session.setLogin(true);
                                session.setPreferenceName(email);
                                Intent intent = new Intent(RegisterActivity.this,
                                        SignInSuccess.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                String errmsg = jsonObject.getString("error_msg");
                                Toast.makeText(RegisterActivity.this,errmsg,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "register");
                p.put("name", name);
                p.put("email", emailid);
                p.put("password", pswd);
                return p;
            }

        };
        Controller.getController().addRequestQueue(stringRequest,reg_requst);
    }


    public void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}