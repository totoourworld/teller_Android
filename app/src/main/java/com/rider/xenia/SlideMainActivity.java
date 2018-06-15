package com.rider.xenia;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.app_controller.AppController;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.utils.Catagories;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;


public class SlideMainActivity extends FragmentActivity implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener, OnMapReadyCallback {
// GooglePlayServicesClient.ConnectionCallbacks,
// GooglePlayServicesClient.OnConnectionFailedListener,


    //Advertisement Layout

    RelativeLayout ad;
    ImageView ad_img, imgbut;
    TextView ad_title, ad_tagline, fname;
    ImageView ad_close;
    String show_ad, city, pincode;
    String status, get_image, get_title, get_tagline, get_url;
    URL adimg, Url;
    Bitmap bitmap;
    private JSONObject login_jsonobj1, advertisement_jsonobj;


    // ******************* Sliding Menu Variable Start ********************	
    protected static final String TAG = "SlideMainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    String User_id, location;
    String WhoLogin;
    private String fbuserproimg;
    Fragment fragment = null;

    LatLng mark, mark1, old;
    Double lat1;
    Double log1;

    Double arrivelat, arrivelng, arrivelat1, arrivelng1;
    LatLng arriveA, arriveB = null;
    float rotation_angle, rotation_angle1;
    String checkpassword;

    String trip_rider_status;
    private JSONObject profile_jsonobj, remove_jsonobj;
    private String profile_status, remove_status;
    private boolean is_first;
    boolean usercheck;
    private boolean whilecheck = true;
    private boolean first = true;
    private boolean close = true;
    private boolean runsingledriver = true;
    String close1;
    private boolean Arrive = true;
    boolean secondrider = false;
    private boolean estimateclick = false;
    public String amount = "siva";
    // *************** Sliding Menu Variable End *************************
    TextView minutes;
    // **************************  Google Map Variable Start **********************
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    GoogleMap mGoogleMap;
    LinearLayout search;
    LinearLayout search1;
    LinearLayout requestlin;
    LinearLayout action;

    Button slideback, request;
    ImageView add, gpsmove;
    String category;
    Marker marker, markerorigin;
    String newMessage1 = "null";
    int n, l;
    Boolean begintripauto = true;
    //  Stores the current  instantiation of the location client in this object
    GoogleApiClient googleApiClient = null;
    Location gpslocation;
    boolean mUpdatesRequested = false;
    private TextView markerText;
    private LatLng center;
    private LatLng oldlatlng[] = new LatLng[25];
    private RelativeLayout markerLayout;
    private Geocoder geocoder;
    private List<Address> addresses;
    private TextView Address, Address1;

    String country;


    //Toll Fee
    JSONObject tollfee_jsonobj;
    String tollfee_status, tollfeeamt;


    Controller controller;


    String regionsearchstatus;
    String regionaddress;

    LatLng pickupadd;
    LatLng desination;

    Double droplatd, droplongd;
    String droplat, droplong;

    ImageView load;
// ************************** Web Service ************************************

    String Liveurl1 = "";
    String Liveurl = "";
    String Liveurl2 = "";
    String googleapikey;
    String googleprojectid, paypalclienttoken, paypaltype;

    private JSONArray driverdetails_jsonarray;
    private JSONObject driverdetails_jsonobj;
    private String driverdetailsjson_status;
    private String acceptstatus;
    private String driverdetails_inputline;
    String driverdetailsname;
    String tripid, driverid, autotripid;

    public String driverdetailsuserid;
    String driverdetailslong;
    String driverdetailslat;
    String driverdetailstime;
    public String accept = null, accept1;
    ImageView profileimg;

    URL proimg;
    Bitmap bitmap1;
    String profile_userfname, profile_userlname, profile_paypal_useremail, profile_useremail, profile_usermobile,
            profile_userprofilepic, profile_userpassword;
    private JSONObject profile_jsonobj3;

    String totalamount, drivername, driveremail, drivermobile, tax, pickup, drop, date, service, distance, waiting;
    public String arrive = "null";


    URL driverdetails_Url;

    Thread t;
    private boolean isExit = false;

    double lat3 = 10.00, lat4 = 10.00;

    LatLng origin, dest, origin1;

    String originlat, originlot;
    String acceptdriverid;

    Animation animRotate;

    FareActivity fareactivity = new FareActivity();

    LatLng gpslatlng;
    String gpscountry;
    String logoimage, logoname;

    Bitmap logobitmap;

    int markerimagecount = 1;

/// ************************** Bottom slider variables **********************

    SeekBar mybar;
    boolean check;
    int progresscount = 0;

    LinearLayout carfaredetails, minfarelin;
    TextView eta, size, minfare, textfare;

    String etas, sizes, minfares, price_km, price_min, catname;
    SlideUpPanelLayout layout;
    TextView content;

    TextView getfareestimate;
    String desinationbuttonaction, pickupaddress, destaddress = null;
    String rideshares;
    LatLng destination;

    boolean drawpolylline;

    float address2distance, address2mins;

    AlertDialog cancelackalertdialog;

    String cancel;


    LinearLayout destinationeta;
    TextView distanceeta;


    String FixedAmount, IsFixed, PresentageAmount, primetime_precentage;
    boolean checkfixedamount;

    float min_faref, price_minutef, price_kmf, max_sizef;
    int minfareamount;
    int isfixed;

    int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 1;
    TextView categoryitem1, categoryitem2, categoryitem3;

    // Display driver details

    String driverfname, driverlname, driverfullname, prof_pic, email, mobile, star, carname;

    TextView drivermname, drivercarname, driverrate;
    ImageView driverprofile, rideshare;
    LinearLayout driverdetails;

    String rideshareid, ridesharename, rideshareprofile, ridesharedriverid;

    String selectedname, selectedphone, acceptednumber;
    Bitmap selectedprofile;


    RelativeLayout navrl;

    String[] carcategory;

    String nETA;
// **************************  Google Map  Variable End **********************	

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters

    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    Double Lastlat = 11.00, Lastlng = 11.00;


    boolean checkamount = true;
    protected LocationManager locationManager;
    String shareacceptstatus;
    public static final String PREFS_NAME = "MyPrefsFile";


    /// Spliting variables

    LinearLayout spliting;

    TextView accspliting, notspliting;
    ImageView accsplitingpro, notsplitingpro;


    String admin_number;


    Button powered;


