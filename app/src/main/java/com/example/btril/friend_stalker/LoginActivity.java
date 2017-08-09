package com.example.btril.friend_stalker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
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
import com.example.btril.friend_stalker.handlers.FetchLocation;
import com.example.btril.friend_stalker.handlers.SQLiteHandler;
import com.example.btril.friend_stalker.handlers.SessionHandler;
import com.google.android.gms.common.api.GoogleApiClient;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private static final int REQUEST_READ_CONTACTS = 0;


    //Keep track of the login task to ensure we can cancel it if requested.

    private ProgressDialog progress;

    // Session and Database Handlers
    private SessionHandler sessionHandler;
    private SQLiteHandler sdb;

    // UI references.
    private EditText loginEmail, loginPass;
    private Button signin, register;

    private View mProgressView;
    private View mLoginFormView;

    // Auto-generated to implement API App Indexing

    private GoogleApiClient googleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        invalidateOptionsMenu();
        progress = new ProgressDialog(this);
        sessionHandler = new SessionHandler(getApplicationContext());
        sdb = new SQLiteHandler(getApplicationContext());

        // Set up the login form.
        loginEmail = (EditText) findViewById(R.id.email);
        loginPass = (EditText) findViewById(R.id.password);

        signin = (Button) findViewById(R.id.signin_user);

        register = (Button) findViewById(R.id.register_user);

        // On Clicking the Register Button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add code to go to link register activity
                Intent intent = new Intent((LoginActivity.this), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String password = loginPass.getText().toString();
                boolean cancel = false;

                // initially setting the errors to null
                loginEmail.setError(null);
                loginPass.setError(null);

                // check if the email and password fields are filled in or empty
                if (!email.isEmpty() && !password.isEmpty()) {
                    checkValidation(email, password);

                    FetchLocation updateLocation = new FetchLocation();
                    updateLocation.updateLocationTable(sessionHandler.getPreferenceName().toString().trim(), LoginActivity.this);

                } else {
                    loginEmail.setError(getString(R.string.error_invalid_email));
                    loginPass.setError(getString(R.string.error_invalid_password));
                }
            }
        });

        if (sessionHandler.isLoggedIn()) {
            Intent intent = new Intent(this, SignInSuccess.class);
            startActivity(intent);
            finish();
        }

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */


//TODO add validation code
    public void checkValidation(String userEmail, final String userPass) {
        final String mail_id = userEmail.toString().trim();
        final String pass = userPass.toString().trim();

        progress.setMessage("Will be logging in...");
        showDialog();

        StringRequest str_req = new StringRequest(Request.Method.POST,
                Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            if (!error) {
                                String uid = jsonObject.getString("uid");

                                JSONObject userJsonObject = jsonObject.getJSONObject("user");
                                String name = userJsonObject.getString("name");
                                String email = userJsonObject.getString("email");
                                String created_at = userJsonObject.getString("created_at");
                                //db.addUser(name,email,uid,created_at);

                                sessionHandler.setLogin(true);
                                sessionHandler.setPreferenceName(email);
                                Log.d(TAG, "pref email    " + sessionHandler.getPreferenceName());

                                Intent i = new Intent(LoginActivity.this, SignInSuccess.class);
                                startActivity(i);
                                finish();
                            } else {
                                String error_msg = jsonObject.getString("error_msg");
                                Toast.makeText(LoginActivity.this, error_msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error response");

                    }
                }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "login");
                p.put("email", mail_id);
                p.put("password", pass);
                return p;
            }

        };
        Controller.getController().addRequestQueue(str_req, "login_request");
    }

    private void showDialog() {
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    public void hideDialog() {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

