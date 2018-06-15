package com.rider.xenia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app_controller.AppController;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ride.adapter.Movie;
import com.utils.Catagories;
import com.utils.Constants;
import com.utils.ParseJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class TripDetailActivity extends Activity {

    ImageButton recancel;
    TextView tripempty;
    String status, Liveurl1 = "", Liveurl = "", userid, fbuserproimg, checkpassword, WhoLogin;
    private static final String TAG = TripDetailActivity.class.getSimpleName();
    String name, amount, time, pickup, drop, distance, date, promoAmount,tax;
    private List<Movie> movieList = new ArrayList<Movie>();
    TextView namy, amounty, timy, picky, dropy, disty, promoAmounty, totalAmount;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Double amountDouble, promoAmountDouble;
    private ImageView cancel_icon;
    Controller controller;
    private TextView taxAmount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripdetail);

        controller= (Controller) getApplication();
        getActionBar().hide();
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        recancel = (ImageButton) findViewById(R.id.recancel);
        namy = (TextView) findViewById(R.id.name);
        amounty = (TextView) findViewById(R.id.amount);
        cancel_icon = (ImageView) findViewById(R.id.canceled_icon);
        timy = (TextView) findViewById(R.id.detaildate);
        picky = (TextView) findViewById(R.id.detailpickup1);
        dropy = (TextView) findViewById(R.id.detaildrop1);
        disty = (TextView) findViewById(R.id.detaildistance1);
        tripempty = (TextView) findViewById(R.id.tripempty);
        TextView tvCarName = (TextView) findViewById(R.id.car_name);
        ImageView ivCarIcon = (ImageView) findViewById(R.id.car_icon);
        promoAmounty = (TextView) findViewById(R.id.promo_amount);
        taxAmount = (TextView) findViewById(R.id.tax_amount);
        totalAmount = (TextView) findViewById(R.id.total_amount);

        final Intent i = getIntent();

        if(i.getStringExtra("trip_status").equals("driver_cancel_at_pickup")||i.getStringExtra("trip_status").equals("driver_cancel_at_drop"))
        {
            cancel_icon.setVisibility(View.VISIBLE);
        }else {
            cancel_icon.setVisibility(View.GONE);
        }


        ArrayList<Catagories> catList = new ParseJson(TripDetailActivity.this).getCatgory(controller.pref.getCatagoryResponce());
        String catgoryName = "";
        for(Catagories catagories:catList){
            if(catagories.getCategory_id().equals(i.getStringExtra("cat_id"))){
                catgoryName=catagories.getCat_name();
            }
        };

        tvCarName.setText(catgoryName);

        switch (Integer.parseInt(i.getStringExtra("cat_id"))) {
            case 1:
                // tvCarName.setText("Hatchback");
                ivCarIcon.setImageResource(R.drawable.hatchback_icon);
                break;
            case 2:
                // tvCarName.setText("Sedan");
                ivCarIcon.setImageResource(R.drawable.sedan_icon);
                break;
            case 3:
                // tvCarName.setText("SUV");
                ivCarIcon.setImageResource(R.drawable.suv_icon);
                break;
        }

        NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.icon);

        thumbNail.setImageUrl(Constants.Urls.IMAGE_BASE_URL + i.getStringExtra("user_image"), imageLoader);

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions marker = new MarkerOptions();
                LatLng pick = new LatLng(Double.parseDouble(i.getStringExtra("pic_lat")), Double.parseDouble(i.getStringExtra("pic_lan")));
                LatLng drop = new LatLng(Double.parseDouble(i.getStringExtra("drop_lat")), Double.parseDouble(i.getStringExtra("drop_lan")));
                marker.position(pick).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_15));
                googleMap.addMarker(marker);
                marker.position(drop).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_25));
                googleMap.addMarker(marker);
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                googleMap.getUiSettings().setZoomGesturesEnabled(false);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                //the include method will calculate the min and max bound.
                builder.include(pick);
                builder.include(drop);
                LatLngBounds bounds = builder.build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels / 2;
                int padding = (int) (width * 0.30); // offset from edges of the map 10% of screen
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                googleMap.animateCamera(cu);
            }
        });

        userid = i.getStringExtra("userid");
        name = i.getStringExtra("drivername");
        amount = i.getStringExtra("amount");
        time = i.getStringExtra("time");
        pickup = i.getStringExtra("pickup");
        drop = i.getStringExtra("drop");
        distance = i.getStringExtra("distance");
        date = i.getStringExtra("date");
        promoAmount = i.getStringExtra("promo_amount");
        tax = i.getStringExtra("tax_amount");




        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        try {


            //  String inputText = "Tue May 21 14:32:00 GMT 2012";
            SimpleDateFormat inputFormat = new SimpleDateFormat
                    ("yyyy-MM-dd hh:mm:ss", Locale.US);
            inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            // Adjust locale and zone appropriately
            Date date1 = inputFormat.parse(date.trim());
            String outputText = outputFormat.format(date1);
            System.out.println(outputText);


     /*       Date date_formet = dateFormat.parse(date.trim());
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault());*/
            // dateFormat1.setTimeZone(TimeZone.getTimeZone("GMT"));
            timy.setText(""+outputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        namy.setText(name);
        amounty.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(amount.trim())));
        picky.setText(drop);
        dropy.setText(pickup);
        // disty.setText(String.format("%.02f km", (Float.parseFloat(distance.trim()) / 1600.0) * 1000));
        disty.setText(String.format(controller.checkDistanceUnit().equalsIgnoreCase("km")?String.format("%.02f km", (Float.parseFloat(distance.trim()) )):String.format("%.02f mi", (Float.parseFloat(distance.trim())*0621371))));
        promoAmounty.setText("-"+String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(promoAmount.trim())));
        taxAmount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(tax.trim())));

        //To evaluate Total Amount
        //Convert amount string to double
        amountDouble = Double.valueOf(amount);
        promoAmountDouble = Double.valueOf(promoAmount);
        Double grandTotal = amountDouble - promoAmountDouble;
        totalAmount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(String.valueOf(grandTotal))));

        fbuserproimg = i.getStringExtra("fbuserproimg");
        checkpassword = i.getStringExtra("password");
        System.out.println("Edit Profile Profile_pic" + fbuserproimg);
        WhoLogin = i.getStringExtra("whologin");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);
        Liveurl = sharedPreferences.getString("liveurl", null);

        if (net_connection_check()) {
        }
        recancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
//                // TODO Auto-generated method stub
//                Intent can=new Intent(getApplicationContext(),Myreview.class);
//                can.putExtra("userid", userid);
//                can.putExtra("fbuserproimg",fbuserproimg);
//                can.putExtra("whologin",WhoLogin);
//                can.putExtra("password",checkpassword);
//                System.out.println("Profile Activity Fb Profile imag" + fbuserproimg);
//
//                startActivity(can);
//                finish();
            }
        });

    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
//        Intent can=new Intent(getApplicationContext(),SlideMainActivity.class);
//        can.putExtra("userid", userid);
//        can.putExtra("fbuserproimg",fbuserproimg);
//        can.putExtra("whologin", WhoLogin);
//        can.putExtra("password",checkpassword);
//        System.out.println("Profile Activity Fb Profile imag"+fbuserproimg);
//
//        startActivity(can);
//        finish();
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