    String acceptdriverfullname;
    Boolean categorynetcheck = false;
    private ParseJson parseJson;


    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_activity_main);
        getActionBar().hide();
        System.out.println("INSITE SLIDE MAIN ACTIVITY");

        System.out.println("1Accept st" + accept);

        controller = (Controller) getApplicationContext();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        parseJson = new ParseJson(SlideMainActivity.this);

        int currentapiVersion = Build.VERSION.SDK_INT;
        System.out.println("Current API Version is " + currentapiVersion);
        System.out.println("Check API version is" + Build.VERSION_CODES.M);
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            // Do something for lollipop and above versions
            int hasLocationPermission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            int hasSMSPermission = checkSelfPermission(android.Manifest.permission.SEND_SMS);
            List<String> permissions = new ArrayList<String>();
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.SEND_SMS);
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);

            }
        }


        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);
        Liveurl = sharedPreferences.getString("liveurl", null);
        Liveurl2 = sharedPreferences.getString("liveurl2", null);
        googleapikey = sharedPreferences.getString("googleapikey", null);


        regionsearchstatus = sharedPreferences.getString("regionsearchstatus", null);

        if (regionsearchstatus == null) {
            System.out.println("regionsearchstatus Driver details funcation" + regionsearchstatus);
            regionsearchstatus = "OFF";
        }

        System.out.println("The Live URL Is " + Liveurl1);

        minutes = (TextView) findViewById(R.id.minutes);


        // ********************************* Sliding Menu Code Start *********************	
        mTitle = mDrawerTitle = getTitle();

        Intent i = getIntent();
        User_id = i.getStringExtra("userid");
        fbuserproimg = i.getStringExtra("fbuserproimg");
        WhoLogin = i.getStringExtra("whologin");
        System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
        System.out.println("Slide Main Activity FB Imag" + fbuserproimg);
        checkpassword = i.getStringExtra("password");
        location = i.getStringExtra("location");
        desinationbuttonaction = i.getStringExtra("desinationbutton");
        pickupaddress = i.getStringExtra("address");
        accept = i.getStringExtra("accept");
        System.out.println("1Accept get" + accept);
        originlat = i.getStringExtra("lat");
        originlot = i.getStringExtra("lot");
        acceptdriverid = i.getStringExtra("acceptdriverid");
        gpscountry = i.getStringExtra("gpscountry");
        cancel = i.getStringExtra("cancel");
        destaddress = i.getStringExtra("destaddress");
        rideshares = i.getStringExtra("rideshare");
        selectedname = i.getStringExtra("splitname");
        selectedphone = i.getStringExtra("splitphone");
        selectedprofile = (Bitmap) i.getParcelableExtra("splitprofile");
        show_ad = i.getStringExtra("ad");
        System.out.println("get ad value" + show_ad);


        android.support.v7.app.AlertDialog.Builder cancelackalert =
                new android.support.v7.app.AlertDialog.Builder(SlideMainActivity.this, R.style.AppCompatAlertDialogStyle);


        System.out.println("Slide main activity Password" + checkpassword);
        System.out.println("LOCATION SLIDE MAIN ACTIVITY" + location);
        System.out.println("DESINATION " + desinationbuttonaction);
        System.out.println("PICKUPADDRESS" + pickupaddress);

        System.out.println("DESTADDRESS" + destaddress);


        categoryitem1 = (TextView) findViewById(R.id.categoryitem1);
        categoryitem2 = (TextView) findViewById(R.id.categoryitem2);
        categoryitem3 = (TextView) findViewById(R.id.categoryitem3);

        if (net_connection_check()) {
            getcategory();
        }

        markerText = (TextView) findViewById(R.id.locationMarkertext);
        Address = (TextView) findViewById(R.id.adressText);
        Address1 = (TextView) findViewById(R.id.adressText1);
        markerLayout = (RelativeLayout) findViewById(R.id.locationMarker);
        ad = (RelativeLayout) findViewById(R.id.ad_layout);
        //Advertisement Declarations
        ad.setVisibility(View.GONE);
        ad_img = (ImageView) findViewById(R.id.ad_img);
        ad_title = (TextView) findViewById(R.id.adTitle);
        ad_tagline = (TextView) findViewById(R.id.tag_line);
        ad_close = (ImageView) findViewById(R.id.close);

        eta = (TextView) findViewById(R.id.eta);
        size = (TextView) findViewById(R.id.maxsize);
        minfare = (TextView) findViewById(R.id.minfare);
        textfare = (TextView) findViewById(R.id.textView3);


        mybar = (SeekBar) findViewById(R.id.seekBar1);

        carfaredetails = (LinearLayout) findViewById(R.id.carfaredetails);
        minfarelin = (LinearLayout) findViewById(R.id.minfarelin);
        getfareestimate = (TextView) findViewById(R.id.getfareestimate);

        destinationeta = (LinearLayout) findViewById(R.id.destinationeta);
        distanceeta = (TextView) findViewById(R.id.distanceeta);


        load = (ImageView) findViewById(R.id.load);


        // Spliting 
        spliting = (LinearLayout) findViewById(R.id.spliting);
        spliting.setVisibility(View.GONE);

        accspliting = (TextView) findViewById(R.id.acceptedname);
        accsplitingpro = (ImageView) findViewById(R.id.accptedproimg);
        notspliting = (TextView) findViewById(R.id.notsplitedname);
        notsplitingpro = (ImageView) findViewById(R.id.notsplitedproimg);


        drivermname = (TextView) findViewById(R.id.drivername);
        drivercarname = (TextView) findViewById(R.id.drivercar);
        driverrate = (TextView) findViewById(R.id.driverrete);
        driverprofile = (ImageView) findViewById(R.id.driverprofile);
        rideshare = (ImageView) findViewById(R.id.rideshare);
        driverdetails = (LinearLayout) findViewById(R.id.driverdetails);
        profileimg = (ImageView) findViewById(R.id.iii);
        fname = (TextView) findViewById(R.id.ridername);
        ad.setVisibility(View.GONE);

        ImageView imgbut = (ImageView) findViewById(R.id.drawicon);
        layout = (SlideUpPanelLayout) findViewById(R.id.slide_layout);


        riderprofiledetails(); // Get Rider details


        estimateclick = true;
        layout.showHandle();
        content = (TextView) findViewById(R.id.content);
        content.setVisibility(View.INVISIBLE);
        content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout.getPanelState() == SlideUpPanelLayout.PanelState.HIDDEN) {
                    layout.showHandle();
                    getActionBar().hide();
                } else {
                    layout.hideHandle();
                    getActionBar().hide();
                }
            }
        });

        powered = (Button) findViewById(R.id.powered);

        powered.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (net_connection_check()) {

                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.cogzidel.com/mobile-application/"));
                    startActivity(i);
                }

            }
        });


        animRotate = AnimationUtils.loadAnimation(this, R.anim.rotation);
        load.startAnimation(animRotate);

        navMenuTitles = getResources().getStringArray(R.array.profile_list);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView imageV = (ImageView) findViewById(R.id.iii);
        navrl = (RelativeLayout) findViewById(R.id.rv);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav
        // drawer items to array
        // Home


        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
    /*    navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
*/

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons


            }
        };

        imgbut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDrawerLayout.openDrawer(navrl);
                mDrawerList.setVisibility(View.VISIBLE);
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        if (savedInstanceState == null) {
            // on first time display view for first nav item

            getActionBar().setTitle("ARCANE");
        }


        if (rideshares == null || rideshares.equals("null")) {

        } else if (rideshares == "cancel" || rideshares.equals("cancel")) {


            System.out.println("SLIDE BACK BUTTON CLICK LAYOUT VISIBLE");
            layout.setVisibility(View.GONE);
            spliting.setVisibility(View.GONE);


            driverdetails.setVisibility(View.VISIBLE);
            destinationeta.setVisibility(View.VISIBLE);

        } else if (rideshares == "share" || rideshares.equals("share")) {
            layout.setVisibility(View.GONE);

            driverdetails.setVisibility(View.GONE);

        }


        if (cancel == null || cancel.equals("null")) {

        } else if (cancel == "cancel" || cancel.equals("cancel")) {

            cancelackalert.setMessage(R.string.waiting_cancel);
            cancelackalert.setCancelable(false);
            cancelackalert.setInverseBackgroundForced(false);
            cancelackalert.setPositiveButton("Close", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancelackalertdialog.cancel();
                }
            });
            close = false;
            cancelackalertdialog = cancelackalert.create();
            cancelackalertdialog.show();

        }
        System.out.println("1Accept ch" + accept);
        if (accept == null || accept.equals("null")) {

            if (net_connection_check()) {
                close = true;
                System.out.println("Category" + category);
                try {
                    if (category == null || category.equals("null")) {
                        category = carcategory[0];
                    }
                    System.out.println("Driver Details Accept Null" + category);
                    Driverdetails();
                } catch (Exception e) {

                }


                //  minfare();
            }
            System.out.println("Not Accepted Your Request Try Later" + close + "Accept is" + accept);

        } else if (accept.equals("yes")) {

            //  imgbut.setVisibility(View.INVISIBLE);
            close = false;
            markerText.setVisibility(View.GONE);

            System.out.println("ACCEPT YES LAYOUT GONE ");
            layout.setVisibility(View.INVISIBLE);
            load.setVisibility(View.GONE);
            load.setAnimation(null);

            if (net_connection_check()) {
                driverdisplaydetails();
                autogetsingledriver();

            }

            System.out.println("Accepted Your Request Arrive Soon Close" + close + "Accept is" + accept);


            System.out.println("After executer downladTask");

            accept1 = accept;

        } else if (accept.equals("no") && begintripauto) {
            System.out.println("Accepted data is null" + close + "Accept is" + accept);
            close = false;


            if (net_connection_check()) {
                if (category == null || category.equals("null")) {
                    category = carcategory[0];
                }
                System.out.println("Driver Details Accept No And Begintrip" + category);
                Driverdetails();

                //  minfare();
            }
        }

        System.out.println("5ACCEPT IS" + accept);
        System.out.println("first LOCATION form Search activity" + location);
        System.out.println("first GEt Desination button action is " + desinationbuttonaction);
        System.out.println("first PICKUPADDRESS" + pickupaddress);

        System.out.println("Acceptd Driver ID" + acceptdriverid);
        System.out.println("Selected NAme" + selectedname);
        System.out.println("Selected NAme" + selectedphone);
        System.out.println("Selected NAme" + selectedprofile);


        if (selectedphone == null || selectedphone == null || selectedphone.equals("null")) {

        } else {
            spliting.setVisibility(View.VISIBLE);

            accspliting.setText("Waiting for invite acceptance...");
            accsplitingpro.setVisibility(View.INVISIBLE);

            notspliting.setText(selectedname);
            if (selectedprofile != null) {
                notsplitingpro.setImageBitmap(selectedprofile);
            }

        }
        if (location == null || location == "null" || location.equals("null")) {
            System.out.println("Location" + location);
            location = "siva1";
            textfare.setText("MIN FARE");
        }


        if (location.equals("siva1") || location == "siva1" && desinationbuttonaction == null) {
            System.out.println("Location" + location);
            location = "siva";
            estimateclick = false;
            textfare.setText("MIN FARE");
        } else if (!location.equals("siva1") || location != "siva1") {

            if (desinationbuttonaction == "fareestimate" || desinationbuttonaction.equals("fareestimate")) {

                if (net_connection_check()) {
/*
                    System.out.println("Location form Search activity" + location);
                    System.out.println("Get Desination button action is " + desinationbuttonaction);


                    pickupadd = getLocationFromAddress(pickupaddress);
                    desination = getLocationFromAddress(location);

                    System.out.println("PICKUP LOCATION" + pickupadd + "DESINATION LOCATION" + desination);

                    drawpolylline = false;
                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(pickupadd, desination);
                    System.out.println("After call direction url" + url);


                    DownloadTask downloadTask = new DownloadTask();

                    System.out.println("After call download task");

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);*/

                    if (estimateclick == true) {
                        //estimatefarecalculation();
                        fare_estimation();
                    }
                }
            } else {
                estimateclick = false;
                textfare.setText("MIN FARE");
            }

        } else {
            location = "siva";
        }

        System.out.println("DEST Addres " + destaddress);
        if (destaddress != null && !destaddress.isEmpty() && !destaddress.equals("null")) {
            if (net_connection_check()) {
                destination = getLocationFromAddress(destaddress);


                droplatd = destination.latitude;
                droplongd = destination.longitude;

                droplat = Double.valueOf(droplatd).toString();
                droplong = Double.valueOf(droplongd).toString();

                droplocation();
            }

        }

        // ************************* Sliding Menu Code Stop *****************************	


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {

            FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

            locationManager.requestLocationUpdates(

                    LocationManager.GPS_PROVIDER,

                    MINIMUM_TIME_BETWEEN_UPDATES,

                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,

                    new MyLocationListener()


            );


        }


        requestlin = (LinearLayout) findViewById(R.id.requestlin);
        requestlin.setVisibility(View.GONE);

        request = (Button) findViewById(R.id.request);
        request.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // tm tmrw abi ku birth dayTODO Auto-generated method stub

                if (net_connection_check()) {


                    if (!drawpolylline && estimateclick) {

                        close = false;
                        estimateclick = false;
                        System.out.println("Close is False when click request qctivity" + close);
                        String lat = new Double(Lastlat).toString();
                        String lng = new Double(Lastlng).toString();
                        //close=false;
                        Intent request = new Intent(getApplicationContext(), RequestActivity.class);
                        request.putExtra("userid", User_id);
                        request.putExtra("fbuserproimg", fbuserproimg);
                        System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
                        request.putExtra("whologin", WhoLogin);
                        request.putExtra("password", checkpassword);
                        request.putExtra("Driverid", driverdetailsuserid);
                        System.out.println("Request Function Driver id" + driverdetailsuserid);
                        request.putExtra("Lat", lat);
                        request.putExtra("Long", lng);
                        request.putExtra("category", category);
                        request.putExtra("regionstatus", regionsearchstatus);
                        request.putExtra("regionaddress", regionaddress);
                        request.putExtra("country", country);
                        System.out.println("Slide Main Activity Country" + country);
                        System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
                        System.out.println("Slide Lat " + lat);
                        System.out.println("Slide Lng" + lng);
                        startActivity(request);
                        finish();
                    } else {
                        close = false;
                        estimateclick = false;
                        System.out.println("Close is False when click request qctivity" + close);
                        String lat = new Double(Lastlat).toString();
                        String lng = new Double(Lastlng).toString();

                        Intent request = new Intent(getApplicationContext(), RequestnextActivity.class);
                        request.putExtra("userid", User_id);
                        request.putExtra("fbuserproimg", fbuserproimg);
                        System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
                        request.putExtra("whologin", WhoLogin);
                        request.putExtra("password", checkpassword);
                        request.putExtra("Driverid", driverdetailsuserid);
                        System.out.println("Request Function Driver id" + driverdetailsuserid);
                        request.putExtra("Lat", lat);
                        request.putExtra("Long", lng);
                        request.putExtra("category", category);

                        System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
                        System.out.println("Slide Lat " + lat);
                        System.out.println("Slide Lng" + lng);
                        startActivity(request);
                        finish();
                    }
                }
            }
        });


        action = (LinearLayout) findViewById(R.id.action);
        action.setVisibility(View.GONE);

        search1 = (LinearLayout) findViewById(R.id.search1);
        search1.setVisibility(View.GONE);


        gpsmove = (ImageView) findViewById(R.id.gpsloc);
        gpsmove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Location myLocation = mGoogleMap.getMyLocation();
                if (myLocation != null) {
                    double dLatitude = myLocation.getLatitude();
                    double dLongitude = myLocation.getLongitude();
                    Log.i("APPLICATION", " : " + dLatitude);
                    Log.i("APPLICATION", " : " + dLongitude);

                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 12));

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
                }


            }
        });
        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                close = false;
                desinationbuttonaction = "address";
                if (net_connection_check()) {

                    pickupaddress = Address1.getText().toString();


                    if (!pickupaddress.equals(getString(R.string.get_location))) {
                        Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                        search.putExtra("userid", User_id);
                        search.putExtra("fbuserproimg", fbuserproimg);
                        search.putExtra("whologin", WhoLogin);
                        search.putExtra("password", checkpassword);
                        search.putExtra("desinationbutton", desinationbuttonaction);
                        search.putExtra("address", pickupaddress);
                        search.putExtra("gpscountry", gpscountry);
                        System.out.println("1Accept add" + accept);
                        if (accept == null) {
                            search.putExtra("accept", "null");
                        } else if (accept.equals("yes")) {
                            search.putExtra("accept", accept);
                            accept1 = accept;
                        } else {
                            search.putExtra("accept", "null");
                        }

                        startActivity(search);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid address move location", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        search = (LinearLayout) findViewById(R.id.search);
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                close = false;
                desinationbuttonaction = "address";

                if (net_connection_check()) {
                    pickupaddress = Address.getText().toString();
                    if (!pickupaddress.equals("Getting location")) {
                        Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                        search.putExtra("userid", User_id);
                        search.putExtra("fbuserproimg", fbuserproimg);

                        search.putExtra("whologin", WhoLogin);
                        search.putExtra("password", checkpassword);
                        search.putExtra("desinationbutton", desinationbuttonaction);
                        search.putExtra("address", pickupaddress);
                        search.putExtra("gpscountry", gpscountry);

                        System.out.println("accept" + accept);
                        if (accept == null) {
                            search.putExtra("accept", "null");
                        } else if (accept.equals("yes")) {
                            search.putExtra("accept", accept);
                            accept1 = accept;
                        } else {
                            search.putExtra("accept", "null");
                        }

                        startActivity(search);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid address move location", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        minfarelin.setOnClickListener(new OnClickListener() {
            TextView baseamount, permin, perkm;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Dialog d = new Dialog(SlideMainActivity.this);
                d.setContentView(R.layout.faredetails);
                System.out.println("CAlled fare details desin");
                d.setTitle(R.string.fare_details);
                Window window = d.getWindow();
                window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                baseamount = (TextView) d.findViewById(R.id.baseamount);
                permin = (TextView) d.findViewById(R.id.permin);
                perkm = (TextView) d.findViewById(R.id.perkm);

                baseamount.setText("$" + minfares);
                permin.setText("$" + price_min);
                perkm.setText("$" + price_km);
                d.show();


            }
        });

        getfareestimate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                close = false;
                estimateclick = true;

                desinationbuttonaction = "fareestimate";
                if (net_connection_check()) {
                    pickupaddress = Address.getText().toString();
                    System.out.println("get FAre ESTimate button click" + desinationbuttonaction);
                    pickupaddress = Address.getText().toString();

                    if (!pickupaddress.equals("Getting location")) {
                        Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                        search.putExtra("userid", User_id);
                        search.putExtra("fbuserproimg", fbuserproimg);
                        //System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
                        search.putExtra("whologin", WhoLogin);
                        search.putExtra("password", checkpassword);
                        search.putExtra("desinationbutton", desinationbuttonaction);
                        search.putExtra("address", pickupaddress);
                        search.putExtra("gpscountry", gpscountry);
                        search.putExtra("showhandle", "showhandle");
                        System.out.println("accept" + accept);
                        if (accept == null) {
                            search.putExtra("accept", "null");
                        } else if (accept.equals("yes")) {
                            search.putExtra("accept", accept);
                            accept1 = accept;
                            mDrawerToggle.setDrawerIndicatorEnabled(false);
                            mDrawerList.setVisibility(View.INVISIBLE);
                        } else {
                            search.putExtra("accept", "null");
                        }

                        startActivity(search);
                        //  finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid address move location", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        slideback = (Button) findViewById(R.id.slideback);
        slideback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActionBar().hide();
                requestlin.setVisibility(View.GONE);

                System.out.println("SLIDE BACK BUTTON CLICK LAYOUT VISIBLE");
                layout.setVisibility(View.VISIBLE);
                action.setVisibility(View.GONE);
                search1.setVisibility(View.GONE);
                mDrawerList.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                markerText.setVisibility(View.VISIBLE);


            }
        });


        rideshare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {  // On Click for to sharing ride on phone contacts
                // TODO Auto-generated method stub
                getActionBar().hide();

                Intent search = new Intent(getApplicationContext(), SelectContacts.class);
                search.putExtra("userid", User_id);
                search.putExtra("fbuserproimg", fbuserproimg);
                search.putExtra("whologin", WhoLogin);
                search.putExtra("password", checkpassword);
                search.putExtra("desinationbutton", desinationbuttonaction);
                search.putExtra("address", pickupaddress);
                search.putExtra("destaddress", destaddress);
                search.putExtra("gpscountry", gpscountry);
                search.putExtra("lat", originlat);
                search.putExtra("lot", originlot);
                search.putExtra("acceptdriverid", acceptdriverid);

                if (accept == null) {
                    search.putExtra("accept", "null");
                } else if (accept.equals("yes")) {
                    search.putExtra("accept", accept);
                    accept1 = accept;
                } else {
                    search.putExtra("accept", "null");
                }
                startActivity(search);

            }
        });


        getkeys();


        getalldriverdetails();

// **************************  Google Map Code Start **********************


        // Getting Google Play availability status
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            // Create a new global location parameters object
            mLocationRequest = LocationRequest.create();

	            /*
	             * Set the update interval
	             */
            mLocationRequest.setInterval(GData.UPDATE_INTERVAL_IN_MILLISECONDS);

            // Use high accuracy
            mLocationRequest
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            //  Set the interval ceiling to one minute
            mLocationRequest
                    .setFastestInterval(GData.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

            // Note that location updates are off until the user turns them on
            mUpdatesRequested = false;

	            /*
	             * Create a new location client, using the enclosing class to handle
	             * callbacks.
	             */

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            googleApiClient.connect();

        }

        GCMRegistrar.checkDevice(this);

        // Make sure the manifest permissions was properly set 
        GCMRegistrar.checkManifest(this);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));


