package com.rider.xenia;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MapHelper.DirectionFinder;
import com.MapHelper.DirectionFinderListener;
import com.MapHelper.Route;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app_controller.AppController;
import com.custom.AnimateFirstDisplayListener;
import com.custom.CustomProgressDialog;
import com.custom.ProperRatingBar;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.grepix.grepixutils.WebService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.GetDriverInfo;
import com.utils.ParseJson;
import com.utils.TripHistory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by devin on 2017-04-04.
 */

public class FragmentRouteNavigation extends FragmentActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback, DirectionFinderListener {

    protected static final String TAG = "MainActivity";

    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    // UI Widgets.

    // Labels.
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    private Controller controller;
    private GoogleMap mMap;
    private ArrayList<Object> originMarkers;
    private ArrayList<Object> polylinePaths;
    private ArrayList<Object> polylinePathsRoute;
    private ArrayList<Object> destinationMarkers;
    private PolylineOptions polylineOptionsyourRoute;
    private boolean isRoute;
    //    private Handler handler;
    private ParseJson parseJson;
    private boolean isTripEnd;
    private boolean isNotHaveLocations=false;
    private TripHistory createdTrip;
    private String notiStatus="no";
    private String waypoints="";
    private LatLng sourceLatLang;
    private LatLng destiLatLang;
    private LatLng lastLatLant;
    private boolean isZoom=true;
    private RelativeLayout rl_driver_notification;
    private TextView driver_name, car_no, car_name;
    private CircularImageView driver_profile;
    private ImageView driver_call;

    private String phone_no, phone;
    private DisplayImageOptions options;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private final ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Context context;
    private static CardView cardLayout;
    private ProperRatingBar driverRate;
    private CustomProgressDialog dialog;
    private Marker driverMarker;
    private Button cancel_trip_button;
    private TextView detaildrop,detailpickup,trip_price,trip_diatance,trip_time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.navigation_activity);
