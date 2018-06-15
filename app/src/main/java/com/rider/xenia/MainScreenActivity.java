package com.rider.xenia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.MapHelper.DirectionFinder;
import com.MapHelper.DirectionFinderListener;
import com.MapHelper.Route;
import com.MapHelper.VolleyJSONRequest;
import com.MapHelper.adapter.AutoCompleteAdapter;
import com.MapHelper.model.PlacePredictions;
import com.addressHelper.DriverConstants;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.app_controller.AppController;
import com.custom.CustomProgressDialog;
import com.custom.TextureVideoView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.grepix.grepixutils.GrepixUtils;
import com.grepix.grepixutils.WebService;
import com.ride.adapter.Custom_Horizontal_Recycler_Adapter;
import com.utils.Catagories;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.GetDriverInfo;
import com.utils.HelperMethods;
import com.utils.NotifyCarTypeChangeCallback;
import com.utils.ParseJson;
import com.utils.TripHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by devin on 2017-03-27.
 */

public class MainScreenActivity extends FragmentActivity implements OnMapReadyCallback, NotifyCarTypeChangeCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnCameraChangeListener, Response.ErrorListener, Response.Listener<String>, DirectionFinderListener {

    private GoogleMap mMap;

    private double latitude;
    private double longitude;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private static final int MY_PERMISSIONS_REQUEST_LOC = 30;
    private Controller controller;
    private ParseJson parseJson;
    private ListView mAutoCompleteList;
    private EditText etDestination;
    private VolleyJSONRequest request;
    private String GETPLACESHIT = "places_hit";
    Custom_Horizontal_Recycler_Adapter custom_horizontal_recycler_adapter;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private PlacePredictions predictions;
    private Handler handler;
    private String preFilledText;
    NotifyCarTypeChangeCallback notifyCarTypeChangeCallback;
    ProgressBar progressDialog;
    private ArrayList<Object> originMarkers;
    private ArrayList<Object> polylinePaths;
    private ArrayList<Object> destinationMarkers;
    private SlideUpPanelLayout sliderlayout;
    private ArrayList<Catagories> catList = new ArrayList<>();
    private ArrayList<Catagories> catListformApi = new ArrayList<>();
    private SeekBar catagorySeekbar;
    private boolean seekcheck;
    private int seekprogresscount;
    private HelperMethods helperMethods;
    private boolean close = true;
    RecyclerView recyclerView;
    private boolean runsingledriver = true;
    private NavDrawerListAdapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private TextView timeText;
    private TextView centerMarker;
    private TextView maxSize;
    private TextView minFare;
    private TextView estimateFare;
    private static boolean isdialoglist = false;
    private ListView dialogautoCompleteList;
    private double catPerKm;
    private double isFixedPrice;
    private double catPercentage;
    private String catagorySt = "1";
    private ArrayList<GetDriverInfo> driverList = new ArrayList<>();
    private int driverCount = 0;
    private String dropLat = "";
    private String dropLng = "";
    private String searchedResult;
    private String searchText;
    private String pickLat;
    private String pickLng;
    private String tripdate;
    private String locationAddress="";
    private boolean isAccepted;
    private TripHistory createdTrip;
    private boolean isRootDraw;
   // private TextView timer;
    //    private TripHistory tripStatus;
    int i = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private RelativeLayout trip_progress_layout;
    private ProgressBar tripProgrss;
    private TextView tripProgressText;

    private String TAG = "MainScreenActivity";
    private boolean isLocationUdated = false;
    private boolean isUpdatedLocation = true;
    private AdView mAdView;
    private Location lastLoction;
    private Location lastLoctionNearBy;
    private RelativeLayout pickupLayout;
    private MarkerOptions markerOptions;
    private Marker markerDriver;
    private Marker markerPickup;
    private Marker markerOptionsdes;
    private double addressLng;
    private double addressLat;
    private ImageView imgMyLocation, ivLocation;
    CameraUpdate cu;
    private String destinationAddress;
    private ArrayList<DriverConstants> constantsList=new ArrayList<>();
    private String driverRadius="2";
    private Timer timertast;

    @BindView(R.id.fare_details_button)
    ImageView fare_details_button;
    @BindView(R.id.fare_details_layout)
    LinearLayout fare_details_layout;

    @BindView(R.id.base_fare_value)
    TextView base_fare_value;
    @BindView(R.id.per_Km_value)
    TextView per_Km_value;
    @BindView(R.id.per_min_value)
    TextView per_min_value;
    @BindView(R.id.distance_value)
    TextView distance_value;
    @BindView(R.id.max_size_value)
    TextView max_size_value;
    @BindView(R.id.estimate_value)
    TextView estimate_value;
    @BindView(R.id.tv_duration_text)
    TextView timer;
    @BindView(R.id.tv_price_calculator)
    TextView price_calculator;
    @BindView(R.id.time_value)
    TextView time_value;
    @BindView(R.id.refressh_driver_progressbar)
    ProgressBar progressDriver;

    @BindView(R.id.timer_circle)
    RelativeLayout driverTimer;

    @BindView(R.id.cropTextureView)
    TextureVideoView mTextureVideoView;






    public boolean isButtonShowing=false;
    private double distanceCover=0.0;
    private double minuts=0.0;
    private Timer timerForTripStatus=new Timer();

    @OnClick(R.id.fare_details_button)
    public void  openFareDetailsLayout(){
        if(isButtonShowing){
            price_calculator.setVisibility(View.VISIBLE);
            fare_details_button.setVisibility(View.GONE);
            fare_details_layout.setVisibility(View.GONE);
            isButtonShowing=false;
        }else{
            price_calculator.setVisibility(View.GONE);
            fare_details_button.setVisibility(View.VISIBLE);
            fare_details_layout.setVisibility(View.VISIBLE);
            isButtonShowing=true;
        }
    }

    @OnClick(R.id.tv_price_calculator)
    public void openFareDetailsLayoutw(){
        if(isButtonShowing){
            price_calculator.setVisibility(View.VISIBLE);
            fare_details_button.setVisibility(View.GONE);
            fare_details_layout.setVisibility(View.GONE);
            isButtonShowing=false;
        }else{
            price_calculator.setVisibility(View.GONE);
            fare_details_button.setVisibility(View.VISIBLE);
            fare_details_layout.setVisibility(View.VISIBLE);
            isButtonShowing=true;
        }
    }


    @OnClick(R.id.cancel_trip)
    public void onCancelTrip(){
        updateTripAsCancelledOnRequest();
    }


    private void updateTripAsCancelledOnRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.USER_ID, controller.pref.getUserID());
        params.put(Constants.Keys.TRIP_ID, controller.getCurrentTrip().getTripId());
        params.put(Constants.Keys.TRIP_STATUS, "cancel");
        System.out.println("UPDATE_TRIP  Expired Params : " + params);
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_UPDATE_TRIP, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                trip_progress_layout.setVisibility(View.GONE);
                mTextureVideoView.stop();
              //  timerForTripStatus.cancel();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        notifyCarTypeChangeCallback = this;
        getActionBar().hide();
        ButterKnife.bind(this);
        controller = (Controller) getApplication();
        dialog=new CustomProgressDialog(this);
        progressDialog = (ProgressBar) findViewById(R.id.progressBar1);
        tripProgrss = (ProgressBar) findViewById(R.id.trip_progressbar);
        tripProgrss.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.yellow_color), android.graphics.PorterDuff.Mode.SRC_ATOP);
        progressDialog.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        //tripProgrss.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        timeText = (TextView) findViewById(R.id.time_text);
        tripProgressText = (TextView) findViewById(R.id.progressText);
        imgMyLocation = (ImageView) findViewById(R.id.imgMyLocation);
        imgMyLocation.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.horizontalRecyclerView);
        ivLocation = (ImageView) findViewById(R.id.iv_location);
        parseJson = new ParseJson(MainScreenActivity.this);
        helperMethods = new HelperMethods(MainScreenActivity.this);
        markerOptions = new MarkerOptions();

        HelperMethods.manageTripStatus(MainScreenActivity.this, controller.pref.getTripStatus(), mNotificationReceiver);
        LocalBroadcastManager.getInstance(MainScreenActivity.this).registerReceiver(mNotificationReceiver, new IntentFilter("some_custom_id"));

        getAllView();
        trip_progress_layout.setVisibility(View.GONE);
        mTextureVideoView.stop();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        manageBottonSlider();


        if (controller.isPush()) {
            if (dialogCount == 0) {
                try {
                    Map<String, String> data = controller.getRemoteMessageData();
                    final String tripStatus = data.get(Constants.Keys.TRIP_STATUS);
                    if (!controller.pref.getTripStatus().equals("no")) {

                        if(tripStatus.equals("end")){

                        }else{
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            // NotificationDialog fareDialog = new NotificationDialog();
                            DialogFragment newFragment = NotificationDialog.newInstance(MainScreenActivity.this);
                            newFragment.show(ft, "dsads");
                        }
                    } else {

                        if(tripStatus.equals("accept")||tripStatus.equals("arrive")){
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            // NotificationDialog fareDialog = new NotificationDialog();
                            DialogFragment newFragment = NotificationDialog.newInstance(MainScreenActivity.this);
                            newFragment.show(ft, "dsads");
                        }
                        controller.pref.setTripStatus("eeee");
                    }

                } catch (Exception e) {

                }
            }

        }




       // getConstantApi();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        manageSerching();


        //custom get Location
        imgMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    getMyLocation();
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (GetDriverInfo info : driverList) {

                        LatLng latLng = new LatLng(Double.parseDouble(info.getD_lat()), Double.parseDouble(info.getD_lng()));
                        markerOptions.position(latLng);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.carimage));

                        builder.include(latLng);
                    }

                    LatLngBounds bounds;
                    bounds = builder.build();

                    // define value for padding
                    int padding = 20;
                    //This cameraupdate will zoom the map to a level where both location visible on map and also set the padding on four side.
                    cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.moveCamera(cu);
                }catch (Exception e){

                }

            }
        });

        mTextureVideoView.stop();
        mTextureVideoView.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);

        Uri FILE_URL  = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.taxivideo);
        mTextureVideoView.setDataSource(this,FILE_URL);

        mTextureVideoView.setListener(new TextureVideoView.MediaPlayerListener() {
            @Override
            public void onVideoPrepared() {

            }

            @Override
            public void onVideoEnd() {

                    ///// On video completion restart it again
                    mTextureVideoView.pause();
                    mTextureVideoView.play();
                ////
            }

            @Override
            public void onErrorOccured() {



            }
        });


        mTextureVideoView.play();

        findNearbyDrivers();
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(navrl)) {
            mDrawerLayout.closeDrawer(navrl);
        } else {
            super.onBackPressed();
        }


    }

    boolean istripStart;

    private void startTripApi() {

        if (driverList.size() > driverCount) {
            trip_progress_layout.setVisibility(View.VISIBLE);
            mTextureVideoView.play();
            tripProgrss.setVisibility(View.VISIBLE);
            tripProgressText.setText(R.string.searching_driver);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            tripdate = dateFormat.format(date);
            callTripApi();
        } else {
            Toast.makeText(MainScreenActivity.this, R.string.sorryNo_nearby_driver_found, Toast.LENGTH_LONG).show();
        }
    }

    public void getLocationAddress() {
        istripStart = true;
      /*  LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext(), new GeocoderHandler());*/
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MainScreen Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNotificationReceiver);
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    int count = 0;
    private int dialogCount = 0;
    protected BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("onReceive== " + intent.getStringExtra("message"));
            // Toast.makeText(MainScreenActivity.this, intent + "  " + intent + intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
            Bundle bundle = getIntent().getExtras();
            //  if(bundle.getString("action_id")
            if (dialogCount == 0) {
                try {
                    Map<String, String> data = controller.getRemoteMessageData();
                    final String tripStatus = data.get(Constants.Keys.TRIP_STATUS);
                    if (!controller.pref.getTripStatus().equals("no")) {

                        if(tripStatus.equals("end")){

                        }else{
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            // NotificationDialog fareDialog = new NotificationDialog();
                            DialogFragment newFragment = NotificationDialog.newInstance(MainScreenActivity.this);
                            newFragment.show(ft, "dsads");
                        }
                    } else {

                        if(tripStatus.equals("accept")||tripStatus.equals("arrive")){
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            // NotificationDialog fareDialog = new NotificationDialog();
                            DialogFragment newFragment = NotificationDialog.newInstance(MainScreenActivity.this);
                            newFragment.show(ft, "dsads");
                        }
                        controller.pref.setTripStatus("eeee");
                    }

                } catch (Exception e) {

                }
            }

        }
    };


    public static class NotificationDialog extends DialogFragment {

        private static MainScreenActivity mainScreenActivity;


        public static NotificationDialog newInstance(MainScreenActivity mainScreenActivityTemp) {
            NotificationDialog f = new NotificationDialog();
            mainScreenActivity = mainScreenActivityTemp;
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
            mainScreenActivity.controller.setPush(false);
            mainScreenActivity.dialogCount++;
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            View v = inflater.inflate(R.layout.dialog_notification, container, false);

            Map<String, String> data = mainScreenActivity.controller.getRemoteMessageData();
            final String tripStatus = data.get(Constants.Keys.TRIP_STATUS);
//            final String message = controller.getPushMessage();

            ((TextView) v.findViewById(R.id.noti_dialog_message)).setText(data.get(Constants.Keys.PRICE));
            ((TextView) v.findViewById(R.id.noti_dialog_ok)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainScreenActivity.dialogCount--;
                    mainScreenActivity.controller.pref.setTripStatus(tripStatus);
                    if (tripStatus.equalsIgnoreCase("accept") || tripStatus.equalsIgnoreCase("arrive")) {
                        getDialog().dismiss();
                        mainScreenActivity.controller.setAccepted(true);
                        try{
                            mainScreenActivity.timertast.cancel();
                        }catch (Exception e){

                        }
                        LocalBroadcastManager.getInstance(mainScreenActivity).unregisterReceiver(mainScreenActivity.mNotificationReceiver);
                        Intent intent = new Intent(mainScreenActivity, FragmentRouteNavigation.class);
                        mainScreenActivity.startActivity(intent);
                        mainScreenActivity.finish();
                        /*FragmentRouteNavigation fragmentRoute=new  FragmentRouteNavigation();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_navigation, fragmentRoute);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();*/

                    } else if (tripStatus.equalsIgnoreCase("end")) {
                    /*    getDialog().dismiss();
                        LocalBroadcastManager.getInstance(mainScreenActivity).unregisterReceiver(mainScreenActivity.mNotificationReceiver);
                        Intent intent = new Intent(mainScreenActivity, FragmentRouteNavigation.class);
                        mainScreenActivity.startActivity(intent);
                        mainScreenActivity.finish();*/
                    }
                    if (tripStatus.equalsIgnoreCase("begin")) {
                        try{
                            mainScreenActivity.timertast.cancel();
                        }catch (Exception e){

                        }
                        LocalBroadcastManager.getInstance(mainScreenActivity).unregisterReceiver(mainScreenActivity.mNotificationReceiver);
                        Intent intent = new Intent(mainScreenActivity, FragmentRouteNavigation.class);
                        mainScreenActivity.startActivity(intent);
                        mainScreenActivity.finish();
                        getDialog().dismiss();
                    } else {
                        getDialog().dismiss();
                    }

                }
            });


            return v;
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
             /*   case 1:
                    Bundle bundle = message.getData();
                    String loc = bundle.getString("address");
                    try{
                        if(loc.equals(null)||loc.equals("")){

                        }else {
                            locationAddress=loc;
                            centerMarker.setText(locationAddress);
                        }
                    }catch (Exception e){

                    }



                    break;
                default:
                    locationAddress = null;*/
            }
            if (istripStart) {

            }

        }
    }


    public void updateUserLatLng(double lat, double lng) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey()); //Verified
        params.put("u_lat", String.valueOf((float) lat));
        params.put("u_lng", String.valueOf((float) lng));
        params.put("user_id", controller.pref.getUserID());
        System.out.print("updateUserLatLng Params : " + params);
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {

            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                isLocationUdated = isUpdate;
            }
        });
    }

    private boolean isCalling;

    private void callTripApi() {
        System.out.println("callTripApi");
        if (dropLat == null || dropLat.equals("")) {
            Toast.makeText(MainScreenActivity.this, R.string.no_destination_address_found, Toast.LENGTH_LONG).show();
            trip_progress_layout.setVisibility(View.GONE);
            mTextureVideoView.stop();

        } else if (pickLat.equals("") || pickLat.equals(null) || locationAddress.equals("") || locationAddress.equals(getString(R.string.sorry_internet_connection_is_too_weak))) {
            trip_progress_layout.setVisibility(View.GONE);
            mTextureVideoView.stop();
            Toast.makeText(MainScreenActivity.this, R.string.no_pickup_address_found, Toast.LENGTH_LONG).show();
        } else if (driverList.size() == 0) {
            trip_progress_layout.setVisibility(View.GONE);
            mTextureVideoView.stop();
            Toast.makeText(MainScreenActivity.this, R.string.sorryNo_nearby_driver_found, Toast.LENGTH_LONG).show();
        }
        else if (!isNetworkConnected()){
            Toast.makeText(MainScreenActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        else {
            if (isCalling) {
                return;
            }

            isCalling = true;
            controller.setDestinationLocation(dropLat + "," + dropLng);
            controller.setSourceLocation(pickLat + "," + pickLng);
            controller.setDestinationAddr(destinationAddress);
            controller.setDriverId("0");
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey()); //Verified
            params.put(Constants.Keys.USER_ID, controller.pref.getUserID()); //Verified
            params.put(Constants.Keys.DRIVER_ID, String.valueOf(0)); //Verified
            params.put(Constants.Keys.SCHEDULED_DROP_LAT, dropLat); //Verified
            params.put(Constants.Keys.SCHEDULED_DROP_LNG, dropLng); //Verified
            params.put(Constants.Keys.SEARCH_RESULT_ADDR, searchedResult);
            params.put(Constants.Keys.SEARCH_ADDR, searchText);
            params.put(Constants.Keys.TRIP_DEST_LOC, destinationAddress);
            params.put(Constants.Keys.TRIP_STATUS, "request");
            params.put(Constants.Keys.SCHEDULED_PICK_LAT, pickLat);
            params.put(Constants.Keys.SCHEDULED_PICK_LNG, pickLng);
            params.put(Constants.Keys.TRIP_DATE, tripdate);
            params.put(Constants.Keys.TRIP_PICK_LOC, locationAddress);
            float distance = getTripDistance(pickLat, pickLng, dropLat, dropLng);
            params.put(Constants.Keys.TRIP_DISTANCE, "" + 0.0);
            params.put(Constants.Keys.TRIP_PAY_AMOUNT, "" + 0.0);

            WebService.excuteRequest(this, params, Constants.Urls.URL_USER_CREATE_TRIP, new WebService.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    isCalling = false;
                    if (isUpdate) {
                        isCalling = false;
                        createdTrip = parseJson.getTripDetails(data.toString());
                        controller.setCurrentTrip(createdTrip);
                        controller.pref.setTripResponce(data.toString());
                        controller.pref.setTripId(data.toString());
                        sendDriverPushNotificationToAllDriver();
                    } else {
                        if (error == null) {
                            GrepixUtils.dialogcalling(MainScreenActivity.this);
                        }
                    }
                }
            });

        }

    }


    public float getTripAmount(float distance) {
        if (catagorySt == null) {
            catagorySt = "1";
        }
        Catagories category = catList.get(Integer.parseInt(catagorySt) - 1);
        return Integer.parseInt(category.getCat_fare_per_km()) * distance;
    }

    public float getTripDistance(String pLat, String pLng, String dLat, String dLng) {
        Location locPickUp = new Location("point A");
        locPickUp.setLatitude(Float.parseFloat(pLat));
        locPickUp.setLongitude(Float.parseFloat(pLng));
        Location locDropUp = new Location("point B");
        locDropUp.setLatitude(Float.parseFloat(dLat));
        locDropUp.setLongitude(Float.parseFloat(dLng));
        float distance = locPickUp.distanceTo(locDropUp);
        float loc = distance / 1000.0f;
        return 0.0f;
    }


    private void sendDriverPushNotificationToAllDriver() {
        if (driverList != null && driverList.size() > 0) {
            ArrayList<String> androidTokens = new ArrayList<>();
            ArrayList<String> iosTokens = new ArrayList<>();
            for (GetDriverInfo driverInfo : driverList) {
                if (driverInfo.getD_device_type() != null && driverInfo.getD_device_type().equals("Android")) {
                    if (driverInfo.getD_device_token().equals(null) || driverInfo.getD_device_token().equals("")) {
                    } else {
                        androidTokens.add(driverInfo.getD_device_token());
                    }

                } else {
                    if (driverInfo.getD_device_token().equals(null) || driverInfo.getD_device_token().equals("")) {
                    } else {
                        iosTokens.add(driverInfo.getD_device_token());
                    }
                }
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("message", "You got a taxi request");
            params.put("trip_id", createdTrip.getTripId());
            params.put("trip_status", "request");


            if (iosTokens.size() > 0) {
                params.put("ios", TextUtils.join(",", iosTokens));
            }
            if (androidTokens.size() > 0) {
                params.put("android", TextUtils.join(",", androidTokens));
            }

            System.out.println("\nParams Notification : " + params);
            WebService.excuteRequest(this, params, Constants.Urls.URL_DRIVER_NOTIFICATION, new WebService.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    if (isUpdate) {
                        tripProgressText.setText(R.string.waiting_for_driver);
                        try{
                            timerForTripStatus.schedule(
                                    new TimerTask() {
                                        @Override
                                        public void run() {
                                            if (isAccepted) {
                                                tripProgrss.setVisibility(View.GONE);
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        callTripStatusApi();
                                                    }
                                                });

                                            }
                                        }
                                    },
                                    40000
                            );
                        }catch (Exception e){

                        }

                    }
                }
            });
        } else {
            Toast.makeText(MainScreenActivity.this, R.string.driver_not_available, Toast.LENGTH_LONG).show();
        }
    }


    private void callTripStatusApi() {
        tripProgressText.setText(R.string.loading);
        String Url;
        Url = Constants.Urls.URL_USER_GET_TRIP;
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.TRIP_ID, createdTrip.getTripId());
        params.put(Constants.Keys.USER_ID, controller.pref.getUserID());

        WebService.excuteRequest(this, params, Url, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                isCalling = false;
                trip_progress_layout.setVisibility(View.GONE);
                mTextureVideoView.stop();
                tripProgrss.setVisibility(View.GONE);
                if (isUpdate) {
                    TripHistory tripHistory = parseJson.parseSingleTrip(data.toString());
                    createdTrip = tripHistory;
                    controller.setCurrentTrip(tripHistory);
                    //  controller.pref.setTripResponce(tripHistory.getTripId());
                    if (tripHistory != null && tripHistory.getTripStatus() != null) {
                        if(tripHistory.getTripStatus().equalsIgnoreCase("cancel")){
                            return;
                        }
                        if (tripHistory.getTripStatus().equalsIgnoreCase("request")) {
                            showDriverNotFountAlert();
                        } else {
                            controller.pref.setTripStatus(tripHistory.getTripStatus());
                            if (!controller.isAccepted()) {
                                controller.setAccepted(true);
                                timertast.cancel();
                                LocalBroadcastManager.getInstance(MainScreenActivity.this).unregisterReceiver(mNotificationReceiver);
                                showDriverAcceptedRequestFountAlert(tripHistory);
                            }else{
                                controller.setAccepted(false);
                                callTripStatusApi();
                            }

                        }
                    } else {

                        showDriverNotFountAlert();
                    }
                } else {
                    if (error == null) {
                        GrepixUtils.dialogcalling(MainScreenActivity.this);
                    }
                }
            }
        });

    }

    AlertDialog alertDialog;

    public void showDriverAcceptedRequestFountAlert(TripHistory jsonObject) {
        String strigMessage;

        try{
            strigMessage =
                    getResources().getString(R.string.accept_your_request);
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                    new android.support.v7.app.AlertDialog.Builder(MainScreenActivity.this, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setMessage("" + strigMessage);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setInverseBackgroundForced(false);
            alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(controller.isAccepted()){
                        controller.setAccepted(false);
                    }

                    Intent intent = new Intent(MainScreenActivity.this, FragmentRouteNavigation.class);
                    startActivity(intent);
                    finish();
                }
            });
            close = false;
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }catch (Exception e){

        }

    }

    public void showDriverNotFountAlert() {
        try {
            trip_progress_layout.setVisibility(View.GONE);
            mTextureVideoView.stop();
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                    new android.support.v7.app.AlertDialog.Builder(MainScreenActivity.this, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setMessage(R.string.all_drivers_are_busy);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setInverseBackgroundForced(false);
            alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
            close = false;

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            updateTripAsExpired();
        } catch (Exception e) {

        }

    }


    private void updateTripAsExpired() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.USER_ID, controller.pref.getUserID());
        params.put(Constants.Keys.TRIP_ID, controller.getCurrentTrip().getTripId());
        params.put(Constants.Keys.TRIP_STATUS, "expired");
        System.out.println("UPDATE_TRIP  Expired Params : " + params);
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_UPDATE_TRIP, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {

            }
        });
    }

    public void getcategory() {
        progressDialog.setVisibility(View.VISIBLE);
        Catagories dummyResponse = new Catagories();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_GET_CATEGORY, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                isCalling = false;
                if (isUpdate) {
                    try{
                      /*  Catagories dummyResponse = new Catagories();

                        controller.pref.setCatagoryResponce(data.toString());
                        dummyResponse.setCategory_id("4");
                        dummyResponse.setCat_name("Bike");
                        dummyResponse.setCat_desc("bike");
                        dummyResponse.setCat_base_price("10");
                        dummyResponse.setCat_fare_per_km("1");
                        dummyResponse.setCat_fare_per_min("1");
                        dummyResponse.setCat_max_size("2");
                        dummyResponse.setCat_is_fixed_price("1");
                        dummyResponse.setCat_prime_time_percentage("10");
                        dummyResponse.setCat_status("1");
                        dummyResponse.setCat_created("2018-01-22 00:00:00");
                        dummyResponse.setCat_modified("2018-01-22 00:00:00");

                        catList.clear();
                        catList.add(dummyResponse);*/

                        catListformApi = parseJson.getCatgory(data.toString());
                        controller.pref.setCatagoryResponce(data.toString());

                        Collections.sort(catListformApi, new Comparator<Catagories>() {
                            @Override
                            public int compare(Catagories o1, Catagories o2) {
                                return o2.getCategory_id().compareTo(o1.getCategory_id());
                            }
                        });

                        catList.clear();
                        catList.addAll(catListformApi);

                        System.out.println("@@@--- cat list size is"+catList.size());

                        if(catList.size()!=0){
                            catList.get(0).setSelected(true);
                        }

                        custom_horizontal_recycler_adapter = new Custom_Horizontal_Recycler_Adapter(catList,getApplicationContext(),notifyCarTypeChangeCallback);
                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainScreenActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(horizontalLayoutManager);
                        recyclerView.setAdapter(custom_horizontal_recycler_adapter);
                        String category = "4";
                        calculateFare(catList, 0);
                        for(DriverConstants constants:constantsList){
                            if(constants.getConstant_key().equals("driver_radius")){
                                driverRadius=constants.getConstant_value();
                            }
                        }
                        isRefreshDrivers=false;
                       /* ((TextView)findViewById(R.id.categoryitem1)).setText(catList.get(2).getCat_name());
                        ((TextView)findViewById(R.id.categoryitem2)).setText(catList.get(1).getCat_name());
                        ((TextView)findViewById(R.id.categoryitem3)).setText(catList.get(0).getCat_name());*/
                        getNearByDriver(category, latitude, longitude, true);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    if (error == null) {
                        Toast.makeText(MainScreenActivity.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                    }
                    String catrsponse = controller.pref.getCatagoryResponce();
                    if(catrsponse!=null){
                       // Catagories dummyResponse = new Catagories();

                   /*     dummyResponse.setCategory_id("4");
                        dummyResponse.setCat_name("Bike");
                        dummyResponse.setCat_desc("bike");
                        dummyResponse.setCat_base_price("10");
                        dummyResponse.setCat_fare_per_km("1");
                        dummyResponse.setCat_fare_per_min("1");
                        dummyResponse.setCat_max_size("2");
                        dummyResponse.setCat_is_fixed_price("1");
                        dummyResponse.setCat_prime_time_percentage("10");
                        dummyResponse.setCat_status("1");
                        dummyResponse.setCat_created("2018-01-22 00:00:00");
                        dummyResponse.setCat_modified("2018-01-22 00:00:00");

                        catList.add(dummyResponse);*/

                        catListformApi = parseJson.getCatgory(catrsponse);

                        Collections.sort(catListformApi, new Comparator<Catagories>() {
                            @Override
                            public int compare(Catagories o1, Catagories o2) {
                                return o2.getCategory_id().compareTo(o1.getCategory_id());
                            }
                        });

                        catList.addAll(catListformApi);

                        if(catList.size()!=0){
                            catList.get(0).setSelected(true);
                        }
                        custom_horizontal_recycler_adapter = new Custom_Horizontal_Recycler_Adapter(catList,getApplicationContext(),notifyCarTypeChangeCallback);
                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainScreenActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(horizontalLayoutManager);
                        recyclerView.setAdapter(custom_horizontal_recycler_adapter);
                    }
                }
            }
        });
    }