// **************************  Google Map Code End **********************	


        navrl.setOnClickListener(new OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @SuppressLint("NewApi")
            public void onClick(View v) {


                if (net_connection_check()) {


                    Intent edit = new Intent(getApplicationContext(), ProfileActivity.class);
                    edit.putExtra("userid", User_id);
                    edit.putExtra("fbuserproimg", fbuserproimg);
                    System.out.println("Profile Activity Fb Profile imag" + fbuserproimg);
                    edit.putExtra("whologin", WhoLogin);
                    System.out.println("Edit Button WhoLogin" + WhoLogin);
                    edit.putExtra("password", checkpassword);
                    edit.putExtra("accept", accept);


                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                    startActivity(edit, bndlanimation);
                    finish();

                }
            }
        });


        minfare();

        final Drawable d = getResources().getDrawable(R.drawable.track0);
        d.setBounds(new Rect(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight()));
        mybar.setProgressDrawable(d);

        final Drawable suv = getResources().getDrawable(R.drawable.suv);
        final Drawable sedan = getResources().getDrawable(R.drawable.sedan);
        final Drawable hatchback = getResources().getDrawable(R.drawable.hatchback);


        suv.setBounds(new Rect(0, 0, suv.getIntrinsicWidth(), suv.getIntrinsicHeight()));
        sedan.setBounds(new Rect(0, 0, sedan.getIntrinsicWidth(), sedan.getIntrinsicHeight()));
        hatchback.setBounds(new Rect(0, 0, hatchback.getIntrinsicWidth(), hatchback.getIntrinsicHeight()));

        final Drawable scar1 = getResources().getDrawable(R.drawable.scar1);
        final Drawable scar2 = getResources().getDrawable(R.drawable.scar2);
        final Drawable scar3 = getResources().getDrawable(R.drawable.scar3);


        scar1.setBounds(new Rect(0, 0, scar1.getIntrinsicWidth(), scar1.getIntrinsicHeight()));
        scar2.setBounds(new Rect(0, 0, scar2.getIntrinsicWidth(), scar2.getIntrinsicHeight()));
        scar3.setBounds(new Rect(0, 0, scar3.getIntrinsicWidth(), scar3.getIntrinsicHeight()));


        mybar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {

                if (net_connection_check()) {

                    if (progresscount == 5) {
                        markerText.setVisibility(View.INVISIBLE);
                        layout.showHandle();
                        Drawable set1 = markerText.getBackground();
                        Drawable set2 = getResources().getDrawable(R.drawable.setpickuploc1);

                        seekBar.setProgress(progresscount);
                        seekBar.setThumb(hatchback);


                        category = carcategory[0];
                        System.out.println("CATEGORY SELETED IS" + category);
                        System.out.println("Driver Details Category 1" + category);
                        Driverdetails();
                        if (estimateclick) {
                            //estimatefarecalculation();
                            fare_estimation();
                        }
                    } else if (progresscount == 50) {
                        markerText.setVisibility(View.INVISIBLE);
                        Drawable set1 = markerText.getBackground();
                        Drawable set2 = getResources().getDrawable(R.drawable.setpickuploc1);
                        layout.showHandle();

                        seekBar.setProgress(progresscount);
                        seekBar.setThumb(sedan);

                        //   minfare();

                        category = carcategory[1];
                        System.out.println("CATEGORY SELETED IS" + category);
                        System.out.println("Driver Details category 2" + category);
                        Driverdetails();
                        if (estimateclick) {
                            // estimatefarecalculation();
                            fare_estimation();
                        }
                    } else if (progresscount == 95) {
                        markerText.setVisibility(View.INVISIBLE);
                        Drawable set1 = markerText.getBackground();
                        Drawable set2 = getResources().getDrawable(R.drawable.setpickuploc1);
                        layout.showHandle();

                        seekBar.setProgress(progresscount);
                        seekBar.setThumb(suv);


                        //   minfare();

                        category = carcategory[2];
                        System.out.println("CATEGORY SELETED IS" + category);
                        System.out.println("Driver Details Category3" + category);
                        Driverdetails();
                        if (estimateclick) {
                            //estimatefarecalculation();
                            fare_estimation();
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //add here your implementation


            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                System.out.println("Progress is" + progress);
                check = true;
                if (progress <= 25) {

                    progresscount = 5;
                    seekBar.setThumb(scar1);
                } else if (progress > 25 && progress <= 75) {

                    progresscount = 50;
                    seekBar.setThumb(scar2);
                } else if (progress > 75) {

                    progresscount = 95;
                    seekBar.setThumb(scar3);
                }
                //add here your implementation
            }

        });
    }


    protected void finalize() throws Throwable {
        if (mHandleMessageReceiver != null) {
            this.unregisterReceiver(mHandleMessageReceiver);
        }
        super.finalize();

    }

    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if (estimateclick) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            location = sharedPreferences.getString("location", null);
            desinationbuttonaction = sharedPreferences.getString("desinationbutton", null);
            pickupaddress = sharedPreferences.getString("address", null);
            accept = sharedPreferences.getString("accept", null);
            System.out.println("1Accept onReusme" + accept);
            gpscountry = sharedPreferences.getString("gpscountry", null);
            // showhandle= sharedPreferences.getString("showhandle", null);


            System.out.println("location===>" + location);
            System.out.println("desinationbuttonaction===>" + desinationbuttonaction);
            System.out.println("pickupaddress===>" + pickupaddress);
            System.out.println("accept===>" + accept);
            System.out.println("gpscountry===>" + gpscountry);
            //System.out.println("showhandle===>"+showhandle);

            location = location.replaceAll("%20", " ");//new DecimalFormat("##.##").format(location)
            getfareestimate.setText(location);
            System.out.println("category======>" + category);
            fare_estimation();

        }


        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        }
    }


    BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);  // get push notification message

            if (newMessage != null || !newMessage.equals("null")) {
                if (newMessage == "tripend" || newMessage.equals("tripend"))   // if message is tripend to stop the trip
                {

                    if (secondrider) {
                        if (net_connection_check()) {
                            getamount1();
                        }
                    } else {
                        if (net_connection_check()) {
                            getamount();
                        }
                    }
                } else if (newMessage == "arrive" || newMessage.equals("arrive")) // if message is arrive driver arrived on rider location
                {

                    if (net_connection_check()) {
                        getamount();
                    }
                } else if (newMessage == "begin" || newMessage.equals("begin")) // if message is begin the trip was begin
                {
                    String title = getString(R.string.begin_trip);
                    String message = getString(R.string.begin_now);
                    accept = "nos";
                    System.out.println("1Accept begin" + accept);
                    markerText.setVisibility(View.GONE);
                    layout.setVisibility(View.GONE);
                    newMessage1 = newMessage;
                    System.out.println("DRIVER CANCEL REQUEST LAYOUT VISIBLE");

                    dialogshow(title, message);
                    if (net_connection_check())
                        autogetsingledriver();

                    if (secondrider) {
                        if (net_connection_check())
                            getdroplocation();
                    }

                } else if (newMessage == "split" || newMessage.equals("split")) {
                    if (net_connection_check()) {

                        secondrider = true;
                        getridesharedetails();
                    }


                } else if (newMessage == "Toll fee" || newMessage.equals(("Toll fee"))) {
                    if (net_connection_check()) {
                        get_tollfee();
                    }
                } else if (newMessage == "split accept" || newMessage.equals("split accept")) {

                    if (net_connection_check()) {
                        newMessage1 = newMessage;
                        getridersharestatus();
                    }


                } else if (newMessage == "Acknowledgement" || newMessage.equals("Acknowledgement")) {
                    cancelackalertdialog.dismiss();


                } else if (newMessage == "driver cancel" || newMessage.equals("driver cancel"))  /// Driver cancel request it show number of reason
                {
                    ;
                    if (net_connection_check()) {
                        get_driver_cancel_reson();
                    }


                    destinationeta.setVisibility(View.INVISIBLE);
                    spliting.setVisibility(View.GONE);
                    driverdetails.setVisibility(View.GONE);

                } else if (newMessage.contains("GetReady")) {


                    String firstAndSecond[] = newMessage.replaceAll("\\s", "").split(",");

                    pickupaddress = firstAndSecond[1].replace("+", " ");
                    location = firstAndSecond[2];
                    destaddress = firstAndSecond[2];
                    category = firstAndSecond[3];


                    pickupadd = getLocationFromAddress(pickupaddress);
                    destination = getLocationFromAddress(location);
                    System.out.println("PickupAddresslatlng" + pickupadd);
                    System.out.println("DestnationAddresslatlng" + destination);
                    System.out.println("First:" + firstAndSecond[1] + ", Second:" + firstAndSecond[2]);

                    android.support.v7.app.AlertDialog.Builder builder =
                            new android.support.v7.app.AlertDialog.Builder(SlideMainActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Your List.");
                    builder.setMessage("Do you want rider now?");
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            close = false;
                            estimateclick = false;
                            System.out.println("Close is False when click request qctivity" + close);
                            String lat = new Double(pickupadd.latitude).toString();
                            String lng = new Double(pickupadd.longitude).toString();
                            //close=false;
                            Intent request = new Intent(getApplicationContext(), RequestActivity.class);
                            request.putExtra("userid", User_id);
                            request.putExtra("fbuserproimg", fbuserproimg);
                            System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
                            request.putExtra("whologin", WhoLogin);
                            request.putExtra("password", checkpassword);
                            request.putExtra("destaddress", destaddress);
                            request.putExtra("Driverid", driverdetailsuserid);
                            System.out.println("Request Function Driver id" + driverdetailsuserid);
                            request.putExtra("Lat", lat);
                            request.putExtra("Long", lng);
                            request.putExtra("category", category);
                            //request.putExtra("name", driverdetailstime);
                            System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
                            System.out.println("Slide Lat " + lat);
                            System.out.println("Slide Lng" + lng);
                            startActivity(request);
                            finish();


                        }
                        //  alertdialog2.cancel();

                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            dialog.dismiss();
                        }
                        //  alertdialog2.cancel();

                    });

                    builder.show();


                } else if (newMessage == "Get Ready" || newMessage.equals("Get Ready")) {
                    String title = "Message!";
                    String message = "Message:Your Ride later reuqest has been activited.";
                    dialogshow(title, message);
                } else if (newMessage == "yes" || newMessage.equals("yes")) {

                    if (net_connection_check())
                        Accept();
                }
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            LatLng latLong;
            // TODO Auto-generated method stub


            mGoogleMap = googleMap;
            mGoogleMap.setMyLocationEnabled(true);


            // Zoom Control Position
            View zoomControls = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getView().findViewById(0x1);

            if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                // ZoomControl is inside of RelativeLayout
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

                // Align it to - parent top|left
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                // Update margins, set to 10dp
                final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics());
                params.setMargins(margin, margin, margin, margin + 150);
            }

            // Enabling MyLocation in Google Map
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);


            // Enable / Disable zooming functionality
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);


            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.setMinZoomPreference(6.0f);
            mGoogleMap.setMaxZoomPreference(14.0f);

            View locationButton = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getView().findViewById(2);

            // and next place it, for exemple, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            rlp.setMargins(0, 120, 30, 0);


            System.out.println("Selected Location Location is " + location);

            System.out.println("LOCATION IS IN THE SETUP MAP PAGE " + location);


            if (location == "siva") {
                System.out.println("insite the else if");

                gpslocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                latLong = new LatLng(gpslocation.getLatitude(), gpslocation.getLongitude());


                //Advertisement
                if (show_ad != null) {
                    if (show_ad.matches("ad")) {
                        thread();

                    }
                }
            } else if (location != "siva") {
                System.out.println("insite the if");

                latLong = getLocationFromAddress(location);
            }


            else {
                System.out.println("insite the else");

                latLong = new LatLng(12.9667, 77.5667);
            }


            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15f).tilt(0).build();

            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
            mGoogleMap.clear();

            mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition arg0) {
                    // TODO Auto-generated method stub
                    center = mGoogleMap.getCameraPosition().target;


                    markerLayout.setVisibility(View.VISIBLE);


                    try {

                        new GetLocationAsync(center.latitude, center.longitude).execute();

                    } catch (Exception e) {
                    }
                }
            });

            markerText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (net_connection_check()) {

                        try {



                            //************ Automatic Run Every Second Function ****************************


                            System.out.println("Marker Layout clickd");

                            Drawable set1 = markerText.getBackground();
                            Drawable set2 = getResources().getDrawable(R.drawable.setpickuploc1);
                            System.out.println("Set 1" + set1);
                            System.out.println("Set 2" + set2);



                            if (markerimagecount == 1 || markerimagecount == 2) {

                                System.out.println("Insite the if set pickup location");
                                markerText.setBackgroundResource(R.drawable.setpickuploc2);
                                markerimagecount = 2;
                                load.setVisibility(View.VISIBLE);
                                load.startAnimation(animRotate);
                            } else {
                                load.setAnimation(null);
                                markerText.setBackgroundResource(R.drawable.nocar1);
                                markerimagecount = 3;
                                load.setVisibility(View.INVISIBLE);
                                System.out.println("MARKER TEST CLICK LAYOUT VISIBLE");
                                layout.setVisibility(View.VISIBLE);
                            }
                            is_first = true;
                            System.out.println("Marker Text Click is First" + is_first);
                            System.out.println("Driver Details Marker test onClick"+category);
                            Driverdetails();


                            System.out.println("Set 1" + set1);
                            System.out.println("Set 2" + set2);
                            if (markerimagecount == 1 || markerimagecount == 2) {
                                System.out.println("sdgkasg jksg  jkg hsjkhg jkshg jkg k ");

                                getActionBar().hide();
                                requestlin.setVisibility(View.VISIBLE);
                                System.out.println("MARKER IMAGE COUNT LAYOUT VISIBLE");
                                layout.setVisibility(View.INVISIBLE);
                                action.setVisibility(View.VISIBLE);
                                search1.setVisibility(View.VISIBLE);

                                mDrawerList.setVisibility(View.VISIBLE);
                                search.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SlideMainActivity.this,"exp"+e,Toast.LENGTH_LONG).show();
        }


    }