//        handler = new Handler();
        dialog=new CustomProgressDialog(FragmentRouteNavigation.this);
        parseJson = new ParseJson(this);
        // Locate the UI widgets.

        controller = (Controller) getApplication();
        polylinePathsRoute = new ArrayList<>();
        LocalBroadcastManager.getInstance(FragmentRouteNavigation.this).registerReceiver(mNotificationReceiver, new IntentFilter("some_custom_id"));
     /*  polylineOptionsyourRoute = new PolylineOptions().
                geodesic(true).
                color(getResources().getColor(R.color.colorAccent)).
                width(10);*/

        if(controller.isAccepted()){
            controller.setAccepted(false);
        }
        String tripStatus=controller.pref.getTripStatus();
        try{
            createdTrip = parseJson.getTripDetails(controller.pref.getTripResponce());
            ((TextView) findViewById(R.id.tvAddress)).setText(createdTrip.getTripToLoc());
            sourceLatLang = new LatLng(Double.parseDouble(createdTrip.getTripScheduledPickLat()), Double.parseDouble(createdTrip.getTripScheduledPickLng()));
            destiLatLang = new LatLng(Double.parseDouble(createdTrip.getTripScheduledDropLat()), Double.parseDouble(createdTrip.getTripScheduledDropLng()));
            lastLat=Double.parseDouble(createdTrip.getTripScheduledPickLat());
            lastlng=Double.parseDouble(createdTrip.getTripScheduledPickLng());
        }catch (Exception e){

        }

        if(tripStatus.equalsIgnoreCase("begin")){
            isRoute=true;


            try{

                //rogressDialog.show();

                new DirectionFinder(this,createdTrip.getTripScheduledPickLat()+","+createdTrip.getTripScheduledPickLng(),createdTrip.getTripScheduledDropLat()+","+createdTrip.getTripScheduledDropLng(),waypoints).execute();
                //new DirectionFinder(this, "Kalka ji","Laxminagar").execute();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }else if(tripStatus.equalsIgnoreCase("end")){
            handleOnEndTrip();
        }else{
            isRoute=false;
            try{

                controller.setDriverId(createdTrip.getDriver().getDriver_id());

                //rogressDialog.show();
                // sourceLat=new LatLng(Double.parseDouble(createdTrip.getTripScheduledPickLat()),Double.parseDouble(createdTrip.getTripScheduledPickLng()));
                new DirectionFinder(FragmentRouteNavigation.this,createdTrip.getTripScheduledPickLat()+","+createdTrip.getTripScheduledPickLng(),createdTrip.getDriver().getULat()+","+createdTrip.getDriver().getULng(),waypoints).execute();
                //new DirectionFinder(this, "Kalka ji","Laxminagar").execute();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }




        //show driver details
      //  cardLayout = (CardView) findViewById(R.id.card_pop_up);
      //  cardLayout.setVisibility(View.GONE);
        rl_driver_notification = (RelativeLayout) findViewById(R.id.rl_driver_details);
        driver_name = (TextView) findViewById(R.id.tv_driver_name);
        car_no = (TextView) findViewById(R.id.tv_car_number);
        car_name = (TextView) findViewById(R.id.tv_car_model);
        driverRate = (ProperRatingBar) findViewById(R.id.rb_review);
        cancel_trip_button = (Button) findViewById(R.id.cancel_trip_button);

        detaildrop = (TextView) findViewById(R.id.detaildrop);
        detailpickup = (TextView) findViewById(R.id.detailpickup);
        trip_price = (TextView) findViewById(R.id.trip_price);
        trip_diatance = (TextView) findViewById(R.id.trip_diatance);
        trip_time = (TextView) findViewById(R.id.trip_time);



        driver_call = (ImageView) findViewById(R.id.iv_driver_call_number);
        driver_profile = (CircularImageView) findViewById(R.id.iv_driver_profile);

        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(FragmentRouteNavigation.this));
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.circleuser).showImageForEmptyUri(R.drawable.circleuser).showImageOnFail(R.drawable.circleuser).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





        // Set labels.
      /*  mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);*/

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        handleProfileUpdate();
        manageLeftslider();

    }


    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout navrl;
    private CircularImageView1 profileImage;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private NavDrawerListAdapter adapter;
    ImageLoader imageLoader2 = AppController.getInstance().getImageLoader();

    private void manageLeftslider() {
        ImageView imgbut = (ImageView) findViewById(R.id.drawicon);
        mTitle = mDrawerTitle = getTitle();
        String[] navMenuTitles = getResources().getStringArray(R.array.profile_list);
        // nav drawer icons from resources
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        profileImage = (CircularImageView1) findViewById(R.id.side_m_profile_mage);
        ParseJson parseJson = new ParseJson(this);
        DriverInfo userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        if(userInfo.getUProfileImagePath()==null||userInfo.getUProfileImagePath().equalsIgnoreCase("null")||userInfo.getUProfileImagePath().equalsIgnoreCase(""))
        {
            profileImage.setBackgroundResource(R.drawable.circleduserwhite);
        }else{
            profileImage.setImageUrl(Constants.Urls.IMAGE_BASE_URL + userInfo.getUProfileImagePath(), imageLoader2);
            profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }



        TextView ridername = (TextView) findViewById(R.id.ridername);
        ridername.setText("" + userInfo.getUFname() + " " + userInfo.getULname());

        navrl = (RelativeLayout) findViewById(R.id.rv);
        final ListView mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        /*navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));*/
        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        //      getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //   getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons

            }
        };

        imgbut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDrawerLayout.openDrawer(navrl);
                mDrawerList.setVisibility(View.VISIBLE);
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    /**
     * Slide menu item click listener
     *
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
                Intent profile = new Intent(getApplicationContext(), EditProfileActivity.class);
                profile.putExtra("userid", controller.pref.getUserID());
                profile.putExtra("fbuserproimg", "");
                profile.putExtra("whologin", "");
                profile.putExtra("password", "");
                profile.putExtra("accept", "");

                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(profile, bndlanimation);

                break;
            case 1:
                Intent pay = new Intent(getApplicationContext(), SosActivity.class);

                pay.putExtra("userid", controller.pref.getUserID());
                pay.putExtra("fbuserproimg", "");
                pay.putExtra("whologin", "");
                pay.putExtra("password", "");
                pay.putExtra("accept", "");
                startActivity(pay);

                break;

            case 2:


                Intent history = new Intent(getApplicationContext(), Myreview.class);

                history.putExtra("userid", controller.pref.getUserID());
                history.putExtra("fbuserproimg", "");

                history.putExtra("whologin", "");
                history.putExtra("password", "");

                startActivity(history);
                break;


            case 3:
                mDrawerLayout.closeDrawer(navrl);
                android.support.v7.app.AlertDialog.Builder builder =
                        new android.support.v7.app.AlertDialog.Builder(FragmentRouteNavigation.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.exit);
                builder.setMessage(R.string.do_you_want_exit_now);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        handleLogOut();

                    }
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


            case 4:
                mDrawerLayout.closeDrawer(navrl);
                Intent contact = new Intent(getApplicationContext(),ContactUsActivity.class);
                startActivity(contact);
                break;

            case 5:
                mDrawerLayout.closeDrawer(navrl);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.Urls.URL_FOR_TERM_OF_USE));
                startActivity(browserIntent);
            case 6:

            default:
                break;
        }
        mDrawerLayout.closeDrawer(navrl);

    }


    private void handleLogOut() {
        dialog.showDialog();
        DeviceTokenService.logout(controller, new DeviceTokenService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                dialog.dismiss();
                controller.pref.saveIsLoginSucess(false);
                Intent i = new Intent(getApplicationContext(), SigninActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(i, bndlanimation);
                finish();
            }
        });
    }




    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //  getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getTripByID();

        }
    };


    private Timer timer;

    public void handleProfileUpdate() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                FragmentRouteNavigation.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getTripByID();
                    }
                });
            }
        }, 0, 10000);//put here time 1000 milliseconds=1 second
    }

    public void getTripByID() {
        if (isTripEnd) {

            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.TRIP_ID, createdTrip.getTripId());
        params.put(Constants.Keys.USER_ID, controller.pref.getUserID());
        System.out.println("GetTripByID : " + params);
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_GET_TRIP, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {

                    final TripHistory tripHistory = parseJson.parseSingleTrip(data.toString());
                    //Set Driver Data

                    if(!isRoute){
                        try{
                            cancel_trip_button.setVisibility(View.VISIBLE);
                         //   cardLayout.setVisibility(View.VISIBLE);
                            tripHistory.getDriver().getUFname();
                            tripHistory.getDriver().getULname();
                            driver_name.setText(tripHistory.getDriver().getUFname() + " " + tripHistory.getDriver().getULname());
                            // tripHistory.getDriver().getCar_reg_no();
                            car_no.setText(tripHistory.getDriver().getCar_reg_no());
                            tripHistory.getDriver().getCar_name();
                            car_name.setText(tripHistory.getDriver().getCar_name());
                            driverRate.setRating((int) Float.parseFloat(tripHistory.getDriver().getD_rating()));

                            tripHistory.getDriver().getD_profile_image_path();

                            if(!tripHistory.getDriver().getD_profile_image_path().equalsIgnoreCase("null")){
                                String url = Constants.Urls.IMAGE_BASE_URL_DRIVER + tripHistory.getDriver().getD_profile_image_path();
                                imageLoader.displayImage(url, driver_profile, options, animateFirstListener);
                            }


                            detailpickup.setText(tripHistory.getTripToLoc());

                            detaildrop.setText(tripHistory.getTripFromLoc());
                            try{
                                String estimate=controller.pref.getEstimatedDetails();
                                String[] estarr=estimate.split("\\|");
                                trip_price.setText(estarr[0]);
                                trip_diatance.setText(estarr[1]);
                                trip_time.setText(estarr[2]);
                            }catch (Exception e){

                            }

                            controller.setDriverId(tripHistory.getDriver().getDriver_id());


                            phone_no = tripHistory.getDriver().getUPhone();
                            driver_call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (phone_no.equals(null)) {
                                        Toast.makeText(FragmentRouteNavigation.this, "No number", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                        callIntent.setData(Uri.parse("tel:" + phone_no));
                                        if (callIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(callIntent);
                                        }
                                    }
                                }
                            });

                            cancel_trip_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showCancelAlert("cancel",tripHistory.getTripId(),"",tripHistory);

                                }
                            });
                        }catch (Exception e){

                        }

                    }else {
                     //   cardLayout.setVisibility(View.GONE);
                        cancel_trip_button.setVisibility(View.GONE);
                    }






                    controller.setCurrentTrip(tripHistory);
                    Map<String, String> dataMessage = controller.getRemoteMessageData();
                    if (dataMessage != null) {
                        dataMessage.put(Constants.Keys.TRIP_STATUS, tripHistory.getTripStatus());
                    }



                    try{
                        if(!tripHistory.getTripStatus().equals(null)){
                            controller.pref.setTripStatus(tripHistory.getTripStatus());
                            if(tripHistory.getTripStatus().equalsIgnoreCase("end")||tripHistory.getTripStatus().equalsIgnoreCase("driver_cancel_at_drop")){
                                if(controller.pref.getTripStatus().equalsIgnoreCase("no")||tripHistory.getTripStatus().equalsIgnoreCase("end")||tripHistory.getTripStatus().equalsIgnoreCase("driver_cancel_at_drop")){
                                }else{
                                    controller.pref.setTripStatus(tripHistory.getTripStatus());
                                }
                            }else {
                                controller.pref.setTripStatus(tripHistory.getTripStatus());
                            }
                        }
                    }catch (Exception e){

                    }

                    String notificationMessage = "change message:";
                    if (tripHistory.getTripStatus().equalsIgnoreCase(Constants.TripStatus.ACCEPT)) {
                        notificationMessage = Constants.Message.ACCEPTED;
                    }
                    else if (tripHistory.getTripStatus().equalsIgnoreCase(Constants.TripStatus.DRIVER_CANCEL)) {
                        notificationMessage = Constants.Message.DRIVER_CANCEL;
                    }else if (tripHistory.getTripStatus().equalsIgnoreCase("go")) {
                        notificationMessage = "Your request accept";

                    } else if (tripHistory.getTripStatus().equalsIgnoreCase(Constants.TripStatus.END)) {
//            end
                        notificationMessage = Constants.Message.END;
                    } else if (tripHistory.getTripStatus().equalsIgnoreCase(Constants.TripStatus.ARRIVE)) {
                        notificationMessage = Constants.Message.ARRIVE;

//            arrive
                    } else if (tripHistory.getTripStatus().equalsIgnoreCase(Constants.TripStatus.BEGIN)) {
                        notificationMessage = Constants.Message.BEGIN;
//            begin
                    }
                    final android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                            new android.support.v7.app.AlertDialog.Builder(FragmentRouteNavigation.this, R.style.AppCompatAlertDialogStyle);
                    alertDialogBuilder.setMessage("" + notificationMessage);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setInverseBackgroundForced(false);
                    alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (tripHistory.getTripStatus().equalsIgnoreCase("driver_cancel")) {
                                Intent intent=new Intent(FragmentRouteNavigation.this,MainScreenActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });
                    if(notiStatus.equalsIgnoreCase(tripHistory.getTripStatus())||notiStatus.equalsIgnoreCase("no")){

                    }else{
                        alertDialogBuilder.show();}
                    if (tripHistory.getTripStatus().equals("end")||tripHistory.getTripStatus().equals("driver_cancel_at_drop")) {
                        handleOnEndTrip();

                    }

                    if (tripHistory.getTripStatus().equalsIgnoreCase("driver_cancel_at_pickup")||tripHistory.getTripStatus().equalsIgnoreCase("driver_cancel_at_pickup")) {
                        controller.pref.setTripStatus("no");
                        Intent intent=new Intent(FragmentRouteNavigation.this,MainScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (tripHistory.getTripStatus().equals("begin")) {
                        // handleOnEndTrip();
                        isRoute=true;
                    //    cardLayout.setVisibility(View.GONE);
                        cancel_trip_button.setVisibility(View.GONE);
                        try{

                            //rogressDialog.show();
                            new DirectionFinder(FragmentRouteNavigation.this,tripHistory.getTripScheduledPickLat()+","+tripHistory.getTripScheduledPickLng(),tripHistory.getTripScheduledDropLat()+","+tripHistory.getTripScheduledDropLng(),waypoints).execute();
                            //new DirectionFinder(this, "Kalka ji","Laxminagar").execute();)
                        } catch (Exception e) {
                            e.printStackTrace();

                        }


                    } else {
                        isRoute=false;
                    //    cardLayout.setVisibility(View.VISIBLE);
                        try{
                            getDriverLocation();
                            //rogressDialog.show();
                            new DirectionFinder(FragmentRouteNavigation.this,tripHistory.getDriver().getULat()+","+tripHistory.getDriver().getULng(),tripHistory.getTripScheduledPickLat()+","+tripHistory.getTripScheduledPickLng(),waypoints).execute();
                            //new DirectionFinder(this, "Kalka ji","Laxminagar").execute();)
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                } else {

                }
            }
        });
    }

    private void showCancelAlert(final String cancel, final String tripId, final String s, final TripHistory tripHistory) {

        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(FragmentRouteNavigation.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.exit);
        builder.setMessage(R.string.do_you_want_cancel_trip_now);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                updateTripStatusApi(cancel,tripId,s,tripHistory);

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
    }

    private void updateTripStatusApi(final String trip_status, String tripId, String s, final TripHistory tripHistory) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.USER_ID, controller.pref.getUserID());
        params.put(Constants.Keys.TRIP_ID, tripId);
        params.put(Constants.Keys.TRIP_STATUS, trip_status);
        dialog.showDialog();
        //	final ProgressDialog dialog=showProgress(SlideMainActivity.this);
        WebService.excuteRequest(this,params, Constants.Urls.URL_USER_UPDATE_TRIP, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                dialog.dismiss();
                if (isUpdate) {
                    sendNotification(tripHistory,trip_status);
                    if (timer != null) {
                        timer.cancel();
                    }
                }
            }
        });




    }

    public void sendNotification(final TripHistory message, String trip_status) {

        boolean isAndroid = message.getDriver().getUDeviceType().equalsIgnoreCase("Android");
        Map<String, String> params = new HashMap<String, String>();
        params.put("message", "Rider has cancelled the Trip. Ask him");
        params.put("trip_id", message.getTripId());
        params.put("trip_status",trip_status);
        if (isAndroid) {
            params.put("android", message.getDriver().getUDeviceToken());
        } else {
            params.put("ios", message.getDriver().getUDeviceToken());
        }
        WebService.excuteRequest(this, params, Constants.Urls.URL_DRIVER_NOTIFICATION, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if(isUpdate){
                    controller.pref.setTripStatus("no");
                    Intent intent=new Intent(FragmentRouteNavigation.this,MainScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private void handleOnEndTrip() {
        isTripEnd = true;
        getTripStatus();
    }

    private void getTripStatus(){

        dialog.showDialog();
        createdTrip = parseJson.getTripDetails(controller.pref.getTripResponce());

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.TRIP_ID, createdTrip.getTripId());
        params.put(Constants.Keys.USER_ID, controller.pref.getUserID());
        System.out.println("GetTripByID : " + params);
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_GET_TRIP, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    dialog.dismiss();
                    if (timer != null) {
                        timer.cancel();
                    }
                    ParseJson parseJson=new ParseJson(FragmentRouteNavigation.this);
                    final TripHistory tripHistory = parseJson.parseSingleTrip(data.toString());
                    controller.setCurrentTrip(tripHistory);
                    LocalBroadcastManager.getInstance(FragmentRouteNavigation.this).unregisterReceiver(mNotificationReceiver);
                    Intent intent = new Intent(FragmentRouteNavigation.this, FareActivity.class);
                    //  Intent intent = new Intent(FragmentRouteNavigation.this,ThankyouActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    getTripStatus();
                }
            }
        });
    }

    private void handleOnPick() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        DialogFragment newFragment = NotificationDialog.newInstance(FragmentRouteNavigation.this);
        newFragment.show(ft, "dsads");
    }


    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link LocationSettingsRequest.Builder} to build
     * a {@link LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        updateUI();
                        break;
                }
                break;
        }
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */


    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */
    public void stopUpdatesButtonHandler(View view) {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        stopLocationUpdates();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        LocationServices.SettingsApi.checkLocationSettings(
                mGoogleApiClient,
                mLocationSettingsRequest
        ).setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        if (ActivityCompat.checkSelfPermission(FragmentRouteNavigation.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FragmentRouteNavigation.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, FragmentRouteNavigation.this);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                "location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            status.startResolutionForResult(FragmentRouteNavigation.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings.";
                        Log.e(TAG, errorMessage);
                        Toast.makeText(FragmentRouteNavigation.this, errorMessage, Toast.LENGTH_LONG).show();
                        mRequestingLocationUpdates = false;
                }
                updateUI();
            }
        });

    }

    /**
     * Updates all UI fields.
     */
    private void updateUI() {
        setButtonsEnabledState();
        updateLocationUI();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNotificationReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    int count = 0;
    private int dialogCount = 0;
    protected BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Toast.makeText(BEverywareHomeActivity.this,""+count++,Toast.LENGTH_LONG).show();
            Bundle bundle = getIntent().getExtras();
            //  if(bundle.getString("action_id")
            if (dialogCount == 0) {
                handleOnPick();
            }
        }
    };


    public void getDriverLocation() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Urls.URL_USER_GET_DRIVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //  Toast.makeText(SplashScreenActivity.this,"Int "+response, Toast.LENGTH_LONG).show();
                        if (response != null) {
                            ParseJson parseJson = new ParseJson(FragmentRouteNavigation.this);
                            ArrayList<GetDriverInfo> driverList = parseJson.getDriverInfos(response,true);

                            //   Toast.makeText(MainScreenActivity.this,driverList.size()+"\n"+response,Toast.LENGTH_LONG).show();
                            if (driverList.size() == 0) {

                            } else {
                                GetDriverInfo info = driverList.get(0);
                                try {
                                    //mMap.clear();
                                    if(driverMarker!=null){
                                        driverMarker.remove();
                                    }
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    LatLng latLng = new LatLng(Double.parseDouble(info.getD_lat()), Double.parseDouble(info.getD_lng()));
                                    markerOptions.position(latLng);
                                    markerOptions.title(info.getD_name());
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.carimage));
                                    // mMap.setOnMarkerClickListener(this);
                                    createdTrip = parseJson.getTripDetails(controller.pref.getTripResponce());

//                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                                    driverMarker=mMap.addMarker(markerOptions);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }


                        } else {
                            // Toast.makeText(SplashScreenActivity.this,"No Network "+response, Toast.LENGTH_LONG).show();
                            //   Util.showdialog(getActivity(), "No Network !", "Internet Connection Failed");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NoConnectionError) {
                            //  Toast.makeText(MainScreenActivity.this,"No Network Internet Connection Failed", Toast.LENGTH_LONG).show();


                        } else if (error instanceof ServerError) {

                            String d = new String(error.networkResponse.data);
                            try {


                                JSONObject jso = new JSONObject(d);

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
                params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
                params.put(Constants.Keys.DRIVER_ID, controller.getDriverId());


                return params;
            }


        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(FragmentRouteNavigation.this);
        requestQueue.add(stringRequest);
    }




    public static class NotificationDialog extends DialogFragment
    {

        private static FragmentRouteNavigation fragmentRouteNavigation;

        public static NotificationDialog newInstance(FragmentRouteNavigation fragmentRouteNavigation2) {
            fragmentRouteNavigation = fragmentRouteNavigation2;
            NotificationDialog f = new NotificationDialog();
            return f;
        }


        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }


        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            fragmentRouteNavigation.controller.setPush(false);
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            View v = inflater.inflate(R.layout.dialog_notification, container, false);

