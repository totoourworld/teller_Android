package com.rider.xenia;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.app_controller.AppController;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class SosActivity extends Activity {


    private static String APP_ID = "742278215809543"; // Replace your App ID here

    // Instance of Facebook Class
    @SuppressWarnings("deprecation")
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;

    //    Button sms, email, fb;
    //	TextView settings;
    String User_id, fbuserproimg, WhoLogin, checkpassword, accept;


    URL alert;


    ShareDialog shareDialog;
    CallbackManager callbackManager = new CallbackManager() {
        @Override
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            return false;
        }
    };
    String lat, lng;
    String provider, Liveurl, email_status, contact_status;
    String get_email1, get_email2, get_email3;
    String get_contact1, get_contact2, get_contact3;
    protected static final String TAG = "Settings";
    private JSONObject contact_jsonobj, email_jsonobj;
    private AppController controller;
    private DriverInfo userInfo;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        getActionBar().hide();
        ButterKnife.bind(this);



    }

    @Override
    protected void onResume() {
        super.onResume();
        controller = (AppController) getApplication();
        ParseJson parseJson = new ParseJson(this);
        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);
        System.out.println("The Live URL Is " + Liveurl);


        Intent i = getIntent();
        User_id = i.getStringExtra("userid");
        fbuserproimg = i.getStringExtra("fbuserproimg");
        WhoLogin = i.getStringExtra("whologin");
        System.out.println("Insite the Slide Main Activity WhoLogin" + WhoLogin);
        checkpassword = i.getStringExtra("password");
        accept = i.getStringExtra("accept");
        lat = i.getStringExtra("lat");
        lng = i.getStringExtra("long");

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new

                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });
    }

    @OnClick(R.id.sos_alert_via_facebook)
    public void alertViaFacebook(View view)

    {
        if (net_connection_check()) {
            if (ShareDialog.canShow(ShareLinkContent.class)) {

                try {
                    alert = new URL("http://maps.google.com?q=" + lat + "," + lng);
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(getString(R.string.alert))
                            .setContentDescription(getString(R.string.please_help_i_am_in_danger) + alert)
                            .setImageUrl(Uri.parse("http://callmum.com/images/help1.png"))
                            .setContentUrl(Uri.parse("http://maps.google.com?q=" + lat + "," + lng))
                            .build();
                    shareDialog.show(linkContent);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @OnClick(R.id.sos_alert_via_email)
    public void alertViaEmail(View view)

    {
        if (net_connection_check()) {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{userInfo.getEmergency_email_1(), userInfo.getEmergency_email_2(), userInfo.getEmergency_email_3()});
            email.putExtra(Intent.EXTRA_SUBJECT, "SOS Alert");
            String url = "http://maps.google.com?q=" + lat + "," + lng;
            email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Please help, I am in danger and need assistance.Follow my location,<a href=\"" + url + "\">Click Here</a> \n " + url));
            email.setType("text/html");
            startActivity(Intent.createChooser(email, getString(R.string.choose_an_email_client)));
        }
    }


    @OnClick(R.id.sos_alert_via_sms)
    public void alertViaSMS(View view)

    {
        boolean result = checkPermission();
        if (result) {
            prepareSmsNo();
        }
    }

    private void prepareSmsNo() {
        if (net_connection_check()) {


        ArrayList<String> contacts = new ArrayList<>();
        if (userInfo.getEmergency_contact_1() != null) {
            contacts.add(userInfo.getEmergency_contact_1());
        }
        if (userInfo.getEmergency_contact_2() != null) {
            contacts.add(userInfo.getEmergency_contact_2());
        }
        if (userInfo.getEmergency_contact_3() != null) {
            contacts.add(userInfo.getEmergency_contact_3());
        }

        String message = "Please help, I am in danger and need assistance.Follow my location, http://maps.google.com?q=" + lat + "," + lng;
        // the phone numbers we want to send to
        for (String number : contacts) {
            System.out.println("get contact number1" + number);
            if (number.length() > 1) {
                try{
                    if(number.equals(null)){
                        Toast.makeText(getBaseContext(), R.string.message_sent_successfully,
                                Toast.LENGTH_SHORT).show();
                    }else{

                        sendSMSMessage(number, message);
                    }

                }catch (Exception e){
                    Toast.makeText(getBaseContext(), R.string.message_no_found,
                            Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    }


    @OnClick(R.id.sos_settings)
    public void onClickSetting(View view) {

        // TODO Auto-generated method stub
        Intent settings = new Intent(SosActivity.this, Settings.class);
        settings.putExtra("userid", User_id);
        settings.putExtra("fbuserproimg", fbuserproimg);
        settings.putExtra("whologin", WhoLogin);
        settings.putExtra("password", checkpassword);
        settings.putExtra("accept", accept);
        settings.putExtra("lat", lat);
        settings.putExtra("long", lng);
        startActivity(settings);


    }

    @OnClick(R.id.recancel)
    public void back(View view) {
        onBackPressed();
    }

    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(SosActivity.this,android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SosActivity.this, android.Manifest.permission.SEND_SMS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SosActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Send SMS permission is necessary to send message.");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SosActivity.this, new String[]{android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)SosActivity.this, new String[]{android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    prepareSmsNo();
                } else {
                    //code for deny
                }
                break;
        }
    }





    private void sendSMSMessage(String phoneNo1, String message1) {


        final Dialog dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar);

        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);

        dialog.show();


        try {


            System.out.println("SMS SEND TO Phone number " + phoneNo1);
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";
            System.out.println("In site the send message");
            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                    new Intent(DELIVERED), 0);

            //---when the SMS has been sent---
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), R.string.message_sent_successfully,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), R.string.generic_failure,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), R.string.no_service,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), R.string.null_PDU,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), R.string.radio_off,
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            //---when the SMS has been delivered---
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), R.string.sms_delivered,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), R.string.sms_not_delivered,
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));
            System.out.println("Before the SMS Manager");
            SmsManager sms = SmsManager.getDefault();
            System.out.println("Before send message" + phoneNo1 + "details" + message1 + "SentPI" + sentPI + "Deliverd PI" + deliveredPI);
            sms.sendTextMessage(phoneNo1, null, message1, sentPI, deliveredPI);
            System.out.println("After send message" + phoneNo1 + "details" + message1 + "SentPI" + sentPI + "Deliverd PI" + deliveredPI);

        }catch (Exception e){
            dialog.dismiss();
            Toast.makeText(getBaseContext(), R.string.message_no_found,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
//        Intent back = new Intent(getApplicationContext(), SlideMainActivity.class);
//        back.putExtra("userid", User_id);
//        back.putExtra("fbuserproimg", fbuserproimg);
//        back.putExtra("whologin", WhoLogin);
//        back.putExtra("password", checkpassword);
//        System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
//        back.putExtra("accept", accept);
//        startActivity(back);
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
}