// **************************  Sliding Menu Function Start **********************

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click

        return super.onOptionsItemSelected(item);

    }

	
	
	
	
	

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    private void displayView(int position) {
        // update the main content by replacing fragments

        switch (position) {
            case 0:
                if (net_connection_check()) {

                    System.out.println("Case 0");
                    close = false;
                    runsingledriver = false;

                    if (mHandleMessageReceiver != null) {
                        this.unregisterReceiver(mHandleMessageReceiver);
                    }
                    Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);


                    profile.putExtra("userid", User_id);
                    profile.putExtra("fbuserproimg", fbuserproimg);

                    profile.putExtra("whologin", WhoLogin);
                    profile.putExtra("password", checkpassword);
                    profile.putExtra("accept", accept);

                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                    startActivity(profile, bndlanimation);
                    finish();
                }
                break;
           case 1:
                //PaymentPageActivity
                if (net_connection_check()) {
                    close = false;
                    runsingledriver = false;
                    Intent pay = new Intent(getApplicationContext(), SosActivity.class);

                    pay.putExtra("userid", User_id);
                    pay.putExtra("fbuserproimg", fbuserproimg);
                    pay.putExtra("whologin", WhoLogin);
                    pay.putExtra("password", checkpassword);
                    pay.putExtra("accept", accept);
                    pay.putExtra("lat", String.valueOf(Lastlat));
                    pay.putExtra("long", String.valueOf(Lastlng));
                    startActivity(pay);
                    finish();
                }
                break;

/*	case 2:
		//PromotionsPage
        if(net_connection_check()) {
            close = false;
            runsingledriver=false;
            calltoadmin();
        }

	break;

            case 3:
                //FreeridesPage
                if (net_connection_check()) {
                    close = false;
                    runsingledriver = false;
                    Intent free = new Intent(getApplicationContext(), RegionSearchActivity.class);

                    free.putExtra("userid", User_id);
                    free.putExtra("fbuserproimg", fbuserproimg);

                    free.putExtra("whologin", WhoLogin);
                    free.putExtra("password", checkpassword);
                    free.putExtra("accept", accept);

                    startActivity(free);
                    finish();
                }
                break;
            case 4:
                //PromotionsPage
                if (net_connection_check()) {
                    close = false;
                    runsingledriver = false;
                    if (mHandleMessageReceiver != null) {
                        unregisterReceiver(mHandleMessageReceiver);

                    }
                    Intent pro = new Intent(getApplicationContext(), Swipe_tab.class);

                    pro.putExtra("userid", User_id);
                    pro.putExtra("fbuserproimg", fbuserproimg);
                    pro.putExtra("whologin", WhoLogin);
                    pro.putExtra("password", checkpassword);
                    pro.putExtra("accept", accept);

                    startActivity(pro);
                    finish();
                }
                break;

            case 5:
                //SupportPage
                if (net_connection_check()) {
                    close = false;
                    runsingledriver = false;
                    if (mHandleMessageReceiver != null) {
                        unregisterReceiver(mHandleMessageReceiver);

                    }
                    Intent sup = new Intent(getApplicationContext(), Ridelater.class);

                    sup.putExtra("userid", User_id);
                    sup.putExtra("fbuserproimg", fbuserproimg);
                    sup.putExtra("whologin", WhoLogin);
                    sup.putExtra("password", checkpassword);
                    sup.putExtra("accept", accept);

                    startActivity(sup);
                    finish();
                }
                break;*/

            case 2:
                if(net_connection_check()) {
                    close=false;
                    if (mHandleMessageReceiver != null) {
                        unregisterReceiver(mHandleMessageReceiver);

                    }
                    Intent history = new Intent(getApplicationContext(), Myreview.class);

                    history.putExtra("userid", User_id);
                    history.putExtra("fbuserproimg", fbuserproimg);

                    history.putExtra("whologin", WhoLogin);
                    history.putExtra("password", checkpassword);

                    startActivity(history);

                    finish();
                    break;
                }


   /*         case 2:
                if(net_connection_check()) {
                    close=false;
                    if (mHandleMessageReceiver != null) {
                        unregisterReceiver(mHandleMessageReceiver);

                    }
                    Intent history = new Intent(getApplicationContext(), Myreview.class);

                    history.putExtra("userid", User_id);
                    history.putExtra("fbuserproimg", fbuserproimg);

                    history.putExtra("whologin", WhoLogin);
                    history.putExtra("password", checkpassword);

                    startActivity(history);

                    finish();
                    break;
                }
*/


            case 3:
			//SupportPage
                if(net_connection_check()) {

                    close=false;
                    android.support.v7.app.AlertDialog.Builder builder =
                            new android.support.v7.app.AlertDialog.Builder(SlideMainActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Exit");
                    builder.setMessage("Do you want Exit now?");
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            whilecheck = false;
                            close=false;
                            runsingledriver=false;

                            if (mHandleMessageReceiver != null) {
                                unregisterReceiver(mHandleMessageReceiver);

                            }
                            loadSaved();
                            Clearsplitid();
                            Removedetails();
                            offriderdetails();
                            clearApplicationData();


                        }


                        //   if(net_connection_check()) {
                        //  close = false;
                        //   runsingledriver=false;
                    /*if (mHandleMessageReceiver != null) {
                        unregisterReceiver(mHandleMessageReceiver);

                    }
                    loadSaved();
                    Clearsplitid();
                    Removedetails();
                    offriderdetails();
                    clearApplicationData();*/

                        //  }
                        //  break;


                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            dialog.dismiss();
                        }
                        //  alertdialog2.cancel();

                    });

                    builder.show();


                    break;
                }

	default:
	break;
	}


    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // **************************  Sliding Menu Function End **********************
    // ************************** fare calculation **********************************
    public void estimatefarecalculation() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                float KM = address2distance;
                float min = address2mins;
                float pm, pkm, minfaref;


                pkm = Float.valueOf(price_km);
                minfaref = Float.valueOf(minfares);
                pm = Float.valueOf(price_min);

                float maxamount = minfaref + KM * pkm;



                System.out.println("Maximum  amount" + maxamount);
                System.out.println("KM" + KM);
                System.out.println("MIN" + min);
                System.out.println("Calculate Price per KM" + price_km);
                System.out.println("Calculate Price per MIN" + price_min);
                System.out.println("Calculate Minium Rate" + minfares);


                minfare.setText("$" + maxamount);
                minfare.setTextSize(40);
                textfare.setText("Estimated Fare");
                getfareestimate.setText(location);
            }
        }, 1500);
    }

    // **************************  Google Map Function Start **********************



    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        System.out.println("Accept is in on Connect function " + accept);

       // stupMap();


    }


    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub

            x = latitude;
            y = longitude;
            Lastlat = x;
            Lastlng = y;


        }

        @Override
        protected void onPreExecute() {
            Address.setText(R.string.get_location);
            Address1.setText(R.string.get_location);
            search.setEnabled(false);
            search1.setEnabled(false);
        }

        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(SlideMainActivity.this);
                addresses = geocoder.getFromLocation(x, y, 1);

                str = new StringBuilder();

                if (Geocoder.isPresent()) {

                    if (addresses.size() > 0) {
                        Address returnAddress = addresses.get(0);
                        String localityString = returnAddress.getLocality();
                        regionaddress = localityString;
                        String city = returnAddress.getCountryName();
                        String region_code = returnAddress.getCountryCode();
                        String zipcode = returnAddress.getPostalCode();

                        str.append(localityString + ", ");
                        str.append(city + ", " + region_code + ", ");
                        str.append(zipcode + "");
                        country=region_code;
                        System.out.println("**************************");
                        System.out.println("Country"+country);
                        System.out.println("ZipCode"+zipcode);
                        System.out.println("**************************");
                    } else {
                    }
                } else {
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                System.out.println("GPS ADDRESS NAME IS " + addresses.get(0));

                Address.setText(addresses.get(0).getAddressLine(0) + ","
                        + addresses.get(0).getAddressLine(1) + " ");
                Address1.setText(addresses.get(0).getAddressLine(0)
                        + addresses.get(0).getAddressLine(1) + " ");
                gpscountry = addresses.get(0).getCountryCode();

                search.setEnabled(true);
                search1.setEnabled(true);
                System.out.println("+++++++++++++++++++++++++++++++");
                System.out.println("GPS COUNTRY NAME IS " + gpscountry);
                System.out.println("+++++++++++++++++++++++++++++++");
                loadSavedPreferences();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(SlideMainActivity.this,Locale.ENGLISH);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    // **************************  Google Map  Function End **********************


    public void getalldriverdetails() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Close is " + close);

                while (close)
                    try {
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run() {
                                System.out.println("Driver Details get all driver detail funcation 10000"+category);
                                if(net_connection_check()) {
                                    Driverdetails();
                                    is_first = true;
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        // ooops
                    }
            }
        })).start();
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


                    System.out.println("BEFORE Driver cancel request funcation called" + whilecheck);
                    whilecheck = false;
                    runsingledriver = false;
                    System.out.println("AFTER Driver cancel request funcation called" + whilecheck);

                    System.out.println("Insite the Drive cancel request Function" + cancelreson);
                    String title = getString(R.string.trip_canceled);
                    String message = getString(R.string.cancel_reason) + cancelreson;
                    System.out.println("Remoe details");
//                    fareactivity.Removedetails(User_id);
                    System.out.println("Before remove user details");
