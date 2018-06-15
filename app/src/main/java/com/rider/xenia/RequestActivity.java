package com.rider.xenia;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app_controller.AppController;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestActivity extends Activity {

    Button close;
    String User_id, location, WhoLogin, fbuserproimg, checkpassword;
    ProgressBar progressbar;
    String close1;
    String cancelreqdriverid;
    URL driver_Url;
    String driver_inputline, driverjson_status;
    JSONObject driver_jsonobj;
    JSONArray driver_jsonarray;
    String amount;
    Boolean cancelreq = true;

    String totalamount, arrive, drivername, driveremail, drivermobile, driverid, tripid, autotripid, tax, pickup, drop, date, service, distance, waiting;
    protected static final String TAG = "SlideMainActivity";


    private int progressStatus = 0;
    private Handler handler = new Handler();

    private volatile Thread blinker;

    public static final String PREFS_NAME = "MyPrefsFile";

    String Liveurl = "";
    String Liveurl1 = "";
    URL remove_Url;
    URL driverdetails_Url;


    private JSONArray driverdetails_jsonarray;
    private JSONObject driverdetails_jsonobj;
    private String driverdetailsjson_status, acceptstatus, acceptstatus1;
    private String driverdetails_inputline;


    String driverdetailsuserid;
    String driverdetailsname;
    String driverdetailslat;
    String driverdetailslong;
    String driverdetailstime;
    String acceptdriverfullname;


    private boolean whilecheck = true;
    private boolean invalidaccept = true;
    private boolean acceptyes = true;
    private boolean checkreceive = true;
    int checkcount = 0;

    Double Lastlat, Lastlng;
    String Lastlat1, Lastlng1;
    String acc;
    String acceptdriverid;

    String lat123, lot123;
    String category, destaddress;
    int i, n, l = 0, count = 0;

    Thread t;


    String arraydriverid[] = new String[20];
    boolean driverfirstround = true;
    int k;

    String regionsearchstatus, regionaddress, country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        getActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);
        Liveurl = sharedPreferences.getString("liveurl", null);
        country = sharedPreferences.getString("country", null);
        regionaddress = sharedPreferences.getString("regionaddress", null);
        regionsearchstatus = sharedPreferences.getString("regionsearchstatus", null);
        System.out.println("The Live URL Is " + Liveurl);
        System.out.println("The Country Is " + country);
        System.out.println("The region address Is " + regionaddress);
        System.out.println("The region status Is " + regionsearchstatus);


        Intent i = getIntent();
        User_id = i.getStringExtra("userid");
        fbuserproimg = i.getStringExtra("fbuserproimg");
        WhoLogin = i.getStringExtra("whologin");
        checkpassword = i.getStringExtra("password");
        location = i.getStringExtra("location");
        driverdetailsuserid = i.getStringExtra("Driverid");
        category = i.getStringExtra("category");
        destaddress = i.getStringExtra("destaddress");


        Lastlat1 = i.getStringExtra("Lat");
        Lastlng1 = i.getStringExtra("Long");
        System.out.println("LAT" + i.getStringExtra("Lat"));
        System.out.println("LNG" + i.getStringExtra("Long"));
        Lastlat = Double.valueOf(Lastlat1);
        Lastlng = Double.valueOf(Lastlng1);

        System.out.println("Double Last Lat " + Lastlat + "Last lng" + Lastlng);
        System.out.println("String Last Lat1 " + Lastlat1 + "Last lng1" + Lastlng1);

        lat123 = Lastlat1;
        lot123 = Lastlng1;

        progressbar = (ProgressBar) findViewById(R.id.reprogresstimer);

        close = (Button) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (net_connection_check()) {
                    close.setEnabled(false);
                    cancelrequest();

                    whilecheck = false;
                    progressStatus = 101;
                    onDestroy();

                    Intent slide = new Intent(getApplicationContext(), SlideMainActivity.class);
                    slide.putExtra("userid", User_id);
                    slide.putExtra("fbuserproimg", fbuserproimg);
                    System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
                    slide.putExtra("whologin", WhoLogin);
                    slide.putExtra("password", checkpassword);
                    System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
                    slide.putExtra("accept", acc);
                    slide.putExtra("cancel", "cancel");
                    slide.putExtra("drivername", driverdetailsname);
                    System.out.println("Accept Status in Request Close Activity" + acc);
                    startActivity(slide);
                    finish();
                }
            }
        });
        if (net_connection_check()) {
            timer();
            Driverdetails();
        }

        GCMRegistrar.checkDevice(this);

        // Make sure the manifest permissions was properly set
        GCMRegistrar.checkManifest(this);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                Config.DISPLAY_MESSAGE_ACTION));


    }

    BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
            System.out.println("New Message form Slide Main Activity" + newMessage);

            if (newMessage != null || !newMessage.equals("null")) {


                if (newMessage == "driver cancel" || newMessage.equals("driver cancel"))  /// Driver cancel request it show number of reason
                {
                    if (net_connection_check())
                        get_driver_cancel_reson();
                } else if (newMessage == "tripend" || newMessage.equals("tripend")) {

                    System.out.println("ELSE before call getamount " + newMessage);
                    if (net_connection_check())
                        getamount();
                    System.out.println("ELSE After call the arrive now " + newMessage);
                } else {
                    if (net_connection_check()) {
                        checkreceive = false;
                        nextdriver(newMessage);
                    }
                }
            }

            System.out.println("i================" + i);
            System.out.println("C================" + count);
            System.out.println("n================" + n);
            System.out.println("l================" + l);

        }
    };

    // ************************* Funcations ***************************
    public void timer() {

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus <= 100) {
                    progressStatus += 1;


                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressbar.setProgress(progressStatus);
                            if (progressStatus == 100) {
                                progressStatus = 1;
                                checkcount = checkcount + 1;
                                System.out.println("Check Count from progress bar" + checkcount);
                                System.out.println("check boolean" + checkreceive);

                                if (checkcount > 3 && checkreceive) {
                                    String newMessage = "no";  // send push notifiacation
                                    nextdriver(newMessage);   /// Call next Driver
                                }


                            }
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        //Just to display the progress slowly
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }


// **************************  Google Map  Function End **********************


    public void nextdriver(String newMessage) {
        System.out.println("i================" + i);
        System.out.println("C================" + count);
        System.out.println("n================" + n);
        System.out.println("l================" + l);
        System.out.println("Accept status Insite Run Method" + newMessage);


        if (newMessage.equals("no")) {
            System.out.println("Accept equal NO is" + newMessage);
            count++;
            if (count == n) {
                progressStatus = 101;
                System.out.println("Accept I==N equal NO is" + newMessage);
                whilecheck = false;
                if (invalidaccept == true) {
                    driverdetailsuserid = null;
                    Userdetails();
                    Drivernotavilable();
                    whilecheck = false;
                    checkreceive = false;

                    System.out.println("Insite the Autorun");


                }
                System.out.println("IF Thread Before Stop");

                System.out.println("IF Thread After Stop");
            } else {
                System.out.println("Accept ELSE equal No is" + newMessage);
                whilecheck = true;
                System.out.println("Accept status Insite Run Method" + newMessage);

                System.out.println("Thread Before Stop");
                l++;


                checkcount = 0;
                Driverdetails();

                System.out.println("Thread After Stop");

            }
        } else if (newMessage.equals("yes")) {

            progressStatus = 101;
            System.out.println("Accept equal YES is" + newMessage);
            whilecheck = false;
            if (acceptyes == true) {
                acc = Accept();
                acceptyes = false;
                checkreceive = false;

                acceptalart();

            }
            System.out.println("Thread Before Stop");
            System.out.println("Thread After Stop");
        } else {
            whilecheck = true;
            System.out.println("Accept status Insite Run Method Else" + newMessage);
        }
    }


    public void Driverdetails() {

        try {

            // driverdetails_Url=new URL(Liveurl+"get_driving_information?userid="+User_id+"&lat="+Lastlat+"&long="+Lastlng+"&category="+category);

            if (regionsearchstatus == null || regionsearchstatus.equals("OFF")) {
                driverdetails_Url = new URL(Liveurl + "get_driving_information?userid=" + User_id + "&lat=" + Lastlat + "&long=" + Lastlng + "&category=" + category + "&country=" + country);
                System.out.println("URL isURL is" + driverdetails_Url);
            } else {
                driverdetails_Url = new URL(Liveurl + "get_driving_information?userid=" + User_id + "&lat=" + Lastlat + "&long=" + Lastlng + "&category=" + category + "&country=" + country + "&region=" + regionaddress);
            }

            Log.w("Rider request Url", driverdetails_Url.toString());
            BufferedReader login_reader;
            String str_login = "";

            login_reader = new BufferedReader(new InputStreamReader(driverdetails_Url.openStream()));

            String s = driverdetails_Url.toString();
            while ((driverdetails_inputline = login_reader.readLine()) != null) {

                str_login += driverdetails_inputline;
                Log.i("Rider request JSON", str_login);

            }

            driverdetails_jsonarray = new JSONArray(str_login);
            if (driverfirstround == true) {
                for (k = 0; k < driverdetails_jsonarray.length(); k++) {

                    n = driverdetails_jsonarray.length();
                    driverdetails_jsonobj = driverdetails_jsonarray.getJSONObject(k);
                    driverdetailsjson_status = driverdetails_jsonobj.getString("status");

                    if (driverdetailsjson_status.equals("Success")) {
                        driverdetailsuserid = driverdetails_jsonobj.getString("id");
                        arraydriverid[k] = driverdetailsuserid;
                        System.out.println(k + "Driver ID" + arraydriverid[k]);

                    }
                }
                driverfirstround = false;

            }


            for (i = 0; i < driverdetails_jsonarray.length(); i++) {

                System.out.println("Request Count is _________:" + count + "L is ____________" + l + "K is __________________" + k);
                driverdetails_jsonobj = driverdetails_jsonarray.getJSONObject(i);
                driverdetailsjson_status = driverdetails_jsonobj.getString("status");

                if (driverdetailsjson_status.equals("Success")) {
                    driverdetailsuserid = driverdetails_jsonobj.getString("id");
                    driverdetailsname = driverdetails_jsonobj.getString("drivername");
                    driverdetailslat = driverdetails_jsonobj.getString("lat");
                    driverdetailslong = driverdetails_jsonobj.getString("long");
                    driverdetailstime = driverdetails_jsonobj.getString("trip_time");


                    if (l < n) {

                        if (arraydriverid[l].equals(driverdetailsuserid)) {

                            System.out.println("Driver id" + driverdetailsuserid);
                            System.out.println("Array id" + arraydriverid[l]);


                            System.out.println("Request Driver Name" + driverdetailsname);
                            cancelreqdriverid = driverdetailsuserid;
                            Userdetails();

                        } else {

                        }

                    }
                } else {

                }
            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


/// ******************************* User Details Send to server ************************

    public void Userdetails() {


        try {

            checkcount = 0;
            checkreceive = true;
            System.out.println("Check Count from user details" + checkcount);
            System.out.println("check boolean user details" + checkreceive);

            System.out.println("inside Try");


            String message = "Request";
            System.out.println("destaddress" + destaddress);

            if (destaddress != null || !destaddress.equals(null)) {
                destaddress = destaddress.replaceAll(" ", "%20");
                System.out.println("Dest ADDRESS" + destaddress);
            }
            String trip_driver_status = "Request";
            driverdetails_Url = new URL(Liveurl + "update_location?userid=" + User_id + "&lat=" + Lastlat + "&long=" + Lastlng + "&driverid=" + driverdetailsuserid + "&pickuplat=" + Lastlat + "&pickuplong=" + Lastlng + "&destaddress=" + destaddress + "&message=" + message + "&trip_driver_status=" + trip_driver_status);
            System.out.println("Rider User Details Update URL" + driverdetails_Url);

            BufferedReader login_reader;
            String str_login = "";

            login_reader = new BufferedReader(new InputStreamReader(driverdetails_Url.openStream()));

            System.out.println("Rider User Details Update Reader" + login_reader);
            String s = driverdetails_Url.toString();
            System.out.println("Rider User Details Update String" + s);
            while ((driverdetails_inputline = login_reader.readLine()) != null) {

                str_login += driverdetails_inputline;
                System.out.println("Rider User Details Update Input Line" + str_login);


            }

            System.out.print("Rider User Details Update Outsite while" + str_login);
            driverdetails_jsonarray = new JSONArray(str_login);


            for (int i = 0; i < driverdetails_jsonarray.length(); i++) {

                driverdetails_jsonobj = driverdetails_jsonarray.getJSONObject(i);
                driverdetailsjson_status = driverdetails_jsonobj.getString("status");

            }


            System.out.println("Check accept request " + driverdetailsjson_status);
            if (driverdetailsjson_status.matches("Success")) {


            } else if (driverdetailsjson_status.matches("Busy")) {


                String newMessage = "no";
                if (newMessage != null || !newMessage.equals("null")) {


                    if (net_connection_check()) {
                        checkreceive = false;
                        nextdriver(newMessage);
                    }
                }
            } else {


            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public String Accept() {


        try {


            driverdetails_Url = new URL(Liveurl + "get_acceptstatus?userid=" + User_id);
            System.out.println("Rider get Accept Status URL" + driverdetails_Url);
            BufferedReader login_reader;
            String str_login = "";

            login_reader = new BufferedReader(new InputStreamReader(driverdetails_Url.openStream()));

            System.out.println("Rider get Accept Status Reader" + login_reader);
            String s = driverdetails_Url.toString();
            System.out.println("Rider get Accept Status String" + s);
            while ((driverdetails_inputline = login_reader.readLine()) != null) {

                str_login += driverdetails_inputline;
                System.out.println("Rider get Accept Status Input Line" + str_login);


            }

            System.out.print("Rider get Accept Status Outsite While" + str_login);

            driverdetails_jsonarray = new JSONArray(str_login);


            for (int i = 0; i < driverdetails_jsonarray.length(); i++) {


                driverdetails_jsonobj = driverdetails_jsonarray.getJSONObject(i);
                acceptstatus = driverdetails_jsonobj.getString("accept");
                acceptdriverid = driverdetails_jsonobj.getString("driverid");
                String acceptdriverfname = driverdetails_jsonobj.getString("firstname");
                String acceptdriverlname = driverdetails_jsonobj.getString("lastname");
                acceptdriverfullname = acceptdriverfname + "" + acceptdriverlname;


            }


            if (acceptstatus.matches("yes") && acceptdriverid != null) {

                System.out.println("Accepted");

            } else {

                System.out.println("Not Accepted " + acceptstatus);
            }


        } catch (MalformedURLException e) {
// TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Accept status In Accept Method" + acceptstatus);
        return acceptstatus;
    }


    public void get_driver_cancel_reson() {
        try {


            System.out.println("inside Try");


            driverdetails_Url = new URL(Liveurl + "GetDriverCancel?userid=" + User_id);
            System.out.println("Get single driver location" + driverdetails_Url);

            BufferedReader login_reader;
            String str_login = "";

            login_reader = new BufferedReader(new InputStreamReader(driverdetails_Url.openStream()));

            System.out.println("Get single driver location" + login_reader);

            String s = driverdetails_Url.toString();

            while ((driverdetails_inputline = login_reader.readLine()) != null) {

                str_login += driverdetails_inputline;


            }


            driverdetails_jsonarray = new JSONArray(str_login);


            for (int i = 0; i < driverdetails_jsonarray.length(); i++) {


                driverdetails_jsonobj = driverdetails_jsonarray.getJSONObject(i);
                driverdetailsjson_status = driverdetails_jsonobj.getString("status");

                if (driverdetailsjson_status.equals("Success")) {
                    String cancelreson;
                    cancelreson = driverdetails_jsonobj.getString("Cancel");


                    String title = "Trip cancelled";
                    String message = "The trip cancelled by Driver due to " + cancelreson;
                    System.out.println("Remoe details");
                    System.out.println("Before remove user details");
                    System.out.println("Before foofle mao");


                    System.out.println("Before remove user details");


                    dialogshow(title, message);


                } else if (driverdetailsjson_status.equals("fail")) {

                } else {
                    System.out.println("Status check is in else");
                }

            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void dialogshow(String title, String msg) {
        System.out.println("Insite the Drive cancel after accept requst dialog alert show");
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                whilecheck = false;
                invalidaccept = false;
                acc = "no";
                System.out.println("Invalid Accept" + acc);
                onDestroy();

                Intent slide = new Intent(getApplicationContext(), SlideMainActivity.class);
                slide.putExtra("userid", User_id);
                slide.putExtra("fbuserproimg", fbuserproimg);
                System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
                slide.putExtra("whologin", WhoLogin);
                slide.putExtra("password", checkpassword);
                System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
                slide.putExtra("accept", acc);
                System.out.println("Accept Status in Request autorun" + acc);
                startActivity(slide);

                finish();

            }
            //  alertdialog2.cancel();

        });


        builder.show();

    }


    public void acceptalart() {
        System.out.println("Insite the accept alart");
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle(R.string.your_request_confirmed);

        builder.setMessage(getString(R.string.arcane_driver) + acceptdriverfullname + getString(R.string.arrives_within) + driverdetailstime);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {

                System.out.println("Accept status Insite Run Method If" + acc);

                close1 = "false";
                onDestroy();
                Intent slide = new Intent(getApplicationContext(), SlideMainActivity.class);
                slide.putExtra("userid", User_id);
                slide.putExtra("close", close1);
                slide.putExtra("fbuserproimg", fbuserproimg);
                System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
                slide.putExtra("whologin", WhoLogin);
                slide.putExtra("password", checkpassword);
                System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
                acc = "yes";
                slide.putExtra("accept", acc);
                slide.putExtra("lat", lat123);
                slide.putExtra("lot", lot123);
                slide.putExtra("destaddress", destaddress);
                slide.putExtra("acceptdriverid", acceptdriverid);
                System.out.println("Accept Status in Request autorun" + acc);
                startActivity(slide);
                finish();

            }
        });
        try {
            builder.show();
        } catch (Exception e) {
            // WindowManager$BadTokenException will be caught and the app would not display
            // the 'Force Close' message
        }

    }


    public void Drivernotavilable() {

        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(R.string.all_drivers_currently_busy);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {


                whilecheck = false;
                invalidaccept = false;
                onDestroy();
                System.out.println("Invalid Accept" + acc);
                //Toast.makeText(getApplicationContext(), "Invalid Accept"+acc,Toast.LENGTH_SHORT).show();
                Intent slide = new Intent(getApplicationContext(), SlideMainActivity.class);
                slide.putExtra("userid", User_id);
                slide.putExtra("fbuserproimg", fbuserproimg);
                System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
                slide.putExtra("whologin", WhoLogin);
                slide.putExtra("password", checkpassword);
                System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
                slide.putExtra("accept", acc);
                System.out.println("Accept Status in Request autorun" + acc);
                startActivity(slide);

                finish();
            }
        });

        try {
            builder.show();
        } catch (Exception e) {
            // WindowManager$BadTokenException will be caught and the app would not display
            // the 'Force Close' message
        }

    }


    public void onBackPressed() {

    }


    public void cancelrequest() {
        try {


            driver_Url = new URL(Liveurl + "cancelreq?driverid=" + cancelreqdriverid + "&userid=" + User_id);
            System.out.println("Driver Accept URL" + driver_Url);

            BufferedReader login_reader;
            String str_login = "";


            login_reader = new BufferedReader(new InputStreamReader(driver_Url.openStream()));

            System.out.println("Driver Accept Reader" + login_reader);

            String s = driver_Url.toString();
            System.out.println("Driver Accept String" + s);
            while ((driver_inputline = login_reader.readLine()) != null) {

                str_login += driver_inputline;
                System.out.println("Driver Accept Input Line" + str_login);


            }

            System.out.print("Driver Accept Outsite While" + str_login);

            driver_jsonarray = new JSONArray(str_login);


            for (int i = 0; i < driver_jsonarray.length(); i++) {


                driver_jsonobj = driver_jsonarray.getJSONObject(i);
                driverjson_status = driver_jsonobj.getString("status");


            }


            if (driverjson_status.matches("Success")) {
                System.out.println("Rquest Cancelled");


            } else {

                System.out.println("Invalid Data");
            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        try {
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);

            //Clear internal resources.
            GCMRegistrar.onDestroy(this);

        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    public String getamount() {

        final String url = Liveurl + "get_amount?userid=" + User_id;
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
                                totalamount = driverdetails_jsonobj.getString("amount");
                                arrive = driverdetails_jsonobj.getString("arrive");
                                System.out.println("The Total amount is" + totalamount);


                                if (!totalamount.equals("null") && (arrive.equals("null") || arrive.equals("no"))) {
                                    drivername = driverdetails_jsonobj.getString("driver_name");
                                    driveremail = driverdetails_jsonobj.getString("driver_email");
                                    drivermobile = driverdetails_jsonobj.getString("driver_mobileno");
                                    driverid = driverdetails_jsonobj.getString("driverid");
                                    System.out.println("Driver Id" + driverid);
                                    tripid = driverdetails_jsonobj.getString("tripid");
                                    System.out.println("Trip Id" + tripid);
                                    autotripid = driverdetails_jsonobj.getString("autotripid");
                                    System.out.println("Auto Trip Id" + autotripid);
                                    tax = driverdetails_jsonobj.getString("tax");
                                    pickup = driverdetails_jsonobj.getString("pickup");
                                    drop = driverdetails_jsonobj.getString("drop");
                                    date = driverdetails_jsonobj.getString("timestamp");
                                    service = driverdetails_jsonobj.getString("service");
                                    distance = driverdetails_jsonobj.getString("distance");
                                    waiting = driverdetails_jsonobj.getString("waitingtime");

                                    System.out.println("Waiting in Rider Slide main activity" + waiting);

                                    System.out.println("Before call fareActivity" + close);
                                    Intent fare = new Intent(getApplicationContext(), FareActivity.class);
                                    fare.putExtra("userid", User_id);
                                    fare.putExtra("tripid", tripid);
                                    fare.putExtra("autotripid", autotripid);
                                    fare.putExtra("driverid", driverid);
                                    fare.putExtra("fbuserproimg", fbuserproimg);
                                    fare.putExtra("whologin", WhoLogin);
                                    fare.putExtra("password", checkpassword);
                                    fare.putExtra("amount", totalamount);
                                    fare.putExtra("drivername", drivername);
                                    fare.putExtra("driveremail", driveremail);
                                    fare.putExtra("drivermobile", drivermobile);
                                    fare.putExtra("tax", tax);
                                    fare.putExtra("pickup", pickup);
                                    fare.putExtra("drop", drop);
                                    fare.putExtra("date", date);
                                    fare.putExtra("service", service);
                                    fare.putExtra("distance", distance);
                                    fare.putExtra("wait", waiting);

                                    System.out.println("FP Auto Trip Id" + autotripid);
                                    System.out.println("FP Driver Id" + driverid);


                                    startActivity(fare);
                                    finish();

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
                    System.out.println("No internet connection");
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.there_is_no_connection, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 70);
                    toast.show();
                }
                VolleyLog.d(TAG, getString(R.string.error) + error.getMessage());
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

        System.out.println("Amount is Before Return" + driverdetailsjson_status);
        return totalamount;
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