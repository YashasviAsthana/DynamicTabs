package com.projects.mara.dynamictabs;



import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Tabs extends AppCompatActivity {

    NonSwipeableViewPager simpleViewPager;
    TabLayout tabLayout;
    ArrayList<Tab> array;
    ArrayList<String> tabList;
    DrawerLayout drawerLayout;
    ListView drawerListView,drawerListStatic;
    PagerAdapter adapter;
    ActionBarDrawerToggle drawerToggle;
    ArrayAdapter<String> drawerListAdapter;
    ArrayList<Notification> notificationArray;
    Intent notif_intent;
    LinearLayout drawer_element;
    int notification_count=0;
    TextView notif_count;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        simpleViewPager = (NonSwipeableViewPager) findViewById(R.id.simpleViewPager);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
        getSupportActionBar().setTitle("Home");
        notif_count = (TextView)findViewById(R.id.notifcount);
        //View view = LayoutInflater.from(getApplication()).inflate(R.layout.drawer, null);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       // drawerLayout.setScrimColor(Color.rgb(0,0,0));
        drawer_element = (LinearLayout)findViewById(R.id.drawer_element);
        drawerListView = (ListView) findViewById(R.id.drawer_list_dynamic);
        drawerListStatic = (ListView) findViewById(R.id.drawer_list_static);
        relativeLayout = (RelativeLayout)findViewById(R.id.notif);
        ArrayList<String> staticList = new ArrayList<>();
        staticList.add("Element 1");
        staticList.add("Element 2");
        staticList.add("Element 2");
        staticList.add("Element 2");
        ArrayAdapter<String> staticAdapter = new ArrayAdapter<String>(this,R.layout.list_item,staticList);
        drawerListStatic.setAdapter(staticAdapter);
        array = new ArrayList<Tab>();
        tabList = new ArrayList<String>();
        notificationArray = new ArrayList<Notification>();
        drawerListAdapter = new ArrayAdapter<String>(this,R.layout.list_item,tabList);
        drawerListView.setAdapter(drawerListAdapter);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        notif_intent = new Intent(Tabs.this,Notifications.class);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(notif_intent);
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                mToolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            selectedItem(0);
        }
        connect();


        //static drawer click
        drawerListStatic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerLayout.closeDrawer(GravityCompat.START);
                if(i==0) startActivity(new Intent(Tabs.this,Element1.class));
                else if(i==1) startActivity(new Intent(Tabs.this,Element2.class));
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view,int position, long id){
            selectedItem(position);
            simpleViewPager.setCurrentItem(position);
        }
    }
    private void selectedItem(int position){
            drawerListView.setItemChecked(position,true);
            drawerLayout.closeDrawer(drawer_element);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tabs, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.quit:
                FragmentManager exit = getFragmentManager();
                ExitDialog exitDialog = new ExitDialog();
                exitDialog.show(exit, "exit");
                return true;
            case R.id.notifications:
                startActivity(notif_intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    Bundle args;
    private void connect(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("Tabs");
                            JSONArray jsonArrayNotif = jsonRootObject.optJSONArray("Notifications");
                            for(int i=0;i<jsonArray.length();i++){
                                Tab tab = new Tab();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                tab.setName(jsonObject.optString("name"));
                                tab.setUrl(jsonObject.optString("url"));
                                array.add(i,tab);
                            }
                            for(int i=0;i<jsonArrayNotif.length();i++){
                                Notification notification = new Notification();
                                JSONObject jsonObject = jsonArrayNotif.getJSONObject(i);
                                notification.setHeading(jsonObject.optString("heading"));
                                notification.setDesc(jsonObject.optString("desc"));
                                notificationArray.add(i,notification);
                                notification_count++;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        addTabs();
                        args = new Bundle();
                        args.putSerializable("NOTIFARRAY", (Serializable) notificationArray);
                        notif_intent.putExtra("Notif_Bundle", args);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addTabs(){
        int length = array.size();
        ArrayList<String> urls = new ArrayList<String>();
        for(int i=0;i<length;i++){
            tabLayout.addTab(tabLayout.newTab().setText(array.get(i).getName()));
            urls.add(array.get(i).getUrl());
            tabList.add(array.get(i).getName());
        }
        tabLayout.setOnTabSelectedListener(onTabSelectedListener(simpleViewPager));
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        adapter.setArray(urls);
        simpleViewPager.setAdapter(adapter);
        drawerListAdapter.notifyDataSetChanged();
        notif_count.setText(String.valueOf(notification_count));
        //notify_status(notification_count);
    }

    protected void notify_status(int count){

        if(count>0){
            NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(this);

            mbuilder.setSmallIcon(android.R.drawable.star_on);
            mbuilder.setContentTitle("New Notifications");
            mbuilder.setContentText("Tap to open.");

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notif_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),PendingIntent.FLAG_UPDATE_CURRENT);

            mbuilder.addExtras(args);
            mbuilder.setContentIntent(pendingIntent);
            int N_id = 001;
            NotificationManager nmgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nmgr.notify(N_id,mbuilder.build());
        }
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                selectedItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

}