//                    fareactivity.Removeuserdetails(User_id);
                    System.out.println("Before foofle mao");
                    if(mGoogleMap!=null) {
                        mGoogleMap.clear();
                    }
                    System.out.println("Before remove user details");


                    System.out.println("DRIVER CANCEL REQUEST LAYOUT VISIBLE");
                    layout.setVisibility(View.VISIBLE);
                    markerText.setVisibility(View.VISIBLE);
                    load.setVisibility(View.INVISIBLE);

                    search.setVisibility(View.VISIBLE);
                    destinationeta.setVisibility(View.INVISIBLE);

                    load.setAnimation(null);

                    dialogshow(title, message);
                    close = true;
                    category = "Siva";
                    System.out.println("Driver Details getDriver Cancel reason"+category);
                    Driverdetails();

                    minfare();
                    getalldriverdetails();

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


    public void Driverdetails() {
        final String url;

        System.out.println("regionsearchstatus Driver details funcation" + regionsearchstatus);

        if (regionsearchstatus == null || regionsearchstatus.equals("OFF")) {
           // url = Liveurl + "get_driving_information?userid=" + User_id + "&lat=" + Lastlat + "&long=" + Lastlng + "&category=" + category+"&country=" + country;
            url = Constants.Urls.URL_USER_GET_DRIVER_INFO+"driver_id=" + User_id + "&lat=" + Lastlat + "&long=" + Lastlng + "&category=" + category+"&country=" + country;
            System.out.println("URL isURL is" + url);
        } else {
            url = Constants.Urls.URL_USER_GET_DRIVER_INFO+"driver_id=" + User_id + "&lat=" + Lastlat + "&long=" + Lastlng + "&category=" + category+"&country=" + country + "&region=" + regionaddress;
        }// Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        Toast.makeText(SlideMainActivity.this,""+response,Toast.LENGTH_LONG).show();
                      /*  if (mGoogleMap != null)
                            mGoogleMap.clear();
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                driverdetails_jsonobj = response.getJSONObject(i);
                                driverdetailsjson_status = driverdetails_jsonobj.getString("status");
                                Log.d("OUTPUT IS", driverdetailsjson_status);
                                if (driverdetailsjson_status.equals("Success")) {
                                    markerText.setVisibility(View.VISIBLE);
                                    driverdetailsuserid = driverdetails_jsonobj.getString("id");
                                    driverdetailslat = driverdetails_jsonobj.getString("lat");
                                    driverdetailslong = driverdetails_jsonobj.getString("long");
                                    String time = driverdetails_jsonobj.getString("trip_time");

                                    eta.setText(time);
                                    StringTokenizer tokens = new StringTokenizer(time, " ");

                                    String first_string = tokens.nextToken();
                                    String File_Ext = tokens.nextToken();

                                    driverdetailstime = first_string;

                                    lat1 = Double.parseDouble(driverdetailslat);
                                    log1 = Double.parseDouble(driverdetailslong);

                                    old = new LatLng(lat1, log1);

                                    oldlatlng[i] = old;

                                    if (first == true) {
                                        old = new LatLng(lat1, log1);
                                        oldlatlng[l] = old;
                                        mark1 = oldlatlng[l];
                                    } else {
                                        mark1 = oldlatlng[l];
                                    }


                                    mark = new LatLng(lat1, log1);




                                    if (is_first) {
                                        System.out.println("Marker Called");

                                        addmarker();
                                        load.startAnimation(animRotate);
                                        load.setVisibility(View.VISIBLE);

                                        markerText.setBackgroundResource(R.drawable.setpickuploc1);
                                        markerimagecount = 1;
                                        markerText.setVisibility(View.VISIBLE);

                                        if (i == 0) {
                                            minutes.setText(getString(R.string.driver_pickyou) + driverdetailstime + " mins");
                                            if (!driverdetailstime.equals("null")) {

                                                int c = driverdetailstime.length();
                                                if (c == 1) {
                                                    markerText.setText("   " + driverdetailstime + "\nMIN");
                                                } else {
                                                    markerText.setText("  " + driverdetailstime + "\nMIN");
                                                }

                                            } else {

                                                load.setAnimation(null);
                                                load.setVisibility(View.INVISIBLE);
                                                markerText.setText("");
                                                markerText.setBackgroundResource(R.drawable.nocar);
                                                markerimagecount = 4;

                                            }


                                        }
                                    } else {
                                        if (usercheck == true) {
                                            System.out.println("User details Send Called");
                                            Userdetails();
                                        }
                                    }

                                } else if (driverdetailsjson_status.equals("fail")) {

                                    load.setAnimation(null);
                                    load.setVisibility(View.INVISIBLE);
                                    markerText.setText("");
                                    markerText.setBackgroundResource(R.drawable.nocar);
                                    markerText.setText("");

                                    markerimagecount = 4;

                                    eta.setText("--");

                                    System.out.println("Insite the Fail to find location");
                                    if (!Lastlat.equals(lat3) || !Lastlng.equals(lat4)) {
                                        System.out.println("Check Last loction is same or not");
                                        if (!isFinishing()) {

                                            load.setAnimation(null);
                                            load.setVisibility(View.INVISIBLE);
                                            markerText.setText("");
                                            markerText.setBackgroundResource(R.drawable.nocar);
                                            markerimagecount = 4;
                                            markerText.setText("");
                                            getActionBar().hide();
                                            requestlin.setVisibility(View.GONE);
                                            action.setVisibility(View.GONE);
                                            search1.setVisibility(View.GONE);
                                            mDrawerList.setVisibility(View.VISIBLE);
                                            search.setVisibility(View.VISIBLE);
                                            System.out.println("FAIL TO FIND THE LOCATION LAYOUT VISIBLE");

                                            layout.setVisibility(View.VISIBLE);


                                        }
                                        lat3 = Lastlat;
                                        lat4 = Lastlng;
                                    }
                                    markerText.setVisibility(View.VISIBLE);

                                } else {
                                    System.out.println("Status check is in else");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }*/
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {

                    //dialogcalling();
                }
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    public void addmarker() {

        System.out.println("Lat is" + driverdetailslat);
        System.out.println("Lng is" + driverdetailslong);

        Location locationA = new Location("point A");

        locationA.setLatitude(Lastlat);
        locationA.setLongitude(Lastlng);
;

        Location locationB = new Location("point B");

        locationB.setLatitude(lat1);
        locationB.setLongitude(log1);

        float distance = locationA.distanceTo(locationB);
        float loc = distance / 1000;
        System.out.println("Distance" + loc);

        if (mark1 == null) {
            mark1 = new LatLng(lat1, log1);
        } else {
            mark1 = mark;
        }

        mark = new LatLng(lat1, log1);



        Location bearlocation = new Location("starting_point");
        bearlocation.setLatitude(lat1);
        bearlocation.setLongitude(log1);
        rotation_angle = bearlocation.getBearing();
        System.out.println("Rotation Angle" + rotation_angle);
        if (mGoogleMap != null) {

            if (category == "SUV") {
                marker = mGoogleMap.addMarker(new MarkerOptions().position(mark1)

                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(logobitmap))
                        .flat(true).rotation(rotation_angle));


            } else if (category == "Sedan") {
                marker = mGoogleMap.addMarker(new MarkerOptions().position(mark1)

                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cargreen))
                        .flat(true).rotation(rotation_angle));
            } else {
                marker = mGoogleMap.addMarker(new MarkerOptions().position(mark1)

                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.carred))
                        .flat(true).rotation(rotation_angle));
            }



            System.out.println("MARK lat" + mark.latitude + "Mark Lng" + mark.longitude);
            System.out.println("MARK1 lat" + mark1.latitude + "Mark1 Lng" + mark1.longitude);

            if (mark.latitude != mark1.latitude && mark.longitude != mark1.longitude) {

                System.out.println("l================" + l);
                animateMarker(marker, mark, false);
                System.out.println("After Animater funcation Called");
                oldlatlng[l] = mark;

                System.out.println("DRIVER NAME" + driverdetailsname + "MARK  " + mark);
                System.out.println("DRIVER NAME" + driverdetailsname + "MARK1 " + mark1);
                System.out.println("LAT LNG  ARRAY" + oldlatlng[l]);

                for (int k = 0; k < n; k++) {
                    old = new LatLng(lat1, log1);
                    oldlatlng[k] = old;
                    mark1 = oldlatlng[k];
                }
            }

        } else {
            System.out.println("mGoogleMap" + mGoogleMap);
        }

    }


    public void animatemarkerarrive() {
        System.out.println("Arrive driver Lat is" + arrivelat);
        System.out.println("Arrive driver Lng is" + arrivelng);

        if (is_first) {
            arrivelat1 = arrivelat;
            arrivelng1 = arrivelng;
        }

        Location locationA = new Location("point A");

        locationA.setLatitude(arrivelat1);
        locationA.setLongitude(arrivelng1);


        Location locationB = new Location("point B");

        locationB.setLatitude(arrivelat);
        locationB.setLongitude(arrivelng);

        float distance = locationA.distanceTo(locationB);
        float loc = distance / 1000;
        System.out.println("Distance" + loc);

        if (mark1 == null) {
            mark1 = new LatLng(arrivelat1, arrivelng1);
        } else {
            mark1 = mark;
        }

        mark = new LatLng(arrivelat, arrivelng);



        Location bearlocation = new Location("starting_point");
        bearlocation.setLatitude(arrivelat);
        bearlocation.setLongitude(arrivelng);
        rotation_angle = bearlocation.getBearing();
        System.out.println("Rotation Angle" + rotation_angle);
        if (mGoogleMap != null) {

            if (category == "SUV") {
                marker = mGoogleMap.addMarker(new MarkerOptions().position(mark1)

                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(logobitmap))
                        .flat(true).rotation(rotation_angle));


            }

            System.out.println("Arrive MARK lat" + mark.latitude + "Arrive Mark Lng" + mark.longitude);
            System.out.println("Arrive MARK1 lat" + mark1.latitude + "Arrive Mark1 Lng" + mark1.longitude);

            if (mark.latitude != mark1.latitude && mark.longitude != mark1.longitude) {

                animateMarker(marker, mark, false);
                System.out.println("After Arrive Animater funcation Called");
                oldlatlng[l] = mark;

                System.out.println("Arrive DRIVER NAME" + driverdetailsname + "MARK  " + mark);
                System.out.println("Arrive DRIVER NAME" + driverdetailsname + "MARK1 " + mark1);
                System.out.println("Arrive LAT LNG  ARRAY" + oldlatlng[l]);

                for (int k = 0; k < n; k++) {
                    old = new LatLng(lat1, log1);
                    oldlatlng[k] = old;
                    mark1 = oldlatlng[k];
                }
            }

        } else {
            System.out.println("mGoogleMap" + mGoogleMap);
        }

    }

	    



    private class MyLocationListener implements LocationListener {


        public void onLocationChanged(Location location) {

            String message = String.format(

                    "New Location \n Longitude: %1$s \n Latitude: %2$s",

                    location.getLongitude(), location.getLatitude()
            );

            gpslatlng = new LatLng(location.getLongitude(), location.getLatitude());

        System.out.println("ON Location Change Accept"+accept);
            System.out.println("1Accept onlocation"+accept);
          if (accept == null || accept.equals("null")) {

                if (net_connection_check()) {
                    close = true;
                    System.out.println("Category is on Location change"+category);
                    if(category==null||category.equals("null")) {
                        category = carcategory[0];
                    }
                    System.out.println("Driver Details on My Location Change Accept Null" + category);
                    Driverdetails();

                  //  minfare();
                }
                System.out.println("Not Accepted Your Request Try Later" + close + "Accept is" + accept);

            }else if (accept.equals("no") && begintripauto) {
                System.out.println("Accepted data is null" + close + "Accept is" + accept);
                close = false;


                if (net_connection_check()) {
                    if(category==null||category.equals("null")) {
                        category = carcategory[0];
                    }
                    System.out.println("Driver Details on My Location Change Accept No & Begin trip" + category);
                    Driverdetails();

                   // minfare();
                }
            }
        }


        public void onStatusChanged(String s, int i, Bundle b) {



        }


        public void onProviderDisabled(String s) {



        }


        public void onProviderEnabled(String s) {


        }


    }