//    // **************************  Sliding Menu Function Start **********************


    private DrawerLayout mDrawerLayout;
    private RelativeLayout navrl;
    private CircularImageView1 profileImage;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @SuppressLint("ResourceType")
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

        }else{
            profileImage.setImageUrl(Constants.Urls.IMAGE_BASE_URL + userInfo.getUProfileImagePath(), imageLoader);
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
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
        /*navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));*/
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


    private void getMyLocation() {
        if(isRouteNotDraw){
            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
            mMap.animateCamera(cameraUpdate);
        }
    }

    // **************************  Sliding Menu Function Start **********************

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


    @Override
    public void notifyCarTypeChange(int position) {
        lastlatitute = "0.0";
        lastlongitute = "0.0";
      //  sliderlayout.showHandle();
        selectedIndex=position;
        String category = catList.get(position).getCategory_id();
        calculateFare(catList, position);
        catagorySt = category;
        isRefreshDrivers=false;
        getNearByDriver(category, latitude, longitude, true);
        custom_horizontal_recycler_adapter.notifyDataSetChanged();
    }

    private void displayView(int position) {
        // update the main content by replacing fragments

        switch (position) {
            case 0:

                close = false;
                runsingledriver = false;
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
                close = false;
                runsingledriver = false;
                Intent pay = new Intent(getApplicationContext(), SosActivity.class);

                pay.putExtra("userid", controller.pref.getUserID());
                pay.putExtra("fbuserproimg", "");
                pay.putExtra("whologin", "");
                pay.putExtra("password", "");
                pay.putExtra("accept", "");
                pay.putExtra("lat", String.valueOf(latitude));
                pay.putExtra("long", String.valueOf(latitude));
                startActivity(pay);

                break;

            case 2:

                close = false;

                Intent history = new Intent(getApplicationContext(), Myreview.class);

                history.putExtra("userid", controller.pref.getUserID());
                history.putExtra("fbuserproimg", "");

                history.putExtra("whologin", "");
                history.putExtra("password", "");

                startActivity(history);
                break;


            case 3:

                close = false;

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                //change the type of data you need to share,
                // //for image use "image/*"

                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, Constants.Urls.URL_TO_SHARE);
                startActivity(Intent.createChooser(intent, "Share"));

                break;


            case 4:

                close = false;
                mDrawerLayout.closeDrawer(navrl);
                close = false;
                android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(MainScreenActivity.this, R.style.AppCompatAlertDialogStyle);
                builder2.setTitle(R.string.exit);
                builder2.setMessage(R.string.do_you_want_deactivate_now);
                builder2.setCancelable(false);
                builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        handleDeactivateAccount();
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
                builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();
                    }
                    //  alertdialog2.cancel();

                });

                builder2.show();
                break;


            case 5:

                close = false;
                String[] email={Constants.Urls.EMAIL_FOR_SUPPORT};
                shareToGMail(email,"Hire Me User App Support","");
                break;

            case 6:
                close = false;
                runsingledriver = false;
                Intent walletIntent = new Intent(getApplicationContext(), WalletOrder.class);

                Bundle animation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(walletIntent, animation);

                break;

            case 7:
                close = false;
                runsingledriver = false;
                Intent coinWallet = new Intent(getApplicationContext(), XeniaCoinActivity.class);

                startActivity(coinWallet);
                break;

            case 8:
                //SupportPage

                mDrawerLayout.closeDrawer(navrl);
                close = false;
                android.support.v7.app.AlertDialog.Builder builder =
                        new android.support.v7.app.AlertDialog.Builder(MainScreenActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.exit);
                builder.setMessage(R.string.do_you_want_exit_now);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        handleLogOut();

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

            default:
                break;
        }
        mDrawerLayout.closeDrawer(navrl);

    }

    private void handleDeactivateAccount() {
        showProgress();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey()); //Verified
        params.put("active","0");
        params.put("user_id", controller.pref.getUserID());
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {

            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if(isUpdate){
                    hideProgress();
                    handleLogOut();
                }else{
                    hideProgress();
                }


            }
        });
    }


    public void shareToGMail(String[] email, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for(final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);
    }


    private void handleLogOut() {
        showProgress();
        close = false;
        runsingledriver = false;
        DeviceTokenService.logout(controller, new DeviceTokenService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                hideProgress();
                controller.pref.saveIsLoginSucess(false);
                Intent i = new Intent(getApplicationContext(), SigninActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(i, bndlanimation);
                finish();
            }
        });
    }

    private CustomProgressDialog dialog;

    public void showProgress() {
        dialog.showDialog();
    }

    public void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
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
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    int selectedIndex=2;
    private void manageBottonSlider() {
      /*  sliderlayout = (SlideUpPanelLayout) findViewById(R.id.slide_layout);
        sliderlayout.showHandle();
        sliderlayout.setVisibility(View.VISIBLE);
        TextView content = (TextView) findViewById(R.id.content);
        final RelativeLayout pickup = (RelativeLayout) findViewById(R.id.pickup_layout_bottom);
        content.setVisibility(View.INVISIBLE);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sliderlayout.getPanelState() == SlideUpPanelLayout.PanelState.HIDDEN) {
                    pickup.setVisibility(View.GONE);
                    sliderlayout.showHandle();
                    getActionBar().hide();
                } else {
                    pickup.setVisibility(View.VISIBLE);
                    sliderlayout.hideHandle();
                    getActionBar().hide();
                }
            }
        });*/

        String responce = controller.pref.getCatagoryResponce();
        try {
            if (responce.equals("") || responce.equals(null)) {

            } else {
                catList = parseJson.getCatgory(responce);
                calculateFare(catList, 0);
            }



        catagorySeekbar = (SeekBar) findViewById(R.id.seekBar1);
        final Drawable d = getResources().getDrawable(R.drawable.track);
        d.setBounds(new Rect(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight()));
        catagorySeekbar.setProgressDrawable(d);

        final Drawable suv = getResources().getDrawable(R.drawable.suv_new);
        final Drawable sedan = getResources().getDrawable(R.drawable.sedan_new);
        final Drawable hatchback = getResources().getDrawable(R.drawable.hatchback_new);


        suv.setBounds(new Rect(0, 0, suv.getIntrinsicWidth(), suv.getIntrinsicHeight()));
        sedan.setBounds(new Rect(0, 0, sedan.getIntrinsicWidth(), sedan.getIntrinsicHeight()));
        hatchback.setBounds(new Rect(0, 0, hatchback.getIntrinsicWidth(), hatchback.getIntrinsicHeight()));

        final Drawable scar1 = getResources().getDrawable(R.drawable.hatchback_new);
        final Drawable scar2 = getResources().getDrawable(R.drawable.sedan_new);
        final Drawable scar3 = getResources().getDrawable(R.drawable.suv_new);


        scar1.setBounds(new Rect(0, 0, scar1.getIntrinsicWidth(), scar1.getIntrinsicHeight()));
        scar2.setBounds(new Rect(0, 0, scar2.getIntrinsicWidth(), scar2.getIntrinsicHeight()));
        scar3.setBounds(new Rect(0, 0, scar3.getIntrinsicWidth(), scar3.getIntrinsicHeight()));


/*        catagorySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {

                if (GrepixUtils.net_connection_check(MainScreenActivity.this)) {

                    if (seekprogresscount == 5) {
                        lastlatitute = "0.0";
                        lastlongitute = "0.0";
                    //    sliderlayout.showHandle();
                        selectedIndex=0;
                        seekBar.setProgress(seekprogresscount);
                        seekBar.setThumb(hatchback);
                        String category = catList.get(2).getCategory_id();
                        calculateFare(catList, 2);
                        catagorySt = category;
                        isRefreshDrivers=false;
                        getNearByDriver(category, latitude, longitude, true);
                    } else if (seekprogresscount == 50) {
                        lastlatitute = "0.0";
                        lastlongitute = "0.0";
                        selectedIndex=1;
//                        sliderlayout.showHandle();
                        seekBar.setProgress(seekprogresscount);
                        seekBar.setThumb(sedan);
                        String category = catList.get(1).getCategory_id();
                        calculateFare(catList, 1);
                        catagorySt = category;
                        isRefreshDrivers=false;
                        getNearByDriver(category, latitude, longitude, true);
                    } else if (seekprogresscount == 95) {
                        lastlatitute = "0.0";
                        lastlongitute = "0.0";
                        selectedIndex=2;
             //           sliderlayout.showHandle();
                        seekBar.setProgress(seekprogresscount);
                        seekBar.setThumb(suv);
                        String category = catList.get(0).getCategory_id();
                        calculateFare(catList, 0);
                        catagorySt = category;
                        isRefreshDrivers=false;
                        getNearByDriver(category, latitude, longitude, true);

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
                seekcheck = true;
                if (progress <= 25) {
                    seekprogresscount = 5;
                    seekBar.setThumb(scar1);
                } else if (progress > 25 && progress <= 75) {

                    seekprogresscount = 50;
                    seekBar.setThumb(scar2);
                } else if (progress > 75) {

                    seekprogresscount = 95;
                    seekBar.setThumb(scar3);
                }
                //add here your implementation
            }

        });*/

           /* ((TextView)findViewById(R.id.categoryitem1)).setText(getString(R.string.category_1));
            ((TextView)findViewById(R.id.categoryitem2)).setText(getString(R.string.category_2));
            ((TextView)findViewById(R.id.categoryitem3)).setText(getString(R.string.category_3));*/
        } catch (Exception e) {

        }
    }

    private void calculateFare(ArrayList<Catagories> catList, int type) {

        try {
            maxSize.setText(catList.get(type).getCat_max_size());
            isFixedPrice = Double.parseDouble(catList.get(type).getCat_is_fixed_price());
            catPerKm = Double.parseDouble(catList.get(type).getCat_fare_per_km());

            catPercentage = Double.parseDouble(catList.get(type).getCat_prime_time_percentage());




            double serviceTaxPercent = 0.0;
            for (DriverConstants driverConstants : constantsList) {
                if (driverConstants.getConstant_key().equalsIgnoreCase("service_tax")) {
                    serviceTaxPercent = Double.parseDouble(driverConstants.getConstant_value());
                }

            }


            double distanceInUnit=controller.checkDistanceUnit().equalsIgnoreCase("km")?distanceCover*0.001:distanceCover*0.000621371;
            double totalPrice = Double.parseDouble(catList.get(type).getCat_base_price()) +(distanceInUnit-1<1?0.0:(distanceInUnit-1)* Double.parseDouble(catList.get(type).getCat_fare_per_km()));
            // float tax1 = totalPrice *self.service_tax_percentage/100;
            //  double tripTax = totalPrice * serviceTaxPercent / 100;
            //   totalPrice = totalPrice +tripTax ;

           /* if (catList.get(type).getCat_is_fixed_price().equals("0")) {
                // float primepercentage=(totalPrice * self.cat_prime_time_percentage) / 100.0;
                double primepercentage = (totalPrice * Double.parseDouble(catList.get(type).getCat_prime_time_percentage())) / 100.0;
                totalPrice = totalPrice + primepercentage;
            }*/

            base_fare_value.setText(controller.currencyUnit()+ catList.get(type).getCat_base_price());
            per_Km_value.setText(controller.currencyUnit()+ catList.get(type).getCat_fare_per_km());
            per_min_value.setText(controller.currencyUnit()+catList.get(type).getCat_fare_per_min());
            distance_value.setText(String.format(controller.checkDistanceUnit().equalsIgnoreCase("km")?String.format("%.02f km", distanceCover*0.001):String.format("%.02f mi", distanceCover*0.000621371)));
            time_value.setText(minuts+" min");
            max_size_value.setText(catList.get(type).getCat_max_size());
            estimate_value.setText(controller.currencyUnit()+String.format("%.02f",totalPrice));

            controller.pref.setEstimatedDetails(controller.currencyUnit()+String.format("%.02f",totalPrice)+"|"+String.format("%.02f",distanceInUnit)
                    +" "+controller.checkDistanceUnit()+"|"+String.format("%.02f",minuts)+" min");

        } catch (Exception e) {
            Log.d("xxx",""+e);
        }
         /*try{

             minFare.setText(controller.currencyUnit() + totelPrice);



             estimateFare.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                     // EstimateFareDialog fareDialog = new EstimateFareDialog();
                     DialogFragment newFragment = EstimateFareDialog.newInstance(MainScreenActivity.this);
                     newFragment.show(ft, "dsads");
                 }
             });
         }catch (Exception e){

         }*/


    }




    public static class EstimateFareDialog extends DialogFragment {

        private static MainScreenActivity mainScreenActivity;

        public static EstimateFareDialog newInstance(MainScreenActivity mainScreenActivity) {
            EstimateFareDialog.mainScreenActivity = mainScreenActivity;
            EstimateFareDialog f = new EstimateFareDialog();
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            View v = inflater.inflate(R.layout.mainscreen_search_dialog, container, false);
            final EditText search = (EditText) v.findViewById(R.id.tv_dialog_destination);
            mainScreenActivity.dialogautoCompleteList = (ListView) v.findViewById(R.id.dialogAutoCompleteList);



            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // optimised way is to start searching for laction after user has typed minimum 3 chars
                    if (search.getText().length() > 3) {

                        // mainScreenActivity.mAutoCompleteList.setVisibility(View.VISIBLE);
                        //   searchBtn.setVisibility(View.GONE);

                        Runnable run = new Runnable() {


                            @Override
                            public void run() {

                                isdialoglist = true;
                                mainScreenActivity.dialogautoCompleteList.setVisibility(View.VISIBLE);
                                // cancel all the previous requests in the queue to optimise your network calls during autocomplete search
                                AppController.volleyQueueInstance.cancelRequestInQueue(mainScreenActivity.GETPLACESHIT);
                                //build Get url of Place Autocomplete and hit the url to fetch result.
                                mainScreenActivity.request = new VolleyJSONRequest(Request.Method.GET, mainScreenActivity.getPlaceAutoCompleteUrl(search.getText().toString()), null, null, mainScreenActivity, mainScreenActivity);
                                //Give a tag to your request so that you can use this tag to cancle request later.
                                mainScreenActivity.request.setTag(mainScreenActivity.GETPLACESHIT);

                                AppController.volleyQueueInstance.addToRequestQueue(mainScreenActivity.request);

                            }

                        };

                        // only canceling the network calls will not help, you need to remove all callbacks as well
                        // otherwise the pending callbacks and messages will again invoke the handler and will send the request
                        if (mainScreenActivity.handler != null) {
                            mainScreenActivity.handler.removeCallbacksAndMessages(null);
                        } else {
                            mainScreenActivity.handler = new Handler();
                        }
                        mainScreenActivity.handler.postDelayed(run, 1000);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });
            //  search.setText(mainScreenActivity.preFilledText);

            search.setSelection(search.getText().length());

            v.findViewById(R.id.dialog_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

            mainScreenActivity.dialogautoCompleteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // pass the result to the calling activity
                    //mainScreenActivity.isdialoglist = false;
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) mainScreenActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    search.setText("" + mainScreenActivity.predictions.getPlaces().get(position).getPlaceDesc());
                    mainScreenActivity.estimateFare.setText("" + mainScreenActivity.predictions.getPlaces().get(position).getPlaceDesc());
                    mainScreenActivity.dialogautoCompleteList.setVisibility(View.GONE);


                    String destination = search.getText().toString();


                    if (destination.isEmpty()) {
                        Toast.makeText(mainScreenActivity, "Please enter destination address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mainScreenActivity.progressDialog.setVisibility(View.VISIBLE);

                    getDialog().dismiss();
                    Toast.makeText(mainScreenActivity, mainScreenActivity.longitude + "," + mainScreenActivity.longitude + " " + destination, Toast.LENGTH_SHORT).show();
                    try {
                        new DirectionFinder(mainScreenActivity, mainScreenActivity.latitude + "," + mainScreenActivity.longitude, destination,"").execute();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            });


            return v;
        }

    }


    private void getAllView() {
        etDestination = (EditText) findViewById(R.id.tv_search_destination);
        centerMarker = (TextView) findViewById(R.id.locationMarkertext);
        maxSize = (TextView) findViewById(R.id.maxsize);
        minFare = (TextView) findViewById(R.id.minfaretext);
        estimateFare = (TextView) findViewById(R.id.getfareestimate);
        trip_progress_layout = (RelativeLayout) findViewById(R.id.trip_progress_layout);

        MobileAds.initialize(MainScreenActivity.this, getResources().getString(R.string.banner_ad_footer));
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //  mAdView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                //  mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.

                mAdView.setVisibility(View.GONE);
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.loadAd(adRequest);
        centerMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pickupLayout = (RelativeLayout) findViewById(R.id.picup_button);
        pickupLayout.setVisibility(View.INVISIBLE);
        pickupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(locationAddress.equals("")){
                    Toast.makeText(controller, "Please select pickup location", Toast.LENGTH_SHORT).show();
                }else{
                    startTripApi();
                }
                System.out.println("Start Trip Api Called .......");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        HelperMethods.manageTripStatus(MainScreenActivity.this, controller.pref.getTripStatus(), mNotificationReceiver);
        manageLeftslider();
        //findNearbyDrivers();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    private boolean isRefreshDrivers=false;
    private void findNearbyDrivers() {


        timertast = new Timer();
        timertast.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MainScreenActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(constantsList.size()==0){
                            getConstantApi();
                        }
                        if(latitude!=0&&longitude!=0){

                                isRefreshDrivers=true;
                                getNearByDriver(catagorySt,addressLat,addressLng, false);

                        }
                        if (!isLocationUdated) {
                            isLocationUdated = true;
                            if(isRouteNotDraw)
                                updateUserLatLng(latitude, longitude);
                        }
                    }
                });
            }
        }, 0, 15000);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        try{
            timertast.cancel();
        }catch (Exception e){

        }

        super.onPause();
    }

    public String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json");
        urlString.append("?input=");
        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlString.append("&location=");
        urlString.append(latitude + "," + longitude); // append lat long of current location to show nearby results.
        urlString.append("&radius=500&language=en");
        urlString.append("&key=" + getResources().getString(R.string.googleapikey));

        Log.d("FINAL URL:::   ", urlString.toString());
        return urlString.toString();
    }

    private void manageSerching() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mAutoCompleteList = (ListView) findViewById(R.id.searchResultLV);
        final ImageView clear = (ImageView) findViewById(R.id.search_icon);
        clear.setVisibility(View.INVISIBLE);
        if (etDestination.getText().toString().length() > 0) {
            clear.setVisibility(View.VISIBLE);
            isRouteNotDraw=true;
        }
        //Add a text change listener to implement autocomplete functionality
        etDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // optimised way is to start searching for laction after user has typed minimum 3 chars
                if (etDestination.getText().length() >= 2) {
                    clear.setVisibility(View.VISIBLE);
                    clear.setImageResource(R.drawable.cancel);
                    mAutoCompleteList.setVisibility(View.VISIBLE);
                    isdialoglist = false;

                    Runnable run = new Runnable() {

                        @Override
                        public void run() {

                            // cancel all the previous requests in the queue to optimise your network calls during autocomplete search
                            AppController.volleyQueueInstance.cancelRequestInQueue(GETPLACESHIT);
                            //build Get url of Place Autocomplete and hit the url to fetch result.
                            request = new VolleyJSONRequest(Request.Method.GET, getPlaceAutoCompleteUrl(etDestination.getText().toString()), null, null, MainScreenActivity.this, MainScreenActivity.this);
                            //Give a tag to your request so that you can use this tag to cancle request later.
                            request.setTag(GETPLACESHIT);

                            AppController.volleyQueueInstance.addToRequestQueue(request);

                        }

                    };

                    // only canceling the network calls will not help, you need to remove all callbacks as well
                    // otherwise the pending callbacks and messages will again invoke the handler and will send the request
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    } else {
                        handler = new Handler();
                    }
                    handler.postDelayed(run, 1000);

                } else {
                    if (etDestination.getText().length() == 0) {
                        pickupLayout.setVisibility(View.INVISIBLE);
                        clear.setVisibility(View.INVISIBLE);
                        isRouteNotDraw=false;
                        mAutoCompleteList.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        // etDestination.setText(preFilledText);

        etDestination.setSelection(etDestination.getText().length());


        //get permission for Android M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            fetchLocation();
        } else {

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOC);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            } else {
                fetchLocation();
            }
        }

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    etDestination.setText("");
                    isUpdatedLocation = true;
                    clear.setVisibility(View.INVISIBLE);
                    pickupLayout.setVisibility(View.INVISIBLE);
                    mAutoCompleteList.setVisibility(View.GONE);
                   /* try {
                        timer.setText(R.string.default_min);
                        timer.setVisibility(View.GONE);

                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }*/

                    markerOptionsdes.remove();
                    mMap.clear();
                    ivLocation.setVisibility(View.VISIBLE);
                    isRouteNotDraw=true;

                    //clear distance after click on cancel button
                    fare_details_button.setVisibility(View.GONE);
                    price_calculator.setVisibility(View.GONE);
                    fare_details_layout.setVisibility(View.GONE);
                    isButtonShowing=false;
                    distanceCover=0.0;
                    minuts=0.0;
                    lastlatitute = "0.0";
                    lastlongitute = "0.0";
                    lastcatagory = "10";
                    isRefreshDrivers=false;
                    getNearByDriver(catagorySt, latitude, longitude, false);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
                }catch (Exception e){

                }

            }
        });

        View view = this.getCurrentFocus();

        mAutoCompleteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // pass the result to the calling activity
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                searchText = etDestination.getText().toString().trim();
                etDestination.setText("" + predictions.getPlaces().get(position).getPlaceDesc());
                destinationAddress="" + predictions.getPlaces().get(position).getPlaceDesc();
                mAutoCompleteList.setVisibility(View.GONE);

                try {
                    mMap.clear();
                    lastlatitute = "0.0";
                    lastlongitute = "0.0";
                    lastcatagory = "10";
                    isRefreshDrivers=false;
                    getNearByDriver(catagorySt, latitude, longitude, false);
                } catch (Exception e) {
                    mMap.clear();
                    isRouteNotDraw=true;
                }
                //clear distance after click on cancel button


                String destination = etDestination.getText().toString();


                if (destination.isEmpty()) {
                    Toast.makeText(MainScreenActivity.this, R.string.please_enter_destination, Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setVisibility(View.VISIBLE);
                try {
                    new DirectionFinder(MainScreenActivity.this, pickLat + "," + pickLng, etDestination.getText().toString(),"").execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void fetchLocation() {

//Build google API client to use fused location
        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    String lastlatitute = "0.0";
    String lastlongitute = "0.0";
    String lastcatagory = "10";
    boolean isFirstTrue;

    private void getNearByDriver(final String category, final double latitude, final double longitude, final boolean iszooom) {

        //

        if (lastlatitute.equals(String.valueOf(latitude)) || lastlongitute.equals(String.valueOf(longitude)) || lastcatagory.equals(category)) {
            if(isRefreshDrivers){
                callDriverApi(category,iszooom,latitude,longitude);
            }else{
                return;
            }

        } else {
            callDriverApi(category,iszooom, latitude, longitude);
        }

    }

    private void callDriverApi(final String category, final boolean iszooom, double latitude, double longitude) {
        progressDriver.setVisibility(View.VISIBLE);
        driverTimer.setVisibility(View.GONE);
        lastcatagory = category;
        lastlatitute = String.valueOf(this.latitude);
        lastlongitute = String.valueOf(this.longitude);
        System.out.println(" GetNearByDriver Called : ");

        progressDialog.setVisibility(View.VISIBLE);
        timeText.setVisibility(View.GONE);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.LAT, "" +latitude);
        params.put(Constants.Keys.LNG, "" +longitude);
        params.put(Constants.Keys.DRIVER_ID, controller.pref.getUserID());
        params.put(Constants.Keys.CATAGORY, category == null ? "1" : category);
        params.put("miles", String.valueOf(driverRadius));

        WebService.excuteRequest(this, params,Constants.Urls.URL_USER_GET_DRIVER_INFO, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                isCalling = false;
                if (isUpdate) {
                    driverList = parseJson.getDriverInfos(data.toString(),false);

                    if (driverList.size() == 0) {
                        timeText.setVisibility(View.GONE);
                        if(isRouteNotDraw){
                            mMap.clear();
                        }else{
                            markerDriver.remove();
                        }
                    }else {
                        progressDriver.setVisibility(View.GONE);
                        driverTimer.setVisibility(View.VISIBLE);
                        if(isRouteNotDraw){
                            mMap.clear();
                        }else{
                            markerDriver.remove();
                        }
                        checkNearByDriverDistance();
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        LatLngBounds bounds;
                        for (GetDriverInfo info : driverList) {
                            LatLng latLng = new LatLng(Double.parseDouble(info.getD_lat()), Double.parseDouble(info.getD_lng()));
                            builder.include(latLng);
                        markerDriver = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.carimage)));
                            timeText.setVisibility(View.VISIBLE);
                        }
                        if (iszooom&&isRouteNotDraw) {
                            LatLng hcmus = new LatLng(MainScreenActivity.this.latitude, MainScreenActivity.this.longitude);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 16));
                                                           }try {
                            markerPickup.remove();
                        } catch (Exception e) {
                            progressDialog.setVisibility(View.GONE);
                            timeText.setVisibility(View.GONE);
                     }
                    }

                } else {
                    progressDialog.setVisibility(View.GONE);
                    timeText.setVisibility(View.GONE);
                    Toast.makeText(MainScreenActivity.this, R.string.connection_failed, Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    double driverDistance=0.0;
    private void checkNearByDriverDistance() {

        try{
            if(driverList.size()!=0) {
                driverDistance = Double.parseDouble(driverList.get(0).getDistance());
                for (GetDriverInfo info : driverList) {
                    double distance = Double.valueOf(info.getDistance());
                    if (distance <= driverDistance) {
                        driverDistance =distance;
                    }
                }
                double time = (driverDistance* 60.0f / 40.0f);
                String timeSt=String.valueOf(time);
                int timegap=Integer.parseInt(timeSt.split("\\.")[0]);
                timer.setText(timegap<=1 ?"1\nmin": "" +timegap+ "\nmin" );
            }
        }catch (Exception e){
          System.out.print(""+e);
        }
    }

    boolean isZoomSet = false;


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        //TODO
        mMap.setMyLocationEnabled(true);
        if (mMap != null) {

            markerOptionsdes = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.no_image_row)));
            markerDriver = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.no_image_row)));
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    // TODO Auto-generated method stub

                    // mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));

                    //load the traffic now
                    // mMap.setTrafficEnabled(true);
                }
            });

        }

