package com.example.btril.find_buddies.data;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by btril on 7/29/17.
 */

/*used to get and put request*/
public class Controller extends Application {
    public static final String LOG_TAG = Controller.class.getSimpleName();
    private RequestQueue requestQueue;
    private static Controller controller;

    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;
    }

    public static synchronized Controller getController(){
        return controller;
    }

    // gets the request
    public RequestQueue getRequestQueue(){
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }

    // adding the request
    public <T> void addRequestQueue(Request<T> req) {
        req.setTag(LOG_TAG);
        getRequestQueue().add(req);
    }

    public <T> void addRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? LOG_TAG : tag);
        getRequestQueue().add(req);
    }

    public void cancelPending(Object tag) {
        if(requestQueue == null)
            requestQueue.cancelAll(tag);
    }

}
