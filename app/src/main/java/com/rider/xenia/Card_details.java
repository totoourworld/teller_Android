package com.rider.xenia;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app_controller.AppController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.braintreepayments.api.Braintree;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Card_details extends Activity implements View.OnClickListener {

    protected static final String TAG = null;




    Button getdetails,cardback;
    TextView wait;
    private String CLIENT_TOKEN_FROM_SERVER;
    int REQUEST_CODE;
    String setemail,setmobile, setpassword;
    private JSONObject login_jsonobj,promo_jsonobj;
    private JSONObject clienttoken_jsonobj;
    private String login_status,promo_status;
    private String User_id;
    String WhoLogin;
    String paymentMethodNonce;

    public static final String PREFS_NAME = "MyPrefsFile";

    String regid,fbuserproimg,id;
    String Liveurl="";
    String regId="";
    Boolean tokenget=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        getActionBar().hide();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);
        regId=sharedPreferences.getString("RegId", null);


        Intent i= getIntent();
        User_id= i.getStringExtra("userid");
        fbuserproimg=i.getStringExtra("fbuserproimg");
        System.out.println("Credit card FB Profile image"+fbuserproimg);
        WhoLogin=i.getStringExtra("whologin");
        setpassword=i.getStringExtra("password");
        setemail=i.getStringExtra("email");


        getdetails=(Button)findViewById(R.id.button);
        cardback=(Button)findViewById(R.id.addpaymentback);
        wait=(TextView)findViewById(R.id.textView14);


        getdetails.setOnClickListener(this);

       get_token();
        //CLIENT_TOKEN_FROM_SERVER="eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiIwMjBhYjUyZGZjMmQ3MGFkNjk3MTQwNWYxYWNmMGY0YWFjYzViZGYxMDMzMDYyZDRlZjUwOTJlNzFlZmFiNmFlfGNyZWF0ZWRfYXQ9MjAxNS0xMi0xNlQwMDoxMToyOS44MDgxNTg1NjkrMDAwMFx1MDAyNm1lcmNoYW50X2lkPWN6c3k3eG5yYjY3ZjNrNjhcdTAwMjZwdWJsaWNfa2V5PW50dm1kM2tzZGZkeHh3a2ciLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvY3pzeTd4bnJiNjdmM2s2OC9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL2N6c3k3eG5yYjY3ZjNrNjgvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIn0sInRocmVlRFNlY3VyZUVuYWJsZWQiOmZhbHNlLCJwYXlwYWxFbmFibGVkIjpmYWxzZSwiY29pbmJhc2VFbmFibGVkIjpmYWxzZSwibWVyY2hhbnRJZCI6ImN6c3k3eG5yYjY3ZjNrNjgiLCJ2ZW5tbyI6Im9mZiJ9";

        Braintree.setup(this, CLIENT_TOKEN_FROM_SERVER, new Braintree.BraintreeSetupFinishedListener() {
            @Override
            public void onBraintreeSetupFinished(boolean setupSuccessful, Braintree braintree, String errorMessage, Exception exception) {
                if (setupSuccessful) {
                    // braintree is now setup and available for use

                } else {
                    // Braintree could not be initialized, check errors and try again
                    // This is usually a result of a network connectivity error
                }
            }
        });

        getdetails.setOnClickListener(this);

        cardback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                clearApplicationData();
                loadSavedPreferences();
                Intent fare = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(fare);
            }
        });


    }
    private void loadSavedPreferences() {
        //User has successfully logged in, save this information
        // We need an Editor object to make preference changes.

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 1);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();


    }

    @Override
    public void onClick(View v) {

if(tokenget) {
    wait.setVisibility(View.VISIBLE);

    Customization customization = new Customization.CustomizationBuilder()
            .submitButtonText("Submit")
            .build();


    Intent intent = new Intent(getApplicationContext(), BraintreePaymentActivity.class);
    intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, CLIENT_TOKEN_FROM_SERVER);
    intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
    startActivityForResult(intent, REQUEST_CODE);
}else
{
    get_token();
}

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case BraintreePaymentActivity.RESULT_OK:
                    paymentMethodNonce = data
                            .getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);

                    update();
                    getdetails.setVisibility(View.GONE);
                    wait.setVisibility(View.VISIBLE);;
                    //passtheNonce to your server–>payment gets completed here
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    // handle errors here, a throwable may be available in
                    break;
                default:
                    break;
            }
        }
    }




    public void update(){


        final String url=Liveurl+"update_auto_nonce?user_id="+User_id+"&paymentnonce="+paymentMethodNonce;
        System.out.println("URL is" + url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                login_jsonobj = response.getJSONObject(i);
                                login_status= login_jsonobj.getString("status");
                                Log.d("OUTPUT IS", login_status);

                                if(login_status.matches("Success")){


                                    get_promocode();
                                    Intent slide = new Intent(Card_details.this, SlideMainActivity.class);
                                    Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_LONG).show();

                                    slide.putExtra("userid", User_id);
                                    slide.putExtra("whologin",WhoLogin);
                                    slide.putExtra("password",setpassword);
                                    slide.putExtra("fbuserproimg",fbuserproimg);
                                    startActivity(slide);
                                    finish();
                                }
                                else if(login_status.matches("Invalid id")) {
                                    System.out.println("Insite the already exit");
                                    Toast toast=Toast.makeText(getApplicationContext(), "In Valid user id", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }


    public void get_token()
    {

        System.out.println("Inside get token");
        final String url=Liveurl+"getclienttoken";
        System.out.println("URL is" + url);
// Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

// Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                 clienttoken_jsonobj = response.getJSONObject(i);
                                CLIENT_TOKEN_FROM_SERVER =clienttoken_jsonobj.getString("token");
                                tokenget=true;
                                System.out.println("Client token from server inside the ger client token:"+CLIENT_TOKEN_FROM_SERVER);

                            } catch (JSONException e) {
                                e.printStackTrace();

// TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                    Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                    startActivity(i,bndlanimation);
                    finish();

                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }


    public void get_promocode()
    {

        System.out.println("Inside get promocode");
        final String url=Liveurl+"get_promocode?email="+setemail;
        System.out.println("URL is" + url);

// Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

// Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                promo_jsonobj = response.getJSONObject(i);
                                promo_status =promo_jsonobj.getString("status");
                                if(promo_status.matches("Send"))
                                {
                                    Toast.makeText(getApplication(),"Promo Code sent to E-mail",Toast.LENGTH_SHORT).show();
                                    System.out.println("Promo Code sent to E-mail");
                                }
                                else
                                {
                                    System.out.println("Promo Code not sent to E-mail");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

// TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
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

