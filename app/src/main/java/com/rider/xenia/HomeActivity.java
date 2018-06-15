package com.rider.xenia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app_controller.AppController;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class HomeActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Button Signin, Register;

    private GoogleCloudMessaging _gcm;
    int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 1;

    String LiveUrl = "";
    String LiveUrl1 = "";
    String LiveUrl2 = "";
    String RiderCurrentStatus = "";
    String imei = "";

    protected static final String TAG = "HomeActivity";

    JSONObject driverdetails_jsonobj;
    String driverdetailsjson_status, fbkey, googleapikey, googleprojectid, paypalclienttoken, paypaltype;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    GoogleApiClient googleApiClient = null;

    private String prefkey;
    private String users;
    private String getfbtkn;
    private String getfbid;
    private String Whologin;

    String fbuserproimg;
    String PREF_KEY;
    String WhoLogin;
    String regId = null, RegId;

    Controller aController;
//    AsyncTask<Void, Void, Void> mRegisterTask;

    //SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MultiDex.install(this);
        Fabric.with(this, new Crashlytics());

        getActionBar().hide();

        LiveUrl = getResources().getString(R.string.LiveUrl);
        LiveUrl1 = getResources().getString(R.string.LiveUrl1);
        LiveUrl2 = getResources().getString(R.string.LiveUrl2);
        RiderCurrentStatus = "Start";
        MultiDex.install(this);
       /* Signin = (Button) findViewById(R.id.signin);
        Register = (Button) findViewById(R.id.register);*/

        checkconnection();

        new GetDeviceID().execute();

        SharedPreferences settings = getSharedPreferences(SigninActivity.PREFS_NAME, 1);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean trLoggedIn = settings.getBoolean("trLoggedIn", false);

        PREF_KEY = settings.getString("euser", prefkey);
        System.out.println("Pref Key" + PREF_KEY);

        String fb_tkn = settings.getString("ftkn", getfbtkn);
        System.out.println("Pref Key" + fb_tkn);

        String fbu_id = settings.getString("fuser", getfbid);
        System.out.println("Pref Key" + fbu_id);

        String user1 = settings.getString("sinuser", users);
        System.out.println("User" + user1);

        WhoLogin = settings.getString("whologin", Whologin);
        System.out.println("Who Login" + WhoLogin);

        fbuserproimg = settings.getString("fbuserproimg", fbuserproimg);
        System.out.println("Who Login" + WhoLogin);

        if (fb_tkn != null) {
            //Go directly to main activity
            Intent i = new Intent(getApplicationContext(), SlideMainActivity.class);
            i.putExtra("userid", fbu_id);
            i.putExtra("fb_token", fb_tkn);
            i.putExtra("whologin", WhoLogin);
            System.out.println("Fb WhoLogin" + WhoLogin);
            i.putExtra("fbuserproimg", fbuserproimg);
            System.out.println("FB Profile Image" + fbuserproimg);
            startActivity(i);
            finish();

        }

        if (PREF_KEY != null) {
            //Go directly to main activity
            Intent i = new Intent(getApplicationContext(), SlideMainActivity.class);
            i.putExtra("userid", PREF_KEY);
            i.putExtra("whologin", WhoLogin);
            System.out.println("Insite the Pref Key");
            System.out.println("WhoLogin" + WhoLogin);
            i.putExtra("fbuserproimg", fbuserproimg);
            System.out.println("Pref_key Profile Image" + fbuserproimg);

            startActivity(i);
            finish();

        }

        if (user1 != null) {
            //Go directly to main activity
            Intent i = new Intent(getApplicationContext(), SlideMainActivity.class);
            i.putExtra("userid", user1);
            i.putExtra("whologin", WhoLogin);
            System.out.println("Signin WhoLogin" + WhoLogin);
            i.putExtra("fbuserproimg", fbuserproimg);
            System.out.println("USer Profile Image" + fbuserproimg);
            startActivity(i);
            finish();

        }

        Register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (regId == null || regId.equals(null)) {
                    new GetDeviceID().execute();

                } else {
                    Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
                    register.putExtra("regId", regId);
                    startActivity(register);
                    finish();
                }
            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (regId == null || regId.equals(null)) {
                    new GetDeviceID().execute();

                } else {
                    Intent signin = new Intent(getApplicationContext(), SigninActivity.class);
                    signin.putExtra("regId", regId);
                    startActivity(signin);
                    finish();
                }
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        try {

            //Clear internal resources.
            GCMRegistrar.onDestroy(getApplicationContext());

        } catch (Exception e) {
            Log.e("UnRegisterReceiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    public void checkconnection() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();

        if (!connection) {
            android.support.v7.app.AlertDialog.Builder builder =
                    new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

            // Setting Dialog Title
            builder.setTitle(R.string.network_connection_error);

            // Setting Dialog Message
            builder.setMessage(R.string.there_is_no_connection);

            // Setting Icon to Dialog

            // Setting Positive "Yes" Btn
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            int pid = android.os.Process.myPid();
                            android.os.Process.killProcess(pid);
                            System.exit(0);
                            finish();
                        }
                    });

            // Showing Alert Dialog
            builder.show();
        } else {

            if (googleApiClient == null) {
                googleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                googleApiClient.connect();
                settingsrequest();

            }
        }
    }

    public void settingsrequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            Signin.setEnabled(false);
                            Register.setEnabled(false);
                            status.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Signin.setEnabled(true);
                        Register.setEnabled(true);
                        break;
                    case Activity.RESULT_CANCELED:
                        HomeActivity.this.finish();
                        System.exit(0);
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(((Object) this).getClass().getSimpleName(),
                "onConnectionSuspended() called, whatever that means");
    }

    private class GetDeviceID extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // progress bar
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if (_gcm == null) {
                _gcm = GoogleCloudMessaging.getInstance(HomeActivity.this);
            }

            try {
                regId = _gcm.register(Config.GOOGLE_SENDER_ID);
                System.out.println("my id is\n" + regId + "\n " + regId.length());
                // getkeys();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            //dismiss dialouge here
            int currentapiVersion = Build.VERSION.SDK_INT;
            System.out.println("Current API Version is " + currentapiVersion);
            System.out.println("Check API version is" + Build.VERSION_CODES.M);
            if (currentapiVersion >= Build.VERSION_CODES.M) {
                // Do something for lollipop and above versions
                int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                int hasSMSPermission = checkSelfPermission(Manifest.permission.SEND_SMS);
                int hasAccessLocation = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                int hasGetAccounts = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
                int hasInternet = checkSelfPermission(Manifest.permission.INTERNET);
                int hasAccessNetwork = checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
                int hasAccounts = checkSelfPermission(Manifest.permission.ACCOUNT_MANAGER);
                int hasCamera = checkSelfPermission(Manifest.permission.CAMERA);
                int hasVibrate = checkSelfPermission(Manifest.permission.VIBRATE);
                int hasReadContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
                int hasWriteContacts = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
                int hasReadStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                int hasWriteStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int hasWakeLock = checkSelfPermission(Manifest.permission.WAKE_LOCK);
                int hasChangeNetwork = checkSelfPermission(Manifest.permission.CHANGE_NETWORK_STATE);
                int hasphonestate = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);

                int callphone = checkSelfPermission(Manifest.permission.CALL_PHONE);
                List<String> permissions = new ArrayList<String>();
                if (hasCamera != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.CAMERA);
                }
                if (callphone != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.CALL_PHONE);
                }
                if (hasVibrate != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.VIBRATE);
                }
                if (hasReadContacts != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.READ_CONTACTS);
                }
                if (hasReadStorage != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }

                if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }

                if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.SEND_SMS);
                }

                if (hasAccessLocation != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
                if (hasGetAccounts != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.GET_ACCOUNTS);
                }

                if (hasInternet != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.INTERNET);
                }

                if (hasAccessNetwork != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
                }

                if (hasAccounts != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.ACCOUNT_MANAGER);
                }

                if (hasChangeNetwork != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.CHANGE_NETWORK_STATE);
                }

                if (hasWakeLock != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.WAKE_LOCK);
                }
                if (hasWriteStorage != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                if (hasphonestate != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.READ_PHONE_STATE);
                }
                if (!permissions.isEmpty()) {
                    requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);

                }

                if (hasphonestate == PackageManager.PERMISSION_GRANTED) {
                    getIMEI();

                } else {
                    new GetDeviceID().execute();
                }
            } else {
                getIMEI();
            }

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
        window.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

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

    public void loadsavedpreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();
        editor.putString("liveurl", LiveUrl);
        editor.putString("liveurl1", LiveUrl1);
        editor.putString("liveurl2", LiveUrl2);

        System.out.println("RegId IS Store" + regId);
        editor.putString("RegId", regId);
        editor.putString("IMEI", imei);
        editor.putString("fbkey", fbkey);
        editor.putString("googleapikey", googleapikey);
        editor.putString("googleprojectid", googleprojectid);
        editor.putString("paypalclienttoken", paypalclienttoken);
        editor.putString("paypaltype", paypaltype);
        editor.putString("RiderCurrentStatus", RiderCurrentStatus);
        // editor.putString("userid",shUser_id);

        editor.commit();
        String restoredText = sharedPreferences.getString("liveurl", null);
        System.out.println("Liveurl value--->" + restoredText);

    }

    public void getIMEI() {
        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = mngr.getDeviceId();
        System.out.println("IMEI NUMBER" + imei);

        getkeys();
    }

    public void getkeys() {
        final String url = LiveUrl + "display_connect";
        System.out.println("URL is" + url);


        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                driverdetails_jsonobj = response.getJSONObject(i);

                                driverdetailsjson_status = driverdetails_jsonobj.getString("status");

                                System.out.println("Get Category details " + response);
                                Log.d("OUTPUT IS", driverdetailsjson_status);
                                if (driverdetailsjson_status.matches("Success")) {

                                    fbkey = driverdetails_jsonobj.getString("fb_api_key");
                                    googleapikey = driverdetails_jsonobj.getString("google_api_key");
                                    googleprojectid = driverdetails_jsonobj.getString("google_project_id");

                                    paypalclienttoken = driverdetails_jsonobj.getString("paypalclienttoken");
                                    paypaltype = driverdetails_jsonobj.getString("paymenttype");

                                    FacebookSdk.setApplicationId(fbkey);

                                    loadsavedpreferences();


                                    System.out.println("User details updated successfully");


                                } else {

                                    System.out.println("User details not updated");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {

                    dialogcalling();
                }
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


}