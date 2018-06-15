package com.rider.xenia;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app_controller.AppController;
import com.custom.CustomProgressDialog;
import com.custom.view.XListView;
import com.ride.adapter.CustomRiderAdapter;
import com.utils.Constants;
import com.utils.ParseJson;
import com.utils.TripHistory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Myreview extends Activity {
    private AppController controller;

    private XListView listView;

    private String userid, fbuserproimg, checkpassword, WhoLogin;
    private static final String TAG = Myreview.class.getSimpleName();


    private CustomRiderAdapter adapter;
    private List<TripHistory> movieList = new ArrayList<TripHistory>();
    private CustomProgressDialog dialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreview);
        dialog=new CustomProgressDialog(Myreview.this);
        getActionBar().hide();
        controller = (AppController) getApplicationContext();
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        listView = (XListView) findViewById(R.id.listhistory);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listView.setXListViewListener(xListViewListener);

        handelIntent();
        if (net_connection_check()) {
            callwebservice(0, 10);
        }

        listView.setOnItemClickListener(itemClickListener);

    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()

    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent list = new Intent(getApplicationContext(), TripDetailActivity.class);
            list.putExtra("userid", userid);
            list.putExtra("fbuserproimg", fbuserproimg);
            list.putExtra("whologin", WhoLogin);
            list.putExtra("password", checkpassword);
            System.out.println("Profile Profile imag" + fbuserproimg);
            startActivity(list);
        }
    };

    public void handelIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            // intent data;
            userid = intent.getStringExtra("userid");
            fbuserproimg = intent.getStringExtra("fbuserproimg");
            checkpassword = intent.getStringExtra("password");
            WhoLogin = intent.getStringExtra("whologin");
        }
    }

    @OnClick(R.id.recancel)
    public void cancel(View view) {
        handelBack();
    }

    private XListView.IXListViewListener xListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore() {

        }
    };


    private void callwebservice(int offset, int limit) {
        showProgressBar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Urls.URL_USER_GET_TRIP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();
                        if (response != null) {
                            ParseJson parseJson = new ParseJson(Myreview.this);
                            movieList = parseJson.getTripHistory(response);
                            adapter = new CustomRiderAdapter(Myreview.this, movieList);
                            listView.setAdapter(adapter);
                        } else {
                            Toast.makeText(Myreview.this, R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        if (error instanceof NoConnectionError)
                            Toast.makeText(Myreview.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                        else if (error instanceof ServerError) {
                            String d = new String(error.networkResponse.data);
                            try {
                                JSONObject jso = new JSONObject(d);
                                Toast.makeText(Myreview.this, jso.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
                params.put(Constants.Keys.USER_ID, controller.pref.getUserID());
                params.put("is_request", "0");
                System.out.println("Params : " + params);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Myreview.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        dialog.dismiss();
    }

    private void showProgressBar() {
        dialog.showDialog();
    }


    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        handelBack();
    }

    public void handelBack() {
//        Intent can = new Intent(getApplicationContext(), SlideMainActivity.class);
//        can.putExtra("userid", userid);
//        can.putExtra("fbuserproimg", fbuserproimg);
//        can.putExtra("whologin", WhoLogin);
//        can.putExtra("password", checkpassword);
//        System.out.println("Profile Activity Fb Profile imag" + fbuserproimg);
//        startActivity(can);
        finish();
    }


    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {


            Toast toast = Toast.makeText(getApplicationContext(), R.string.there_is_no_connection, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 70);
            toast.show();
        }
        return connection;
    }

}
