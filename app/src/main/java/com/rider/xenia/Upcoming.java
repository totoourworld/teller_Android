package com.rider.xenia;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app_controller.AppController;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ride.adapter.CustomeFutureAdapter;
import com.ride.adapter.Movie1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Upcoming extends Activity {
    ListView    listView;
    String status,Liveurl1="",Liveurl="",userid;
    private static final String TAG = Allrides.class.getSimpleName();
    private ProgressDialog pDialog;
    JSONObject login_jsonobj,login_Error;
    String User_id,fbuserproimg,WhoLogin,checkpassword,location,pickupaddress,desinationbutton,accept,gpscountry,destcountry;
    CustomeFutureAdapter adapter;
    private List<Movie1> movieList = new ArrayList<Movie1>();
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);
        if( Build.VERSION.SDK_INT >= 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        tv=(TextView)findViewById(R.id.listemptyupcoming);
        Intent i= getIntent();
        userid= i.getStringExtra("userid");
        fbuserproimg=i.getStringExtra("fbuserproimg");
        WhoLogin=i.getStringExtra("whologin");
        checkpassword=i.getStringExtra("password");
        accept=i.getStringExtra("accept");
        desinationbutton=i.getStringExtra("desinationbutton");
        pickupaddress=i.getStringExtra("address");
        gpscountry=i.getStringExtra("gpscountry");

        User_id= i.getStringExtra("userid");
        fbuserproimg=i.getStringExtra("fbuserproimg");
        System.out.println("whologin upcoming="+WhoLogin);
        System.out.println("future rides user id==="+userid);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);
        Liveurl=sharedPreferences.getString("liveurl",null);
        listView = (ListView)findViewById(R.id.listView1);
        adapter = new CustomeFutureAdapter(this, movieList);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);

        System.out.println("after onclick listener");

        adapter.notifyDataSetChanged();

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request

        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.setContentView(R.layout.progress_dialog);
        callwebservice();

    }
    private void callwebservice()
    {

        final String url=Liveurl+"future?userid="+userid;
        //*******************************************ListView code start*****************************************************
        System.out.println("url in fututerides page==="+url);

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {


                                JSONObject obj = response.getJSONObject(i);
                                System.out.println("inseide  for loop");
                                status=obj.getString("status");
                                System.out.println("Satauskkkkkkkkkk"+status);
                                if(status.matches("failure"))
                                {

                                    tv.setVisibility(View.VISIBLE);
                                }
                                else if(status.matches("no data"))
                                {
                                    tv.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    tv.setVisibility(View.GONE);
                                    Movie1 movie = new Movie1();
                                    movie.setcarname(obj.getString("carname"));

                                    movie.setpickup(obj.getString("pickup"));

                                    movie.setdate1(obj.getString("pickupdate"));
                                    movie.settime1(obj.getString("pickuptime"));

                                    System.out.println("carname=="+obj.getString("carname"));
                                    System.out.println("carname=="+obj.getString("pickup"));
                                    // adding movie to movies array
                                    movieList.add(movie);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        adapter.notifyDataSetChanged();
                        hidePDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NoConnectionError) {
                    System.out.println("No internet connection");
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.there_is_no_connection, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 70);
                    toast.show();
                }
                VolleyLog.d(TAG, R.string.error + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

        System.out.println("after loop listview view future ");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }

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