//        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker arg0) {
//                // TODO Auto-generated method stub
//                Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public void onMarkerDragEnd(Marker arg0) {
//                // TODO Auto-generated method stub
//                Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
//
//                double scrollLat = arg0.getPosition().latitude;
//                double scrollLng = arg0.getPosition().longitude;
//
//                pickLat = "" + (float) scrollLat;
//                pickLng = "" + (float) scrollLng;
//                LatLng hcmus = new LatLng(scrollLat, scrollLng);
//
//
//                Location mCurrentLocation = new Location("Current Loc");
//                mCurrentLocation.setLatitude(scrollLat);
//                mCurrentLocation.setLongitude(scrollLng);
//
//
//                try {
//                    double distance = lastLoctionNearBy.distanceTo(mCurrentLocation);
//                    if (distance >= 50) {
//                        if (!isLocationUdated) {
//                            isLocationUdated = true;
//                            updateUserLatLng(latitude, longitude);
//                        }
//                        if (catagorySt.equals(null) || catagorySt.equals("")) {
//                            getNearByDriver(catagorySt, scrollLat, scrollLng,false);
//                        } else {
//                            //getNearByDriver("1", scrollLat, scrollLng,false);
//                        }
//
//                        istripStart = false;
//                        addressLat = scrollLat;
//                        addressLng = scrollLng;
//                        onButtonClicked();
//
//                        //   new GeocodeAsyncTask().execute();
//
//                        //LocationAddress locationAddress = new LocationAddress();
//                        //locationAddress.getAddressFromLocation(scrollLat,scrollLng, getApplicationContext(), new GeocoderHandler());
//                    }
//                } catch (Exception e) {
//                    if (catagorySt.equals(null) || catagorySt.equals("")) {
//                        getNearByDriver(catagorySt, scrollLat, scrollLng,false);
//                    } else {
//                        getNearByDriver("1", scrollLat, scrollLng,false);
//                    }
//                    istripStart = false;
//                    addressLat = scrollLat;
//                    addressLng = scrollLng;
//                    onButtonClicked();
//
//
//                    // new GeocodeAsyncTask().execute();
//                      /* LocationAddress locationAddress = new LocationAddress();
//                       locationAddress.getAddressFromLocation(scrollLat,scrollLng, getApplicationContext(), new GeocoderHandler());*/
//                }
//
//
//                lastLoctionNearBy = new Location(mCurrentLocation);
//
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
//            }
//
//            @Override
//            public void onMarkerDrag(Marker arg0) {
//                // TODO Auto-generated method stub
//                Log.i("System out", "onMarkerDrag...");
//            }
//        });

