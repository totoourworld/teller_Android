package com.rider.xenia;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RegionSearchActivity extends Activity {


    String regionsearchstatus;
    Switch regionsearch;
    Button regcancel;
    String User_id,fbuserproimg,WhoLogin,checkpassword,accept;
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_search);
        getActionBar().hide();

        Intent i= getIntent();
        User_id= i.getStringExtra("userid");
        fbuserproimg=i.getStringExtra("fbuserproimg");
        WhoLogin=i.getStringExtra("whologin");
        System.out.println("Insite the Slide Main Activity WhoLogin"+WhoLogin);
        checkpassword=i.getStringExtra("password");
        accept=i.getStringExtra("accept");
        regionsearchstatus="OFF";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        regionsearchstatus=sharedPreferences.getString("regionsearchstatus",null);

        System.out.println("regionsearchstatus0 Driver details funcation" + regionsearchstatus);

        regionsearch = (Switch) findViewById(R.id.regionsearch);

        if(regionsearchstatus==null||regionsearchstatus.equals("OFF")) {
            //set the switch to ON
            regionsearch.setChecked(false);
            regionsearchstatus="OFF";
            System.out.println("regionsearchstatus1 Driver details funcation" + regionsearchstatus);

        }else
        {
            regionsearch.setChecked(true);
            regionsearchstatus="ON";
            System.out.println("regionsearchstatus2 Driver details funcation" + regionsearchstatus);

        }
        //attach a listener to check for changes in state
        regionsearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    regionsearchstatus="ON";
                    System.out.println("regionsearchstatus3 Driver details funcation" + regionsearchstatus);

                }else{
                    regionsearchstatus="OFF";
                    System.out.println("regionsearchstatus4 Driver details funcation" + regionsearchstatus);

                }

            }
        });

        //check the current state before we display the screen
        if(regionsearch.isChecked()){
            regionsearchstatus="ON";
            System.out.println("regionsearchstatus5 Driver details funcation" + regionsearchstatus);

        }
        else {
            regionsearchstatus="OFF";
            System.out.println("regionsearchstatus6 Driver details funcation" + regionsearchstatus);

        }


        regcancel=(Button)findViewById(R.id.regcancel);

        regcancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent search=new Intent(getApplicationContext(),SlideMainActivity.class);
                search.putExtra("userid", User_id);
                search.putExtra("fbuserproimg",fbuserproimg);

                search.putExtra("whologin",WhoLogin);
                search.putExtra("password", checkpassword);
                System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
                search.putExtra("accept", accept);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //Set "hasLoggedIn" to true
                editor.putString("regionsearchstatus",regionsearchstatus );

                // Commit the edits!
                editor.commit();

                startActivity(search);
                finish();
            }
        });
    }
}
