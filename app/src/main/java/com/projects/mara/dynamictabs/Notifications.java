package com.projects.mara.dynamictabs;

import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {

    ListView listView;
    TextView emptyText;
    List<Notification> notificationArrayList;
    NotificationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        //Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_notif);
        //setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Notifications");
        emptyText = (TextView)findViewById(R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);
        listView = (ListView)findViewById(R.id.notif_listView);

        Bundle args = getIntent().getBundleExtra("Notif_Bundle");
        try {
            notificationArrayList = (ArrayList<Notification>) args.getSerializable("NOTIFARRAY");
        }catch (Exception e){
            e.printStackTrace();
            notificationArrayList = null;
        }
        if(notificationArrayList==null){
            listView.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        }else {
            adapter = new NotificationAdapter(notificationArrayList, this);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    /*
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
}

class NotificationAdapter extends ArrayAdapter<Notification>{

    private List<Notification> dataSet;
    Context context;

    private static class ViewHolder{
        TextView heading,desc;
    }
    public NotificationAdapter(List<Notification> data, Context context){
        super(context,R.layout.single_notif,data);
        this.dataSet = data;
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Notification notification = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_notif,parent,false);
            viewHolder.heading = (TextView)convertView.findViewById(R.id.heading);
            viewHolder.desc = (TextView)convertView.findViewById(R.id.desc);

            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.heading.setText(notification.getHeading());
        viewHolder.desc.setText(notification.getDesc());
        return convertView;
    }
}