/// ******************************* User Details Send to server ************************

    public void Userdetails() {

        usercheck = false;


        final String url = Liveurl + "update_location?userid=" + User_id + "&lat=" + Lastlat + "&long=" + Lastlng + "&driverid=" + driverdetailsuserid;
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
                                Log.d("OUTPUT IS", driverdetailsjson_status);
                                if (driverdetailsjson_status.matches("Success")) {

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

    public void autogetsingledriver() {
        // ******************************* Automatically get amount Details *****************

        System.out.println("Insite the auto get Single driver" + whilecheck);
	

	

        (new Thread(new Runnable() {

            @Override
            public void run() {
                // while (!Thread.interrupted())
                //while (!isExit)
                System.out.println("INSITE auto get RUn Insite the  driver cancel request funcation called" + whilecheck);




                while (whilecheck)


                    try {
                        System.out.println("Insite the TRY " + whilecheck);

                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run() {

                                if (runsingledriver) {
                                    if (net_connection_check()) {
                                        System.out.println("Single driver function Calling");
                                      //  singledriver();
                                        get_single_driver getsingledriver = new get_single_driver();
                                        getsingledriver.execute();
                                    }




                                }

                            }
                        });
                    } catch (InterruptedException e) {
                        // ooops
                        System.out.println("GOOGLE MAP CLEAR IN CATCH FUNCTION");

                        mGoogleMap.clear();
                    }


            }

        })).start();


    }

    public void singledriver() {
        try {


            driverdetails_Url = new URL(Liveurl1 + "display_details?user_id=" + acceptdriverid);
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
                    String singledriveruserid, singledriverlat, singledriverlong;
                    singledriveruserid = driverdetails_jsonobj.getString("id");
                    singledriverlat = driverdetails_jsonobj.getString("lat");
                    singledriverlong = driverdetails_jsonobj.getString("lot");

                    System.out.println("singledriverlat"+singledriverlat);
                    System.out.println("singledrierlng"+singledriverlong);
                    if (!driverdetails_jsonobj.getString("lat").equals("null")) {
                        Double slat1 = Double.parseDouble(singledriverlat);
                        Double slog1 = Double.parseDouble(singledriverlong);
                        origin = new LatLng(slat1, slog1);

                        arrivelat = slat1; //arrive driver current lat
                        arrivelng = slog1; //arrive driver current lng

                        arriveA=new LatLng(arrivelat,arrivelng);





                        System.out.println("ACCCCCCept status insite the poly line draw" + accept);

                        System.out.println("Rotation Angle" + rotation_angle);

                        System.out.println("1Accept single driver"+accept);
                        if (accept == "yes" || accept.equals("yes")) {

                            Double orilat, orilot;

                            System.out.println("Origin Lat is " + originlat);
                            System.out.println("Origin Lot is " + originlot);

                            orilat = Double.parseDouble(originlat);
                            orilot = Double.parseDouble(originlot);

                            dest = new LatLng(orilat, orilot);

                        } else {

                            dest = destination;

                        }




                        if (arriveB == null) {

                            arriveB = arriveA;
                        }


                        // MKU MDU
                        // TPJ

                        drawpolylline = true;
                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);
                        System.out.println("After call direction url" + url);


                        DownloadTask downloadTask = new DownloadTask();


                        //Start downloading json data from Google Directions API
                        downloadTask.execute(url);


                       Location bearlocation = new Location("starting_point");
                        bearlocation.setLatitude(arriveB.latitude);
                        bearlocation.setLongitude(arriveB.longitude);
                        rotation_angle1 = bearlocation.getBearing();

                        System.out.println("Accept MARK lat" + arriveA.latitude + "Mark Lng" + arriveA.longitude);
                        System.out.println("Accept MARK1 lat" + arriveB.latitude + "Mark1 Lng" + arriveB.longitude);


                        System.out.println("After Add Accept MARK lat" + arriveA.latitude + "Mark Lng" + arriveA.longitude);

                        //  animatemarkerarrive(); // animate Marker in smooth Move

                    }

                } else if (driverdetailsjson_status.equals("fail")) {

                } else {
                    System.out.println("Status check is in else");
                }

            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
         //   e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
           // e.printStackTrace();
        }


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
                                    whilecheck = false;

                                    close = false;
                                    runsingledriver=false;
                                    System.out.println("Before call fareActivity" + close);
                                    Intent fare = new Intent(getApplicationContext(), FareActivity.class);
                                    fare.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    fare.putExtra("EXIT", true);
                                    fare.putExtra("userid", User_id);
                                    fare.putExtra("tripid", tripid);
                                    fare.putExtra("rideshareid", "null");
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
                                    fare.putExtra("rideshare", rideshares);
                                    System.out.println("FP Auto Trip Id" + autotripid);
                                    System.out.println("FP Driver Id" + driverid);


                                    startActivity(fare);
                                    finish();

                                } else if (arrive.equals("yes")) {
                                    System.out.println("The Amount is not insite the getamount method ELSE If " + totalamount + "" + arrive);
                                    if (Arrive == true) {

                                        System.out.println("Close Arrive yes" + close);

                                        arrive();
                                    }
                                } else {

                                    System.out.println("The Amount is not insite the getamount method" + totalamount + "" + arrive);


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

        System.out.println("Amount is Before Return" + driverdetailsjson_status);
        return totalamount;
    }

    public String getamount1() {

        final String url = Liveurl + "get_amount1?userid=" + rideshareid;
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
                                totalamount = driverdetails_jsonobj.getString("amount1");
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
                                    pickup = driverdetails_jsonobj.getString("drop"); ///change after complete
                                    drop = driverdetails_jsonobj.getString("drop");
                                    date = driverdetails_jsonobj.getString("timestamp");
                                    service = driverdetails_jsonobj.getString("service");//service1
                                    distance = driverdetails_jsonobj.getString("distance"); // distance1
                                    waiting = driverdetails_jsonobj.getString("waitingtime"); //waitigtime1

                                    System.out.println("Waiting in Rider Slide main activity" + waiting);
                                    whilecheck = false;

                                    close = false;
                                    runsingledriver=false;
                                    System.out.println("Before call fareActivity" + close);
                                    Intent fare = new Intent(getApplicationContext(), FareActivity.class);
                                    fare.putExtra("userid", User_id);
                                    fare.putExtra("tripid", tripid);
                                    fare.putExtra("rideshareid", rideshareid);
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
                                    fare.putExtra("rideshare", rideshares);
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

                        dialogcalling();
                    }
	 				VolleyLog.d(TAG, "Error: " + error.getMessage());
	 			}
	 		});

	 // Adding request to request queue
	 AppController.getInstance().addToRequestQueue(movieReq);
	 
	    System.out.println("Amount is Before Return"+driverdetailsjson_status);
	    return totalamount;
	    }

	    public void onBackPressed()
	    {


	    	
	    	finish();
	     	int pid = android.os.Process.myPid();
	        android.os.Process.killProcess(pid);
	        System.exit(0);
	 	
	    }


    private void arrive() {
        System.out.println("Insite the Arrive Button");
        Arrive = false;
	    	
	    	android.support.v7.app.AlertDialog.Builder builder =
					new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
	    	

	    	        builder.setMessage(R.string.driver_arrived);
	    	        builder.setCancelable(false);
	    	        builder.setInverseBackgroundForced(false);
	    	        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                System.out.println("Arrive Button Ok Click" + close);

            }
        });



        builder.show();

    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mGoogleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 10000;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
           long elapsed;
            float t;
            double lat,lng;
            @Override
            public void run() {

              //  System.out.println(" Start animateMarker is " + start);
                 elapsed = SystemClock.uptimeMillis() - start;
                t = interpolator.getInterpolation((float) elapsed
                        / duration);
              //  System.out.println(" Found Elapsed ="+elapsed+"T animateMarker is " + t);
                lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;

                lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;


                Double dLon = toPosition.longitude - startLatLng.longitude;
                Double y = Math.sin(dLon) * Math.cos(toPosition.latitude);
                Double x = Math.cos(startLatLng.latitude) * Math.sin(toPosition.latitude) - Math.sin(startLatLng.latitude) * Math.cos(toPosition.latitude) * Math.cos(dLon);
                float bearing = (float) ((float) Math.atan2(y, x) * 180 / Math.PI);

                Location bearlocation = new Location("starting_point");
                bearlocation.setLatitude(lat);
                bearlocation.setLongitude(lng);
                rotation_angle = bearlocation.getBearing();

                marker.setRotation(bearing);
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    handler.postDelayed(this, 0);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }


    public void newtry() {
        mGoogleMap.addMarker(new MarkerOptions().position(mark1).title("Start"));
        mGoogleMap.addMarker(new MarkerOptions().position(mark).title("End"));

        com.google.android.gms.maps.model.LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(mark1);
        boundsBuilder.include(mark);

        final LatLngBounds bounds = boundsBuilder.build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 12));

    }

    private void buildAlertMessageNoGps() {
	        android.support.v7.app.AlertDialog.Builder builder =
					new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(R.string.gps_disabled)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.w(((Object) this).getClass().getSimpleName(),
                "onConnectionSuspended() called, whatever that means");
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String key = "key="+googleapikey;
        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url 
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url 
            urlConnection.connect();

            // Reading data from url 
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (drawpolylline) {
                mGoogleMap.clear();
                drawMarker(arriveB);  // when you start polyline to remove the comment line
                drawMarker1(dest);


                if (arriveA.latitude != arriveB.latitude || arriveA.longitude != arriveB.longitude) {

                    System.out.println("INSIDE the IF CONTIDION animate MArker Called" + markerorigin + "arriveA" + arriveA);

                    animateMarker(markerorigin, arriveA, false);
                    arriveB = arriveA;


                }
            }

            if (result.size() < 1) {

                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLUE);
            }

            if (drawpolylline) {



                  /// draw marker in origin and dest location
                mGoogleMap.addPolyline(lineOptions);      /// Draw poly line
                System.out.println("After draw polyline");
                System.out.println("1Accept insite draw polyline"+accept);
                if (accept.equals("yes") || accept == "yes") {
                    search.setVisibility(View.INVISIBLE);
                    destinationeta.setVisibility(View.VISIBLE);
                    distanceeta.setText(getString(R.string.driver_pick_loc) + duration);
                } else if (newMessage1 == "begin" || newMessage1.equals("begin")) {
                    search.setVisibility(View.INVISIBLE);
                    destinationeta.setVisibility(View.VISIBLE);
                    distanceeta.setText(getString(R.string.driver_drop) + duration);
                } else {
                    search.setVisibility(View.VISIBLE);
                    destinationeta.setVisibility(View.INVISIBLE);
                }

                StringTokenizer tokens = new StringTokenizer(distance, " ");

                String first_string = tokens.nextToken();
                String File_Ext = tokens.nextToken();

                distance = first_string;
                StringTokenizer tokens1 = new StringTokenizer(duration, " ");

                String first_string1 = tokens1.nextToken();
                String File_Ext1 = tokens1.nextToken();

                duration = first_string1;
                distance = distance.replaceAll(",", "");

                System.out.println("Duration Before force stop" + duration);
                System.out.println("Distance Before force stop" + distance);
                address2distance = Float.valueOf(distance);
                address2mins = Float.valueOf(duration);

            } else {
                StringTokenizer tokens = new StringTokenizer(distance, " ");

                String first_string = tokens.nextToken();
                String File_Ext = tokens.nextToken();

                distance = first_string;
                StringTokenizer tokens1 = new StringTokenizer(duration, " ");

                String first_string1 = tokens1.nextToken();
                String File_Ext1 = tokens1.nextToken();

                duration = first_string1;
                distance = distance.replaceAll(",", "");

                System.out.println("Duration Before force stop" + duration);
                System.out.println("Distance Before force stop" + distance);
                address2distance = Float.valueOf(distance);
                address2mins = Float.valueOf(duration);
                if (accept.equals("yes") || accept == "yes") {
                    search.setVisibility(View.INVISIBLE);
                    destinationeta.setVisibility(View.VISIBLE);
                    distanceeta.setText(getString(R.string.driver_pick_loc) + duration);
                } else if (newMessage1 == "begin" || newMessage1.equals("begin")) {
                    search.setVisibility(View.INVISIBLE);
                    destinationeta.setVisibility(View.VISIBLE);
                    distanceeta.setText(getString(R.string.driver_drop) + duration);
                } else {
                    search.setVisibility(View.VISIBLE);
                    destinationeta.setVisibility(View.INVISIBLE);
                    distanceeta.setText(R.string.driver_drop + duration);
                }

                System.out.println("Duration After force stop" + duration);
                System.out.println("Distance After force stop" + distance);

                System.out.println("Distance from " + address2distance + "Duration" + address2mins);
            }

            // Drawing polyline in the Google Map for the i-th route

        }
    }

    private void drawMarker(LatLng point) {
		



        markerorigin = mGoogleMap.addMarker(new MarkerOptions().position(point)

                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.carblue))
                .flat(true).rotation(rotation_angle));
    }

    private void drawMarker1(LatLng point) {


        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(point);

        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */

        if (accept == "yes" || accept.equals("yes")) {

            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.manmapicon));

        } else {

            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));

        }


			
			// Add new marker to the Google Map Android API V2
			mGoogleMap.addMarker(options);		
		}

		private void dialogshow(String title,String msg)
		{
		System.out.println("Insite the Drive cancel after accept requst dialog alert show");
			android.support.v7.app.AlertDialog.Builder builder =
					new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
			builder.setTitle(title);
		    builder.setMessage(msg);

		    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		     	 
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
            }
            //  alertdialog2.cancel();

        });


		    builder.show();

    }

    public void getlogo() {


        try {
            driverdetails_Url = new URL(Liveurl + "customlogo?userid=" + User_id);
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
                logoimage = driverdetails_jsonobj.getString("logoimg");
                logoname = driverdetails_jsonobj.getString("logoname");

                if (logoimage == null || logoimage.equals("null")) {
                    //Bitmap logobitmap;
                    logobitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.carblue);
                } else {
                    logobitmap = getBitmapFromURL(logoimage);
                }

                System.out.println("LOGO IMAGE IS" + logoimage);
                System.out.println("LOGO Bitmap IS" + logobitmap);
                System.out.println("LOGO NAME IS" + logoname);
            }


            if (driverdetailsjson_status.matches("Success")) {

                System.out.println("User details updated successfully");



            } else {

                System.out.println("User details not updated");

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


    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void loadSavedPreferences() {
        System.out.println("User has successfully logged in, save this information");
        // We need an Editor object to make preference changes.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Set "hasLoggedIn" to true
        editor.putString("gpscountry", gpscountry);
        editor.putString("country", country);
        editor.putString("regionaddress",regionaddress);
        editor.putString("regionsearchstatus",regionsearchstatus);


        // Commit the edits!
        editor.commit();

    }


    public void minfare() {
        if (net_connection_check()) {

                getcategory();



        }

       // get_allcategory_details();

    }

    public void get_allcategory_details() {
        final String url = Liveurl + "get_category_details?category=" + category;
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

                                    catname = driverdetails_jsonobj.getString("categoryname");
                                    price_min = driverdetails_jsonobj.getString("price_minute");
                                    price_km = driverdetails_jsonobj.getString("price_km");
                                    sizes = driverdetails_jsonobj.getString("max_size");
                                    minfares = driverdetails_jsonobj.getString("price_fare");
                                    primetime_precentage = driverdetails_jsonobj.getString("primetime_precentage");

                                    System.out.println("Price per KM" + price_km);
                                    System.out.println("Price per MIN" + price_min);


                                    max_sizef = Float.valueOf(sizes);
                                    min_faref = Float.valueOf(minfares);
                                    price_minutef = Float.valueOf(price_min);
                                    price_kmf = Float.valueOf(price_km);
                                    size.setText(sizes);


                                    Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.primelogo);
                                    img.setBounds(0, 0, 30, 30);

                                    if (primetime_precentage == "null" || primetime_precentage.equals("null")) {
                                        categoryitem1.setCompoundDrawables(null, null, null, null);
                                        categoryitem2.setCompoundDrawables(null, null, null, null);
                                        categoryitem3.setCompoundDrawables(null, null, null, null);
                                    } else {
                                        categoryitem1.setCompoundDrawables(img, null, null, null);
                                        categoryitem2.setCompoundDrawables(img, null, null, null);
                                        categoryitem3.setCompoundDrawables(img, null, null, null);
                                    }



                                    System.out.println("User details updated successfully");

                                    FixedAmount();

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


    public void FixedAmount() {


        try {

            driverdetails_Url = new URL(Liveurl + "GetFixedAmount");
            System.out.println("Driver Name URL" + driverdetails_Url);
            BufferedReader login_reader;
            String str_login = "";

            login_reader = new BufferedReader(new InputStreamReader(driverdetails_Url.openStream()));

            System.out.println("Driver Name Reader" + login_reader);

            String s = driverdetails_Url.toString();
            System.out.println("Driver Name String" + s);
            while ((driverdetails_inputline = login_reader.readLine()) != null) {

                str_login += driverdetails_inputline;
                System.out.println("Driver Name Input Line" + str_login);


            }

            System.out.print("Driver Name Outsite While" + str_login);

            driverdetails_jsonarray = new JSONArray(str_login);

            for (int i = 0; i < driverdetails_jsonarray.length(); i++) {


                driverdetails_jsonobj = driverdetails_jsonarray.getJSONObject(i);
                IsFixed = driverdetails_jsonobj.getString("IsFixed");


                if (IsFixed == "1" || IsFixed.equals("1")) {
                    FixedAmount = driverdetails_jsonobj.getString("FixedAmount");
                    checkfixedamount = true;
                } else {
                    PresentageAmount = driverdetails_jsonobj.getString("percentage_amount");
                    checkfixedamount = false;
                }
                System.out.println("User Status" + driverdetailsjson_status);



            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }

        int totalcheck=0;

        if (checkfixedamount)  /// Admin set pay mode for Fixed Amount
        {
            if(FixedAmount!=null||!FixedAmount.equals(null))
            totalcheck = Integer.parseInt(FixedAmount);
        } else {
            if(PresentageAmount!=null||!PresentageAmount.equals(null))
            totalcheck = Integer.parseInt(PresentageAmount);
        }

        System.out.println("================================================================================");
        System.out.println("Total Check" + totalcheck);
        System.out.println("ISFixed" + IsFixed);
        System.out.println("FixedAmount" + FixedAmount);
        System.out.println("================================================================================");


        if (checkfixedamount) // Fixed amount
        {



            minfareamount = (int) (totalcheck + min_faref + price_minutef + price_kmf);
        } else {
            int driverfare = (int) (min_faref + price_minutef + price_kmf);

            int admincom = driverfare * totalcheck / 100;

            System.out.println("Admin Commition IS" + admincom);
            System.out.println("Driver fare amount" + driverfare);
            minfareamount = driverfare + admincom;
        }


        System.out.println("MinFareAMount" + minfareamount);
        System.out.println("Base Fare" + min_faref);
        System.out.println("Price Min" + price_minutef);
        System.out.println("Price km" + price_kmf);

        System.out.println("Admin Amount" + (min_faref + price_minutef + price_kmf) * (totalcheck / 100));
        if (!estimateclick) {
            minfare.setText("$" + minfareamount);
            textfare.setText("MIN FARE");
        }

        System.out.println("ISFixed" + IsFixed);
        System.out.println("FixedAmount" + FixedAmount);
        isfixed = Integer.parseInt(IsFixed);


        System.out.println("==========================================================================");
        System.out.println("MINFARE AMOUNT" + minfare);
        System.out.println("ISFixed" + IsFixed);
        System.out.println("FixedAmount" + FixedAmount);
        System.out.println("==========================================================================");

    }


    public void driverdisplaydetails() {

        //http://54.164.64.111/arcanemobile/drivers/display_personal_details?user_id=55b27aac230d76a52ccba3c3
        final String url = Liveurl1 + "display_personal_details?user_id=" + acceptdriverid;
        System.out.println("URL is" + url);

        //   Toast.makeText(getApplicationContext(), "Register URL IS "+fbsignup_Url, 1000).show();

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

                                    driverfname = driverdetails_jsonobj.getString("firstname");
                                    driverlname = driverdetails_jsonobj.getString("lastname");
                                    prof_pic = driverdetails_jsonobj.getString("prof_pic");
                                    email = driverdetails_jsonobj.getString("email");
                                    mobile = driverdetails_jsonobj.getString("mobile_no");
                                    star = driverdetails_jsonobj.getString("star");
                                    carname = driverdetails_jsonobj.getString("carname");


                                    drivermname.setText(driverfname + "" + driverlname);
                                    drivercarname.setText(carname);

                                    double rate = new Double(star);
                                    DecimalFormat twoDecimals = new DecimalFormat("#,#");
                                    rate = Double.valueOf(twoDecimals.format(rate));
                                    star = String.valueOf(rate);
                                    driverrate.setText(star);


                                    URL driverprofileurl = new URL(prof_pic);
                                    Bitmap driverprofilebitmap = BitmapFactory.decodeStream(driverprofileurl.openStream());
                                    driverprofile.setScaleType(ScaleType.FIT_XY);
                                    driverprofile.setImageBitmap(driverprofilebitmap);


                                } else {

                                    System.out.println("User details not updated");

                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
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

    public void droplocation() {

        //http://54.164.64.111/arcanemobile/drivers/display_personal_details?user_id=55b27aac230d76a52ccba3c3
        final String url = Liveurl + "update_droploc?id=" + User_id + "&droplat=" + droplat + "&droplong=" + droplong;
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

                                    System.out.println("User details updated");
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

    private void ridesharedialog() throws IOException {

        Button Accept, Decline;
        TextView dname;
        ImageView dprofile;

        final Dialog d = new Dialog(SlideMainActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.split_accept_dialog);
        d.setCancelable(false);
        Decline = (Button) d.findViewById(R.id.ddecline);
        Accept = (Button) d.findViewById(R.id.daccept);
        dname = (TextView) d.findViewById(R.id.dname);
        dprofile = (ImageView) findViewById(R.id.dprofile);

        dname.setText(ridesharename);

        if (rideshareprofile.equals("null") || rideshareprofile.equals("")) {

        } else {

        }




        d.show();
        Decline.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                shareacceptstatus = "no";
                ridershareacceptstatus();
                d.dismiss();
            }
        });
        Accept.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                shareacceptstatus = "yes";
                close = false;
                whilecheck = true;
                markerText.setVisibility(View.GONE);

                System.out.println("ACCEPT YES LAYOUT GONE ");
                layout.setVisibility(View.INVISIBLE);
                load.setVisibility(View.GONE);
                load.setAnimation(null);
                driverdetails.setVisibility(View.VISIBLE);
                rideshare.setVisibility(View.INVISIBLE);
                ;


                acceptdriverid = ridesharedriverid;
                accept = "yes";
                System.out.println("1Accept rider share accpet"+accept);
                ridershareacceptstatus();
                driverdisplaydetails();

                originlat = Double.toString(gpslocation.getLatitude());
                originlot = Double.toString(gpslocation.getLongitude());
                if (net_connection_check())
                    autogetsingledriver();

                markerText.setVisibility(View.GONE);

                System.out.println("ACCEPT YES LAYOUT GONE ");
                layout.setVisibility(View.INVISIBLE);
                load.setVisibility(View.GONE);
                load.setAnimation(null);
                d.dismiss();

            }
        });

    }


    public void ridershareacceptstatus() {

        //http://54.164.64.111/arcanemobile/drivers/display_personal_details?user_id=55b27aac230d76a52ccba3c3
        final String url = Liveurl + "split_accept?userid=" + User_id + "&accept=" + shareacceptstatus;
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

                                    System.out.println("User details updated");
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

    public void getridesharedetails() {

        final String url = Liveurl + "get_split_invite?userid=" + User_id;
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



                                    rideshareid = driverdetails_jsonobj.getString("split_id");

                                    if (rideshareid != null || !rideshareid.equals("null")) {
                                        ridesharename = driverdetails_jsonobj.getString("name");
                                        rideshareprofile = driverdetails_jsonobj.getString("prof_pic");
                                        ridesharedriverid = driverdetails_jsonobj.getString("driverid");
                                        try {
                                            ridesharedialog();
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }


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



    public void getridersharestatus() {


        //http://54.164.64.111/arcanemobile/drivers/display_personal_details?user_id=55b27aac230d76a52ccba3c3
        final String url = Liveurl + "invite_status?userid=" + User_id;
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



                                    acceptednumber = driverdetails_jsonobj.getString("accepted_number");

                                    System.out.println("WEB Selected NAme" + acceptednumber);
                                    System.out.println("WEB Selected NAme" + selectedname);
                                    System.out.println("WEB Selected NAme" + selectedphone);
                                    System.out.println("WEB Selected NAme" + selectedprofile);


                                    accsplitingpro.setVisibility(View.VISIBLE);
                                    accspliting.setText(notspliting.getText().toString());
                                    accsplitingpro.setScaleType(ScaleType.FIT_XY);
                                    if (selectedprofile != null) {
                                        accsplitingpro.setImageBitmap(selectedprofile);
                                    }


                                    notspliting.setVisibility(View.INVISIBLE);
                                    notsplitingpro.setVisibility(View.INVISIBLE);
	    						     	

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


    public void getdroplocation() {


        try {


            driverdetails_Url = new URL(Liveurl1 + "get_droploc?userid=" + rideshareid);
            System.out.println("Driver Name URL" + driverdetails_Url);

            BufferedReader login_reader;
            String str_login = "";

            login_reader = new BufferedReader(new InputStreamReader(driverdetails_Url.openStream()));

            System.out.println("Driver Name Reader" + login_reader);

            String s = driverdetails_Url.toString();
            System.out.println("Driver Name String" + s);
            while ((driverdetails_inputline = login_reader.readLine()) != null) {

                str_login += driverdetails_inputline;
                System.out.println("Driver Name Input Line" + str_login);


            }

            System.out.print("Driver Name Outsite While" + str_login);

            driverdetails_jsonarray = new JSONArray(str_login);


            for (int i = 0; i < driverdetails_jsonarray.length(); i++) {


                driverdetails_jsonobj = driverdetails_jsonarray.getJSONObject(i);
                driverdetailsjson_status = driverdetails_jsonobj.getString("status");

                if (driverdetailsjson_status.matches("Success")) {
                    System.out.println("Begin Notification send ");


                    droplat = driverdetails_jsonobj.getString("droplat");
                    droplong = driverdetails_jsonobj.getString("droplong");

                    droplatd = Double.parseDouble(droplat);
                    droplongd = Double.parseDouble(droplong);


                    destination = new LatLng(droplatd, droplongd);


                    System.out.println("After executer downladTask");
                } else {
                    System.out.println("Data is not cleared please try again");


                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public void calltoadmin() {


        try {


            driverdetails_Url = new URL(Liveurl + "send_number?userid=123");
            System.out.println("Driver Name URL" + driverdetails_Url);

            BufferedReader login_reader;
            String str_login = "";

            login_reader = new BufferedReader(new InputStreamReader(driverdetails_Url.openStream()));

            System.out.println("Driver Name Reader" + login_reader);

            String s = driverdetails_Url.toString();
            System.out.println("Driver Name String" + s);
            while ((driverdetails_inputline = login_reader.readLine()) != null) {

                str_login += driverdetails_inputline;
                System.out.println("Driver Name Input Line" + str_login);


            }

            System.out.print("Driver Name Outsite While" + str_login);

            driverdetails_jsonarray = new JSONArray(str_login);



            for (int i = 0; i < driverdetails_jsonarray.length(); i++) {


                driverdetails_jsonobj = driverdetails_jsonarray.getJSONObject(i);
                driverdetailsjson_status = driverdetails_jsonobj.getString("status");

                if (driverdetailsjson_status.matches("Success")) {
                    admin_number = driverdetails_jsonobj.getString("contact");


                    Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:("+admin_number+")"));

                    try {
                        startActivity(call);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println("Data is not cleared please try again");

                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public void getcategory() {

        final ProgressDialog dialog = new ProgressDialog(SlideMainActivity.this);
        dialog.setMessage("Please wait....");
        dialog.setTitle("");
        dialog.show();
        dialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Urls.URL_USER_GET_CATEGORY ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        dialog.dismiss();
                        Toast.makeText(SlideMainActivity.this,"Int "+response, Toast.LENGTH_LONG).show();
                        if (response != null) {

                            ArrayList<Catagories>catagory=parseJson.getCatgory(response);
                         /*  int i=0;
                            for (Catagories catagories:catagory) {
                                i++;
                              *//*  driverdetails_jsonobj = driverdetails_jsonarray.getJSONObject(i);
                                driverdetailsjson_status = driverdetails_jsonobj.getString("car");
                                Log.d("OUTPUT IS", driverdetailsjson_status);*//*
                                carcategory[i] = catagories.getCat_name();
                                System.out.println("CATEGORY" + carcategory[i]);
                            }

                            System.out.println("CATEGORY OF 00000000000000000000" + carcategory[0]);
                            System.out.println("CATEGORY OF 00000000000000000000" + carcategory[1]);
                            System.out.println("CATEGORY OF 00000000000000000000" + carcategory[2]);
                            categoryitem1.setText(carcategory[0]);
                            categoryitem2.setText(carcategory[1]);
                            categoryitem3.setText(carcategory[2]);*/

                        } else {
                            //   Util.showdialog(getActivity(), "No Network !", "Internet Connection Failed");
                            Toast.makeText(SlideMainActivity.this,"Internet Connection Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if(error instanceof NoConnectionError)
                            Toast.makeText(SlideMainActivity.this, "No internet available", Toast.LENGTH_SHORT).show();
                        else if(error instanceof ServerError){

                            String d= new  String(error.networkResponse.data);
                            try {
                                JSONObject jso= new JSONObject(d);
                                Toast.makeText(SlideMainActivity.this, jso.getString("message"),Toast.LENGTH_LONG).show();
                                // signUpFacebook();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.Keys.API_KEY,controller.pref.getUserApiKey());


                return params;
            }


        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SlideMainActivity.this);
        requestQueue.add(stringRequest);





       /* try {

            categorynetcheck = false;
            driverdetails_Url = new URL(Liveurl1 + "getcarcategory?userid=" + User_id);
            System.out.println("Driver Name URL" + driverdetails_Url);
            BufferedReader login_reader;
            String str_login = "";

            login_reader = new BufferedReader(new InputStreamReader(driverdetails_Url.openStream()));

            System.out.println("Driver Name Reader" + login_reader);

            String s = driverdetails_Url.toString();
            System.out.println("Driver Name String" + s);
            while ((driverdetails_inputline = login_reader.readLine()) != null) {

                str_login += driverdetails_inputline;
                System.out.println("Driver Name Input Line" + str_login);


            }

            System.out.print("Driver Name Outsite While" + str_login);

            driverdetails_jsonarray = new JSONArray(str_login);
            carcategory = new String[driverdetails_jsonarray.length()];

            System.out.println("LENgTH OF CAR Category is sssssssssssssssssssssssss" + driverdetails_jsonarray.length());
            for (int i = 0; i < driverdetails_jsonarray.length(); i++) {
                driverdetails_jsonobj = driverdetails_jsonarray.getJSONObject(i);
                driverdetailsjson_status = driverdetails_jsonobj.getString("car");
                Log.d("OUTPUT IS", driverdetailsjson_status);
                carcategory[i] = driverdetailsjson_status;
                System.out.println("CATEGORY" + carcategory[i]);
            }

            System.out.println("CATEGORY OF 00000000000000000000" + carcategory[0]);
            System.out.println("CATEGORY OF 00000000000000000000" + carcategory[1]);
            System.out.println("CATEGORY OF 00000000000000000000" + carcategory[2]);
            categoryitem1.setText(carcategory[0]);
            categoryitem2.setText(carcategory[1]);
            categoryitem3.setText(carcategory[2]);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }*/
    }


    public void thread() {
        (new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Insite the Thread While Check" + whilecheck);

                while (close) {
                    System.out.println("Insite the while While Check" + whilecheck);



                    try {

                        Thread.sleep(10000);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                System.out.println("INSITE THE RUN");
                                if (net_connection_check()) {

                                    getAddressFromLocation(gpslocation.getLatitude(), gpslocation.getLongitude());
                                }
                            }
                        });
                    } catch (InterruptedException e) {

                    }
                }


            }
        })).start();
    }

    public void display_ad()

    {

        final String url = Liveurl2 + "advertisement/disp_advertisement?city=" + city;
        System.out.println("advertisement URL is" + url);



        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                advertisement_jsonobj = response.getJSONObject(i);
                                status = advertisement_jsonobj.getString("status");
                                if (status.matches("success")) {
                                    ad.setVisibility(View.VISIBLE);
                                    driverdetails.setVisibility(View.GONE);
                                    layout.setVisibility(View.GONE);
                                    get_image = advertisement_jsonobj.getString("imgurl");
                                    get_tagline = advertisement_jsonobj.getString("tagline");
                                    get_url = advertisement_jsonobj.getString("targeturl");
                                    get_title = advertisement_jsonobj.getString("title");
                                    System.out.println("get image for advertisement" + get_image);
                                    System.out.println("get tagline for advertisement" + get_tagline);
                                    System.out.println("get tagline for advertisement" + get_url);
                                    System.out.println("get title for advertisement" + get_title);

                                    if (get_image != null) {
                                        adimg = new URL(get_image);
                                        bitmap = BitmapFactory.decodeStream(adimg.openStream());
                                        ad_img.setImageBitmap(bitmap);
                                    }
                                    ad_tagline.setText(get_tagline);
                                    ad_title.setText(get_title);
                                    ad_close.setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            ad.setVisibility(View.GONE);
                                            layout.setVisibility(View.VISIBLE);
                                            whilecheck = false;
                                        }
                                    });
                                    ad.setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(get_url));
                                            startActivity(i);
                                        }
                                    });

                                } else {
                                    layout.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof NoConnectionError) {

                    dialogcalling();
                }

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    public void getAddressFromLocation(double lat, double lon) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(SlideMainActivity.this);

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            city = addresses.get(0).getLocality();

            display_ad();
            System.out.println("get city name" + city);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Here 1 represent max location result to returned, by documents it recommended 1 to 5

    }


    //Toll Fee Webservice
    public void get_tollfee() {

        System.out.println("Inside get token");
        //final String url=Liveurl+"last_tollfee?driverid="+driverdetailsuserid+"userid="+User_id;
        final String url = Liveurl + "last_tollfee?userid=" + User_id;
        System.out.println("URL is" + url);

// Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

// Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                tollfee_jsonobj = response.getJSONObject(i);
                                tollfee_status = tollfee_jsonobj.getString("status");
                                System.out.println("geting client token from json:" + tollfee_status);
                                if (tollfee_status.matches("success")) {
                                    tollfeeamt = tollfee_jsonobj.getString("amount");

                                    System.out.println("notification msg is: " + tollfeeamt);

                                    String title = "Toll Fees";
                                    String message = "You has been charged for Toll Fee:$ " + tollfeeamt;
                                    System.out.println("You has been charged for Toll Fee:$ " + tollfeeamt);
                                    dialogshow(title, message);

                                } else {
                                    System.out.println("");
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
                if (error instanceof NoConnectionError) {
                    System.out.println("No internet connection");

                    dialogcalling();
                }
            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
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
                acceptalart();
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


    public String getAcceptDriverDetails() {


        try {




            driverdetails_Url = new URL(Liveurl + "get_driver_details?user_id=" + User_id);
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
                driverdetailsjson_status = driverdetails_jsonobj.getString("status");
                if(driverdetailsjson_status=="Success" || driverdetailsjson_status.equals("Success")) {

                    acceptdriverid = driverdetails_jsonobj.getString("driver_id");
                    destaddress = driverdetails_jsonobj.getString("drop");
                    pickupaddress = driverdetails_jsonobj.getString("pickup");

                    pickupadd = getLocationFromAddress(pickupaddress);

                    System.out.println("PickupAddresslatlng" + pickupadd);
                    System.out.println("DestnationAddresslatlng" + destination);
                }
                else
                {

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
        System.out.println("Accept status In Accept Method" + acceptstatus);
        return acceptstatus;
    }
    public void acceptalart() {
        System.out.println("Insite the accept alart");
        android.support.v7.app.AlertDialog.Builder builder =
				new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
       builder.setCancelable(false);
       builder.setTitle("Your request confirmed");


       builder.setMessage("Arcane Driver : " + acceptdriverfullname + getString(R.string.reachyousoon));

       builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {


           @Override
           public void onClick(DialogInterface dialog, int which) {

               System.out.println("******************Dest address ************************" + destaddress);

               if (destaddress == null || destaddress.equals(null)) {
                   System.out.println("****************** ELSE Dest address ************************" + destaddress);
                   getAcceptDriverDetails();
                   String acc = "yes";
                   System.out.println("Accept status Insite Run Method If" + acc);

                   close1 = "false";
                   Intent slide = new Intent(getApplicationContext(), SlideMainActivity.class);
                   slide.putExtra("userid", User_id);
                   slide.putExtra("close", close1);
                   slide.putExtra("fbuserproimg", fbuserproimg);
                   System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
                   slide.putExtra("whologin", WhoLogin);
                   slide.putExtra("password", checkpassword);
                   System.out.println("Slide Main Activity WhoLogin" + WhoLogin);

                   slide.putExtra("accept", acc);


                   String lat = new Double(pickupadd.latitude).toString();
                   String lng = new Double(pickupadd.longitude).toString();
                   slide.putExtra("lat", lat);
                   slide.putExtra("lot", lng);

                   slide.putExtra("destaddress", destaddress);
                   slide.putExtra("acceptdriverid", acceptdriverid);
                   System.out.println("Accept Status in Request autorun" + acc);
                   startActivity(slide);
                   finish();
               } else {
                   System.out.println("****************** IF  Dest address ************************" + destaddress);
                   String acc = "yes";
                   System.out.println("Accept status Insite Run Method If" + acc);

                   close1 = "false";
                   Intent slide = new Intent(getApplicationContext(), SlideMainActivity.class);
                   slide.putExtra("userid", User_id);
                   slide.putExtra("close", close1);
                   slide.putExtra("fbuserproimg", fbuserproimg);
                   System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
                   slide.putExtra("whologin", WhoLogin);
                   slide.putExtra("password", checkpassword);
                   System.out.println("Slide Main Activity WhoLogin" + WhoLogin);

                   slide.putExtra("accept", acc);
                   String lat = null, lng = null;
                   if (pickupadd != null || !pickupadd.equals(null)) {
                       lat = new Double(pickupadd.latitude).toString();
                       lng = new Double(pickupadd.longitude).toString();
                   }
                   slide.putExtra("lat", lat);
                   slide.putExtra("lot", lng);

                   slide.putExtra("destaddress", destaddress);
                   slide.putExtra("acceptdriverid", acceptdriverid);
                   System.out.println("Accept Status in Request autorun" + acc);
                   startActivity(slide);
                   finish();
               }

           }
       });
        try {
            builder.show();
        } catch(Exception e){
            // WindowManager$BadTokenException will be caught and the app would not display
            // the 'Force Close' message
        }

    }

    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {
            dialogcalling();
            categorynetcheck = true;
        }
        return connection;
    }

    public void dialogcalling() {
        final Dialog dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar);

        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);

        window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

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





	private void loadSaved() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();

	}

	public void Clearsplitid() {



		final String url=Liveurl+"clear_split_mode?userid="+User_id;
		System.out.println("URL is" + url);
		// Creating volley request obj
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {


						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {

								remove_jsonobj = response.getJSONObject(i);
								remove_status= remove_jsonobj.getString("status");
								Log.d("OUTPUT IS",remove_status);
								if(remove_status.matches("sucess")){



									System.out.println("Amount data will be cleared");
								}

								else  {

									System.out.println("Amount data not cleared");
								}						} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				if(error instanceof NoConnectionError) {
					System.out.println("No internet connection");
					Toast toast = Toast.makeText(getApplicationContext(), "There is no network please connect.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 70);
					toast.show();
				}
				VolleyLog.d(TAG, "Error: " + error.getMessage());


			}
		});

// Adding request to request queue
		AppController.getInstance().addToRequestQueue(movieReq);


	}




	public void Removedetails() {


		final String url=Liveurl+"clear_amount?userid="+User_id;
		System.out.println("URL is" + url);
		// Creating volley request obj
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {


						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {

								remove_jsonobj = response.getJSONObject(i);
								remove_status= remove_jsonobj.getString("status");
								Log.d("OUTPUT IS",remove_status);

								if(remove_status.matches("Sucess")){

									System.out.println("Details Removed");

								}

								else  {

									System.out.println("Missing fields");


								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				if(error instanceof NoConnectionError) {
					System.out.println("No internet connection");
					Toast toast = Toast.makeText(getApplicationContext(), "There is no network please connect.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 70);
					toast.show();
				}
				VolleyLog.d(TAG, "Error: " + error.getMessage());


			}
		});

// Adding request to request queue


		AppController.getInstance().addToRequestQueue(movieReq);

	}


	public void offriderdetails() {


		final String url=Liveurl+"availability_off?userid="+User_id+"&status=off";
		System.out.println("URL is"+url);
		// Creating volley request obj
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {


						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {

								remove_jsonobj = response.getJSONObject(i);
								remove_status= remove_jsonobj.getString("status");
								Log.d("OUTPUT IS",remove_status);

								if(remove_status.matches("Sucess")){

									System.out.println("Details Removed");

								}

								else  {

									System.out.println("Missing fields");


								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				if(error instanceof NoConnectionError) {
					System.out.println("No internet connection");
					Toast toast = Toast.makeText(getApplicationContext(), "There is no network please connect.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 70);
					toast.show();
				}
				VolleyLog.d(TAG, "Error: " + error.getMessage());


			}
		});

// Adding request to request queue


		AppController.getInstance().addToRequestQueue(movieReq);

	}

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
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle bndlanimation =ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
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

    public void riderprofiledetails()
    {

        try {

            driverdetails_Url = new URL(Liveurl+"display_details?user_id="+User_id);
            System.out.println("Driver Name URL" + driverdetails_Url);
            BufferedReader login_reader;
            String str_login = "";

            login_reader = new BufferedReader(new InputStreamReader(driverdetails_Url.openStream()));

            System.out.println("Driver Name Reader" + login_reader);

            String s = driverdetails_Url.toString();
            System.out.println("Driver Name String" + s);
            while ((driverdetails_inputline = login_reader.readLine()) != null) {

                str_login += driverdetails_inputline;
                System.out.println("Driver Name Input Line" + str_login);


            }

            System.out.print("Driver Name Outsite While" + str_login);

            driverdetails_jsonarray = new JSONArray(str_login);



            for (int i = 0; i < driverdetails_jsonarray.length(); i++) {


                profile_jsonobj = driverdetails_jsonarray.getJSONObject(i);

                profile_status = profile_jsonobj.getString("status");
                User_id=  profile_jsonobj.getString("id");
                profile_userfname=profile_jsonobj.getString("firstname");
                profile_userlname=profile_jsonobj.getString("lastname");
                profile_useremail=profile_jsonobj.getString("email");

                profile_usermobile=profile_jsonobj.getString("mobile_no");
                trip_rider_status=profile_jsonobj.getString("trip_rider_status");
                originlat=profile_jsonobj.getString("pickuplat");
                originlot=profile_jsonobj.getString("pickuplong");
                destaddress=profile_jsonobj.getString("destaddress");
                acceptdriverid=  profile_jsonobj.getString("driverid");

                System.out.println("DEST ADDRESS IS "+destaddress);
                System.out.println("TRIP RIDER SATUS Is"+trip_rider_status);
                System.out.println("Accepted Driver ID IS"+acceptdriverid);

                ParseJson parseJson=new ParseJson(SlideMainActivity.this);
                DriverInfo responceLogin = parseJson.ParseLoginResponce(controller.pref.getUserLogin());


                if(!profile_userfname.equals("null"))
                {
                    fname.setText(profile_userfname);
                }else
                {
                    StringTokenizer tokens = new StringTokenizer(profile_useremail, "@");

                    String first_string = tokens.nextToken();
                    String File_Ext = tokens.nextToken();
                    System.out.println("First_string : "+first_string);
                    System.out.println("File_Ext : "+File_Ext);

                    fname.setText(first_string);
                }



                System.out.println("FNAME IS"+profile_userfname);
                System.out.println("LNAME IS"+profile_userlname);

                String image=profile_jsonobj.getString("prof_pic");
                System.out.println("Profile Image"+image);
                if(profile_jsonobj.getString("prof_pic").equals("null"))
                {
                    profile_userprofilepic=fbuserproimg;
                }
                else
                {
                    profile_userprofilepic=profile_jsonobj.getString("prof_pic");
                }

                System.out.print("Insite the Profile Activity Profile image"+profile_userprofilepic);
                profile_userpassword=profile_jsonobj.getString("password");

                Log.d("OUTPUT IS",profile_status);
                if(profile_status.matches("Success")){


                    if(profile_jsonobj.getString("prof_pic").equals("null")&&fbuserproimg==null)
                    {
                    }
                    else {
                        proimg = new URL(profile_userprofilepic);
                        bitmap = BitmapFactory.decodeStream(proimg.openStream());
                        profileimg.setScaleType(ScaleType.FIT_XY);
                        profileimg.setImageBitmap(bitmap);
                    }

                    System.out.println("First NAME IS"+profile_userfname);
                    System.out.println("LAst NAME IS" + profile_userlname);


                    fname.setEnabled(false);

                    profileimg.setClickable(false);




                    if(trip_rider_status.equals("Home Page"))
                    {

                    }
                    else if(trip_rider_status.equals("Request"))
                    {

                    }else if(trip_rider_status.equals("Accept yes"))
                    {

                        accept="yes";
                        accept1 = accept;
                        System.out.println("1Accept D AS"+accept);


                    }
                    else if (trip_rider_status.equals("Arrive")) {
                        if (net_connection_check()) {
                            accept="yes";
                            accept1=accept;
                            System.out.println("1Accept D A"+accept);
                            getamount();
                        }
                    } else if (trip_rider_status.equals("Begin Trip")) {


                        close = false;
                        begintripauto=false;
                       accept="nos";
                        System.out.println("1Accept D BT"+accept);
                        markerText.setVisibility(View.GONE);


                        System.out.println("ACCEPT YES LAYOUT GONE ");
                        layout.setVisibility(View.INVISIBLE);
                        load.setVisibility(View.GONE);
                        load.setAnimation(null);


                        String title = getString(R.string.begin_trip);
                        String message = getString(R.string.begin_now);

                        newMessage1 = "begin";
                        System.out.println("DRIVER CANCEL REQUEST LAYOUT VISIBLE");

                        dialogshow(title, message);
                        if (net_connection_check()) {
                            autogetsingledriver();
                            driverdisplaydetails();
                        }

                        if (secondrider) {
                            if (net_connection_check())
                                getdroplocation();
                        }
                        close=false;
                    } else if (trip_rider_status.equals("End Trip")) {
                        if (secondrider) {
                            if (net_connection_check()) {
                                getamount1();
                            }
                        } else {
                            if (net_connection_check()) {
                                getamount();
                            }
                        }
                    }


                }

                else if(profile_status.matches("Invalid")) {
                    Toast.makeText(getApplicationContext(), "Invalid User ID", Toast.LENGTH_LONG).show();

                }

            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    public void loadsavedpreferences1()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("googleapikey", googleapikey);
        editor.putString("googleprojectid", googleprojectid);
        editor.putString("paypalclienttoken", paypalclienttoken);
        editor.putString("paypaltype", paypaltype);
        // editor.putString("userid",shUser_id);

        editor.commit();
        String restoredText = sharedPreferences.getString("liveurl", null);
        System.out.println("Liveurl value--->" + restoredText);

    }


    public void getkeys() {
        final String url = Liveurl + "display_connect?category=100";
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

                                    googleapikey = driverdetails_jsonobj.getString("google_api_key");
                                    googleprojectid = driverdetails_jsonobj.getString("google_project_id");

                                    paypalclienttoken = driverdetails_jsonobj.getString("paypalclienttoken");
                                    paypaltype = driverdetails_jsonobj.getString("paymenttype");


                                    loadsavedpreferences1();


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
    public void fare_estimation() {

        //Toast.makeText(SlideMainActivity.this, "here....", Toast.LENGTH_SHORT).show();
        //http://localhost/Praveen_R/arrow/users/etacalc?driverid=571e183b230d76ed2c7798b2&area1=&area2=20Meenambakkam,%20Chennai,%20Tamil%20Nadu
        pickupaddress=pickupaddress.replaceAll(" ","%20");
        location=location.replaceAll(" ","%20");

        final String url = Liveurl+"fare_estimation?carcategory="+category+"&area1="+pickupaddress+"&area2="+location;
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

                                System.out.println("Get Category details====>" + response);
                                Log.d("OUTPUT IS===>", driverdetailsjson_status);
                                if (driverdetailsjson_status.matches("success")) {

                                    nETA = driverdetails_jsonobj.getString("ETA");

                                    System.out.println("eta is====>"+nETA);
                                    double double_Eta = Double.parseDouble(nETA);
                                    nETA = new DecimalFormat("##.##").format(double_Eta);

                                    minfare.setText("$"+nETA);
                                    minfare.setTextSize(20);
                                    textfare.setText("Estimated Fare");
                                    //getfareestimate.setText(location);


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


    // Fetches data from url passed
    private class get_single_driver extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            singledriver();
            return null;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


        }
    }

}