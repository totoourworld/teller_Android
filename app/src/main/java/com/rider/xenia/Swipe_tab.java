package com.rider.xenia;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class Swipe_tab extends  TabActivity{

    String User_id,fbuserproimg,WhoLogin,checkpassword,location,pickupaddress,desinationbutton,accept,gpscountry,destcountry;
    private String[] tabs = { "ALLRIDES", "UPCOMMING","COMPLETED" };
    String userid1;
    Button backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_tab);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        Intent i1=getIntent();
        userid1=i1.getStringExtra("userid");
        fbuserproimg=i1.getStringExtra("fbuserproimg");
        WhoLogin=i1.getStringExtra("whologin");
        checkpassword=i1.getStringExtra("password");
        accept=i1.getStringExtra("accept");
        desinationbutton=i1.getStringExtra("desinationbutton");
        pickupaddress=i1.getStringExtra("address");
        gpscountry=i1.getStringExtra("gpscountry");

        User_id= i1.getStringExtra("userid");
        fbuserproimg=i1.getStringExtra("fbuserproimg");

        System.out.print("swipe tab userid==="+userid1);
        System.out.println("whologin swiptab="+WhoLogin);
        TabHost tabHost = getTabHost();
        TabSpec outboxSpec = tabHost.newTabSpec("All");

        outboxSpec.setIndicator(getTabIndicator1(tabHost.getContext(), "All Rides"));
        Intent outboxIntent = new Intent(this, Allrides1.class);
        outboxIntent.putExtra("userid", userid1);
        outboxIntent.putExtra("fbuserproimg",fbuserproimg);
        outboxIntent.putExtra("whologin",WhoLogin);
        outboxIntent.putExtra("password",checkpassword);
        outboxIntent.putExtra("location",location);
        outboxIntent.putExtra("accept",accept);
        outboxIntent.putExtra("desinationbutton", desinationbutton);
        outboxIntent.putExtra("address",pickupaddress );
        outboxIntent.putExtra("gpscountry", gpscountry);
        outboxSpec.setContent(outboxIntent);
        TabSpec outboxSpec1 = tabHost.newTabSpec("UpComing");
        outboxSpec1.setIndicator(getTabIndicator1(tabHost.getContext(), "UpComing"));
        Intent outboxIntent1 = new Intent(this, Upcoming.class);
        outboxIntent1.putExtra("userid", userid1);
        outboxIntent1.putExtra("fbuserproimg",fbuserproimg);
        outboxIntent1.putExtra("whologin",WhoLogin);
        outboxIntent1.putExtra("password",checkpassword);
        outboxIntent1.putExtra("location",location);
        outboxIntent1.putExtra("accept",accept);
        outboxIntent1.putExtra("desinationbutton", desinationbutton);
        outboxIntent1.putExtra("address",pickupaddress );
        outboxIntent1.putExtra("gpscountry", gpscountry);
        outboxSpec1.setContent(outboxIntent1);

        TabSpec outboxSpec2 = tabHost.newTabSpec("Completed");
        outboxSpec2.setIndicator(getTabIndicator(tabHost.getContext(), "Completed", R.drawable.pwd)); // new function to inject our own tab layout
        outboxSpec2.setIndicator(getTabIndicator1(tabHost.getContext(), "Completed"));
        Intent outboxIntent2 = new Intent(this, Completed1.class);
        outboxIntent2.putExtra("userid", userid1);
        outboxIntent2.putExtra("fbuserproimg",fbuserproimg);
        outboxIntent2.putExtra("whologin",WhoLogin);
        outboxIntent2.putExtra("password",checkpassword);
        outboxIntent2.putExtra("location",location);
        outboxIntent2.putExtra("accept",accept);
        outboxIntent2.putExtra("desinationbutton", desinationbutton);
        outboxIntent2.putExtra("address",pickupaddress );
        outboxIntent2.putExtra("gpscountry", gpscountry);
        outboxSpec2.setContent(outboxIntent2);



        tabHost.addTab(outboxSpec);
        tabHost.addTab(outboxSpec1);
        tabHost.addTab(outboxSpec2);

        tabHost.setCurrentTab(0);

        backbutton = (Button) findViewById(R.id.myridesback);
         backbutton.setOnClickListener(new View.OnClickListener() {

             public void onClick(View v) {
                 // TODO Auto-generated method stub
                 Intent back=new Intent(getApplicationContext(),SlideMainActivity.class);
                 back.putExtra("userid", User_id);
                 back.putExtra("fbuserproimg",fbuserproimg);
                 back.putExtra("whologin",WhoLogin);
                 back.putExtra("password", checkpassword);
                 System.out.println("Slide Main Activity WhoLogin"+WhoLogin);
                 back.putExtra("accept",accept);
                 startActivity(back);
                 finish();
             }
         });

    }


    private View getTabIndicator(Context context, String title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        ;

        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);

        return view;
    }
    private View getTabIndicator1(Context context, String title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);

        return view;
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent can=new Intent(getApplicationContext(),SlideMainActivity.class);
        can.putExtra("userid", User_id);
        can.putExtra("fbuserproimg",fbuserproimg);
        can.putExtra("whologin",WhoLogin);
        can.putExtra("password",checkpassword);
        can.putExtra("location",location);
        can.putExtra("accept",accept);
        can.putExtra("desinationbutton", desinationbutton);
        can.putExtra("address",pickupaddress );
        can.putExtra("gpscountry", gpscountry);

        startActivity(can);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent can=new Intent(getApplicationContext(),SlideMainActivity.class);
                can.putExtra("userid", User_id);
                can.putExtra("fbuserproimg",fbuserproimg);
                can.putExtra("whologin",WhoLogin);
                can.putExtra("password",checkpassword);
                can.putExtra("location",location);
                can.putExtra("accept",accept);
                can.putExtra("desinationbutton", desinationbutton);
                can.putExtra("address",pickupaddress );
                can.putExtra("gpscountry", gpscountry);
                startActivity(can);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {
            dialogcalling();
        }
        return connection;
    }

    public void dialogcalling() {
        final Dialog dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar);

        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);

        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);

        dialog.show();

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });
        handler.postDelayed(runnable, 3000);
    }
}