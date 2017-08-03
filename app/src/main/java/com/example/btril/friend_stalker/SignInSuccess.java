package com.example.btril.friend_stalker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.btril.friend_stalker.handlers.DrawerAdapter;
import com.example.btril.friend_stalker.handlers.SQLiteHandler;
import com.example.btril.friend_stalker.handlers.SessionHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by RAVI on 7/24/2017.
 */

public class SignInSuccess extends Activity {

    //UI
    public class NavigationItem {
        public String title;
        public String subtitle;
        public int icon;

        public NavigationItem(String title, String subtitle, int icon) {
            this.title = title;
            this.subtitle = subtitle;
            this.icon = icon;
        }
    }

    ListView drawerList;
    RelativeLayout drawerPane;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    ArrayList<NavigationItem> items = new ArrayList<NavigationItem>();


    public static final String LOG_TAG = SignInSuccess.class.getSimpleName();

    private TextView emailTV, nameTV;
    private Button logout;
    private Button invite,accept,listFriends;
    private SessionHandler session;
    private SQLiteHandler db;
    private TextView textView;

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
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //   Here, I took some help from the following // http://codetheory.in/android-navigation-drawer/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_signin);

        //UI
        // TODO add icons to the respective name (Trilok)
        /*items.add(new NavigationItem("Home", "Check out Freinds", R.drawable.app_icon1));
        items.add(new NavigationItem("Logout", "Say Bye to This App", R.drawable.app_icon2));
        items.add(new NavigationItem("About", "Get to know about us", R.drawable.app_icon3));*/

        // DrawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);


        // Populate the Navigtion Drawer with options
        drawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        drawerList = (ListView) findViewById(R.id.navList);


        DrawerAdapter adapter= new DrawerAdapter(this,items);
        drawerList.setAdapter(adapter);
       

        // Drawer Item click listeners
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });


        checkTheLoginAndDisplayLogoutButton();
    }
    public void checkTheLoginAndDisplayLogoutButton(){
        logout = (Button) findViewById(R.id.logout_btn);
        invite = (Button) findViewById(R.id.invite_btn);
        accept = (Button) findViewById(R.id.accept_btn);
        listFriends = (Button) findViewById(R.id.list_friends_btn);
        session = new SessionHandler(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        //if the session is loged in then i am moving the user to the detail activity
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String,String> userDeatils = db.getUserDetails();

        String name = userDeatils.get("name");
        String email = userDeatils.get("email");



        //if register is clicked
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInSuccess.this, AddFriend.class);
                startActivity(i);
                finish();

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInSuccess.this, AcceptFriend.class);
                startActivity(i);
                finish();

            }
        });

        listFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInSuccess.this, MyFriends.class);
                startActivity(i);
                finish();

            }
        });
    }


    private void logoutUser(){
        session.setLogin(false);
        db.deleteUser();
        Intent i = new Intent(SignInSuccess.this, LoginActivity.class);
        startActivity(i);
        finish();
    }



    //UI
    private void selectItemFromDrawer(int position) {

        switch (position){
            case 0: {
                Intent i = new Intent(SignInSuccess.this, SignInSuccess.class);
                startActivity(i);
                finish();
                break;
            }
            case 1:
            {
                logoutUser();
                break;
            }


        }

    }
}