//            final String message=controller.getPushMessage();
            Map<String, String> data = fragmentRouteNavigation.controller.getRemoteMessageData();
            final String tripStatus = data.get(Constants.Keys.TRIP_STATUS);
            fragmentRouteNavigation.notiStatus=tripStatus;
            ((TextView) v.findViewById(R.id.noti_dialog_message)).setText(data.get(Constants.Keys.PRICE));
            ((TextView) v.findViewById(R.id.noti_dialog_ok)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tripStatus.equalsIgnoreCase("end")){
                        if(fragmentRouteNavigation.controller.pref.getTripStatus().equalsIgnoreCase("no")||fragmentRouteNavigation.controller.pref.getTripStatus().equalsIgnoreCase("end")||fragmentRouteNavigation.controller.pref.getTripStatus().equalsIgnoreCase("driver_cancel_at_drop")){
                        }else{
                            fragmentRouteNavigation.controller.pref.setTripStatus(tripStatus);
                        }
                    }else {
                        fragmentRouteNavigation.controller.pref.setTripStatus(tripStatus);
                    }

                    if (tripStatus.equalsIgnoreCase("end")||tripStatus.equalsIgnoreCase("driver_cancel_at_drop")) {
                        getDialog().dismiss();
                        fragmentRouteNavigation.handleOnEndTrip();
                    }
                    if (tripStatus.equalsIgnoreCase("begin")) {
                        fragmentRouteNavigation.isRoute = true;
                       // fragmentRouteNavigation.cardLayout.setVisibility(View.GONE);
                        fragmentRouteNavigation.cancel_trip_button.setVisibility(View.GONE);

                        try{

                            //rogressDialog.show();
                            new DirectionFinder(fragmentRouteNavigation, fragmentRouteNavigation.createdTrip.getTripScheduledPickLat()+","+fragmentRouteNavigation.createdTrip.getTripScheduledPickLng(),fragmentRouteNavigation.createdTrip.getTripScheduledDropLat()+","+fragmentRouteNavigation.createdTrip.getTripScheduledDropLng(),fragmentRouteNavigation.waypoints).execute();
                            //new DirectionFinder(this, "Kalka ji","Laxminagar").execute();
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        getDialog().dismiss();
                    }

                    if (tripStatus.equalsIgnoreCase("driver_cancel_at_pickup")) {
                        Intent intent=new Intent(fragmentRouteNavigation,MainScreenActivity.class);
                        fragmentRouteNavigation.startActivity(intent);
                        fragmentRouteNavigation.finish();
                        getDialog().dismiss();
                    }else {
                        getDialog().dismiss();
                    }
                }
            });


            return v;
        }

    }

    /**
     * Disables both buttons when functionality is disabled due to insuffucient location settings.
     * Otherwise ensures that only one button is enabled at any time. The Start Updates button is
     * enabled if the user is not requesting location updates. The Stop Updates button is enabled
     * if the user is requesting location updates.
     */
    private void setButtonsEnabledState() {


    }

    /**
     * Sets the value of the UI fields for the location latitude, longitude and last update time.
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            // waypoints="|"+mCurrentLocation;
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            if (isRoute) {

//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
              /*  polylineOptionsyourRoute.add(latLng);
                polylinePathsRoute.add(mMap.addPolyline(polylineOptionsyourRoute));*/
            } else {
//                getDriverLocation();
            }

            // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
          /*  polylineOptionsyourRoute.add(latLng);
            polylinePathsRoute.add(mMap.addPolyline(polylineOptionsyourRoute));*/


        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
                setButtonsEnabledState();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();


        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            setButtonsEnabledState();
            startLocationUpdates();
        }

        isZoom=true;
        // updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        /*if (timer != null) {
            timer.cancel();
            timer = null;
        }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
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
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateLocationUI();
        }
        if (mRequestingLocationUpdates) {
            Log.i(TAG, "in onConnected(), starting location updates");
            startLocationUpdates();
        }

    }

    /**
     * Callback that fires when the location changes.
     */

    double lastLat;
    double lastlng;
    double calculatedDistance;

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;

        if (mCurrentLocation != null){
            double latitute = mCurrentLocation.getLatitude();
            double longitute = mCurrentLocation.getLongitude();
            if (isRoute){
                if(lastLat==latitute&&lastlng==longitute){
                }else{
                    double coverDistance = distance(lastLat, lastlng, latitute, longitute);
                    if(coverDistance>0.050){
                        calculatedDistance=calculatedDistance+coverDistance;
                        ((TextView)findViewById(R.id.tvDistCal)).setText(calculatedDistance+" "+coverDistance);
                        lastLat=latitute;
                        lastlng=longitute;
                        waypoints=waypoints+"|"+latitute+","+longitute;
                    }
                }
            }


        }

        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationUI();
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lon1);
        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lon2);
        double distance = locationA.distanceTo(locationB);
        double km=distance/1000;
        return (km);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onDirectionFinderStart() {

    }

    /**
     * Interface method
     *
     * @param routes
     */
    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        dialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {

            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            mMap.clear();
            if(isRoute){
                /*originMarkers.add(mMap.addMarker(new MarkerOptions()
                       // .icon(BitmapDescriptorFactory.fromResource(R.drawable.carimage))
                        .title(route.startAddress)
                        .position(route.startLocation)));

                destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(route.endAddress)
                       // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))
                        .position(route.endLocation)));*/
                // isZoom=false;
                mMap.addMarker(new MarkerOptions().position(sourceLatLang).icon(BitmapDescriptorFactory.fromResource(R.drawable.carimage)));
                mMap.addMarker(new MarkerOptions().position(destiLatLang).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker)));

            }else{
                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(route.startAddress)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))
                        .position(route.startLocation)));

                destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(route.endAddress)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))
                        .position(route.endLocation)));
            }



            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //the include method will calculate the min and max bound.







            //    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.start_blue);

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(8).zIndex(4);


            for (int i = 0; i < route.points.size(); i++){
                polylineOptions.add(route.points.get(i));
                builder.include(route.points.get(i));
            }

            try{
                if(isZoom){
                    if(isRoute){
                        isZoom=false;
                    }
                    LatLngBounds bounds = builder.build();
                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;
                    int padding = (int) (width * 0.30); // offset from edges of the map 10% of screen

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                    mMap.animateCamera(cu);
                }

            }catch(Exception e){

            }




            polylinePaths.add(mMap.addPolyline(polylineOptions));

        }
    }



    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // gps = new GPSTracker(MapsActivity.this);

        // check if GPS enabled


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
        mMap.setMyLocationEnabled(true);
        if (mMap != null) {


            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    // TODO Auto-generated method stub

                    //  mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));

                    //load the traffic now
                    //  mMap.setTrafficEnabled(true);
                }
            });

        }

    }
}