//Don't forget to Set draggable(true) to marker, if this not set marker does not drag.

        // mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).icon(BitmapDescriptorFactory .fromResource(R.drawable.carred)).draggable(true));

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng scrollPosition = mMap.getCameraPosition().target;
                // Toast.makeText(MainScreenActivity.this,""+scrollPosition,Toast.LENGTH_LONG).show();

                double scrollLat = scrollPosition.latitude;
                double scrollLng = scrollPosition.longitude;
                LatLng hcmus = new LatLng(scrollLat, scrollLng);

                if(isRouteNotDraw){
                    pickLat = "" + (float) scrollLat;
                    pickLng = "" + (float) scrollLng;
                }
                Location mCurrentLocation = new Location("Current Loc");
                mCurrentLocation.setLatitude(scrollLat);
                mCurrentLocation.setLongitude(scrollLng);


                try {
                    double distance = lastLoctionNearBy.distanceTo(mCurrentLocation);
                    if (distance >= 50) {
                        if (!isLocationUdated) {
                            isLocationUdated = true;
                            if(isRouteNotDraw)
                                updateUserLatLng(latitude, longitude);
                        }

                        istripStart = false;
                        addressLat = scrollLat;
                        addressLng = scrollLng;
                        onButtonClicked();
                    }
                } catch (Exception e) {
                    istripStart = false;
                    addressLat = scrollLat;
                    addressLng = scrollLng;
                    onButtonClicked();

                }


                lastLoctionNearBy = new Location(mCurrentLocation);
                if (scrollLat == 0.0 || scrollLng == 0.0) {
                    isZoomSet = false;
                } else {
                    if (!isZoomSet&&isRouteNotDraw) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 16));
                        isZoomSet = true;
                        centerMarker.setText(R.string.please_wait);
                        addressLat = scrollLat;
                        addressLng = scrollLng;
                        onButtonClicked();

                    }
                }

                mCurrentLocation.setLatitude(scrollLat);
                mCurrentLocation.setLongitude(scrollLng);

                if (!isLocationUdated) {
                    isLocationUdated = true;
                    updateUserLatLng(latitude, longitude);
                    istripStart = false;
                }

                try {
                    double distance = lastLoctionNearBy.distanceTo(mCurrentLocation);
                    if (distance >= 50) {
                        if (!isLocationUdated) {
                            isLocationUdated = true;
                            updateUserLatLng(latitude, longitude);
                        }
                        istripStart = false;
                }
                } catch (Exception e) {
                    istripStart = false;
                }

                lastLoctionNearBy = new Location(mCurrentLocation);

            }
        });


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(statusOfGPS){
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                if (isRouteNotDraw) {
                    LatLng hcmus = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 16));
                }


                pickLat = "" + latitude;
                pickLng = "" + longitude;

                if (GrepixUtils.net_connection_check(MainScreenActivity.this)) {
                    isRefreshDrivers = false;
                    getNearByDriver(catagorySt, latitude, longitude, false);

                }
            }
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                GrepixUtils.showSettingsAlert(MainScreenActivity.this);
            }


        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        Location mCurrentLocation = location;

        latitude = mCurrentLocation.getLatitude();
        longitude = mCurrentLocation.getLongitude();

        LatLng hcmus = new LatLng(latitude, longitude);
        if (!isLocationUdated) {
            isLocationUdated = true;
            updateUserLatLng(latitude, longitude);
        } else {
            double distance = lastLoction.distanceTo(mCurrentLocation);
            if (distance >= 100) {
                if (!isLocationUdated) {
                    isLocationUdated = true;
                    updateUserLatLng(latitude, longitude);
                }

            }

        }
        lastLoction = location;

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


    boolean isRouteNotDraw=true;

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        progressDialog.setVisibility(View.GONE);

        if (isdialoglist) {
            for (Route route : routes) {

                calculateFare(catList,selectedIndex);

            }
        } else {
            timeText.setVisibility(View.VISIBLE);
            polylinePaths = new ArrayList<>();
            originMarkers = new ArrayList<>();
            destinationMarkers = new ArrayList<>();

            isRouteNotDraw=false;



            for (Route route : routes) {
                distanceCover=route.distance.value;
                minuts=route.duration.value/60;
                calculateFare(catList,selectedIndex);

                dropLat = "" + (float) route.endLocation.latitude;
                dropLng = "" + (float) route.endLocation.longitude;
                searchedResult = "" + route.endAddress;
                if (markerOptionsdes != null)
                    markerOptionsdes.remove();
                pickupLayout.setVisibility(View.VISIBLE);

                if(!isButtonShowing){
                    price_calculator.setVisibility(View.VISIBLE);
                    fare_details_button.setVisibility(View.GONE);
                    fare_details_layout.setVisibility(View.GONE);
                    isButtonShowing=false;
                }else{
                    price_calculator.setVisibility(View.GONE);
                    fare_details_button.setVisibility(View.VISIBLE);
                    fare_details_layout.setVisibility(View.VISIBLE);
                    isButtonShowing=true;
                }

                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_15))
                        .title(route.startAddress)
                        .position(route.startLocation)));

                destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(route.endAddress)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_15))
                        .position(route.endLocation)));

                ivLocation.setVisibility(View.GONE);
                isUpdatedLocation = false;
                ArrayList<LatLng> latLng = new ArrayList<>();
                latLng.add(route.startLocation);
                 PolylineOptions polylineOptions = new PolylineOptions().
                        geodesic(true).
                        color(Color.BLUE).
                        width(8).zIndex(4);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int i = 0; i < route.points.size(); i++) {
                    polylineOptions.add(route.points.get(i));
                    //the include method will calculate the min and max bound.
                    builder.include(route.points.get(i));
                }

                try{
                    LatLngBounds bounds = builder.build();

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;
                    int padding = (int) (width * 0.40); // offset from edges of the map 10% of screen

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                    mMap.animateCamera(cu);




                }catch (Exception e){

                }

                polylinePaths.add(mMap.addPolyline(polylineOptions));

            }

        }
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {


    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(String s) {
        // searchBtn.setVisibility(View.VISIBLE);
        Log.d("PLACES RESULT:::", s);
        Gson gson = new Gson();
        predictions = gson.fromJson(s, PlacePredictions.class);

        if (mAutoCompleteAdapter == null) {
            mAutoCompleteAdapter = new AutoCompleteAdapter(this, predictions.getPlaces(), MainScreenActivity.this);
            if (isdialoglist) {
                dialogautoCompleteList.setAdapter(mAutoCompleteAdapter);
            } else {
                mAutoCompleteList.setAdapter(mAutoCompleteAdapter);
            }

        } else {
            mAutoCompleteAdapter.clear();
            mAutoCompleteAdapter.addAll(predictions.getPlaces());
            mAutoCompleteAdapter.notifyDataSetChanged();
            if (isdialoglist) {
                dialogautoCompleteList.invalidate();
            } else {
                mAutoCompleteList.invalidate();
            }

        }
    }


    private void fatchAddress(final String result) {
         String url = "https://maps.google.com/maps/api/geocode/json?latlng=" +result;
        url = url.replaceAll(" ","%20");
        Map<String,String> params = new HashMap<String, String>();
        WebService.excuteRequest(this, params,url, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                isCalling = false;
                if (isUpdate) {
                    if (data!= null) {
                        try {
                            JSONObject result=new JSONObject(data.toString());
                            if( result.has("results") ){

                                JSONArray array = result.getJSONArray("results");
                                if( array.length() > 0 ){
                                    JSONObject place1 = array.getJSONObject(0);
                                    String completeAddress=place1.optString("formatted_address");

                                    if(completeAddress.equals(null)||completeAddress.equals("")) {
                                        if( array.length() > 0 ){
                                            JSONObject place = array.getJSONObject(0);
                                            JSONArray components = place.getJSONArray("address_components");
                                            for( int i = 0 ; i < components.length() ; i++ ){
                                                JSONObject component = components.getJSONObject(i);
                                                JSONArray types = component.getJSONArray("types");
                                                for( int j = 0 ; j < types.length() ; j ++ ){
                                                    if( types.getString(j).equals("locality") ){
                                                        centerMarker.setText(component.getString("long_name"));
                                                        locationAddress = component.getString("long_name");
                                                    }
                                                }
                                            }
                                        }
                                    }else{
                                        centerMarker.setText(completeAddress);
                                        locationAddress =completeAddress;
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            centerMarker.setText(getResources().getString(R.string.sorry_internet_connection_is_too_weak));
                            locationAddress = "";
                            fatchAddress(result);
                        }

                    }else{
                        centerMarker.setText(getResources().getString(R.string.sorry_internet_connection_is_too_weak));
                        locationAddress = "";
                        fatchAddress(result);
                    }
                } else {
                    if (error != null) {
                        centerMarker.setText(getResources().getString(R.string.sorry_internet_connection_is_too_weak));
                        locationAddress = "";
                        fatchAddress(result);
                    }
                }
            }
        });

    }

    public void onButtonClicked() {
       if (isUpdatedLocation) {

           fatchAddress(addressLat+","+addressLng);
        }
        if (catagorySt==null || (catagorySt!=null&& catagorySt.equals(""))) {
        } else {
            if(isRouteNotDraw) {
                isRefreshDrivers = false;
                callDriverApi(catagorySt,false, addressLat, addressLng);
            }
        }
    }
    class GeocodeAsyncTask extends AsyncTask<Void, Void, String> {

        String errorMessage = "Please wait....";
        String addressName = "";



        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... none) {
            Geocoder geocoder = new Geocoder(MainScreenActivity.this, Locale.getDefault());
            List<Address> addresses = null;

            double latitude = addressLat;
            double longitude = addressLng;
            try {
                addressName = helperMethods.getAddressFromLatLong(addressLat, addressLng);
                if (addressName.equals(null) || addressName.equals("")) {

                    addresses = geocoder.getFromLocation(latitude, longitude, 5);


                    if (addresses != null && addresses.size() > 0) {

                        for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                            addressName += "  " + addresses.get(0).getAddressLine(i);
                        }
                        return addressName;
                    }
                } else {
                    return addressName;
                }
            } catch (IOException ioException) {
                errorMessage = getString(R.string.sorry_internet_connection_is_too_weak);
                onButtonClicked();
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                errorMessage = getString(R.string.sorry_internet_connection_is_too_weak);
                onButtonClicked();
                Log.e(TAG, errorMessage + ". " +
                        getString(R.string.latitude) + latitude + getString(R.string.longitude) +
                        longitude, illegalArgumentException);
            } catch (Exception Exception) {
                errorMessage = getString(R.string.sorry_internet_connection_is_too_weak);
                onButtonClicked();
                Log.e(TAG, errorMessage + ". " +
                        getString(R.string.latitude) + latitude + getString(R.string.longitude) +
                        longitude, Exception);
            }

            return errorMessage;
        }

        protected void onPostExecute(String address) {
            if (address.equals(null) || address.equals("")) {
                centerMarker.setText(errorMessage);
                locationAddress = "";
            } else {
                centerMarker.setText(address);
                locationAddress = address;
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void getConstantApi() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", controller.pref.getUserApiKey());
        WebService.excuteRequest(this, params,Constants.Urls.GET_CONSTANTS_API, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                isCalling = false;
                if (isUpdate) {
                    ParseJson obj = new ParseJson(MainScreenActivity.this);
                    constantsList = obj.driverConstantParseApi(data.toString());
                    controller.setConstantsList(constantsList);
                    getcategory();
                } else {
                    if (error == null) {
                        Toast.makeText(MainScreenActivity.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}

