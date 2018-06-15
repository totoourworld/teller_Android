package com.rider.xenia;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.utils.Catagories;
import com.utils.Constants;
import com.utils.ParseJson;
import com.utils.TripHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.OnClick;

public class FareReviewActivity extends Activity {
    private TextView pickup, drop, distance,tripid, driverid;
    private TextView date,name,amount,totalAmount,promoAmount;
    private Controller controller;
    TextView tvCarName;
    private Button backButton;
    ImageView ivCarIcon;
    private Double amountDouble, promoAmountDouble;
    private TextView tax;

//
//    private Controller controller;
//    @BindView(R.id.fd_tripid)
//    TextView tripId;
//    @BindView(R.id.fd_driverid)
//    TextView driverId;
//    @BindView(R.id.fd_pickuploc)
//    TextView pickUpLoc;
//    @BindView(R.id.fd_droploc)
//    TextView dropLoc;
//    @BindView(R.id.fd_date)
//    TextView date;
//    @BindView(R.id.fd_distance)
//    TextView distance;
//    @BindView(R.id.fd_total)
//    TextView fareToPay;
//



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fare_summary_activity);
        getActionBar().hide();
        //ButterKnife.bind(this);

        tripid = (TextView) findViewById(R.id.tripid);
        driverid = (TextView) findViewById(R.id.driverid);

        pickup = (TextView) findViewById(R.id.pickuploc);
        drop = (TextView) findViewById(R.id.droploc);
        date = (TextView) findViewById(R.id.date);
        distance = (TextView) findViewById(R.id.distance);
        amount = (TextView) findViewById(R.id.amount);
        promoAmount = (TextView) findViewById(R.id.promo_amount1);
        totalAmount = (TextView) findViewById(R.id.total_amount);
        tvCarName = (TextView) findViewById(R.id.car_name);
        name = (TextView) findViewById(R.id.name);
        tax= (TextView) findViewById(R.id.tax_amount1);
        ivCarIcon = (ImageView) findViewById(R.id.car_icon);
        backButton = (Button) findViewById(R.id.fd_back);
        controller = (Controller) getApplication();
        setFareDetail(controller.getCurrentTrip());
    }

    @OnClick(R.id.fd_back)
    public void goBack(View view) {
        finish();
    }

    public void setFareDetail(final TripHistory currentTrip) {
        if (currentTrip == null) {
            tripid.setText("503");
            return;
        }
        tripid.setText("" + currentTrip.getTripId());
        driverid.setText("" + currentTrip.getDriver().getDriver_id());
        pickup.setText("" +currentTrip.getTripToLoc());
        drop.setText("" +  currentTrip.getTripFromLoc());
        //date.setText("" + currentTrip.getTripCreated());
        name.setText(currentTrip.getDriver().getUFname() + " " + currentTrip.getDriver().getULname());
        //distance.setText(String.format("%.02f km", (Float.parseFloat(currentTrip.getTripDistance().trim()) / 1600.0) * 1000));
        distance.setText(String.format(controller.checkDistanceUnit().equalsIgnoreCase("km")?String.format("%.02f km", (Float.parseFloat(currentTrip.getTripDistance().trim()) )):String.format("%.02f mi", (Float.parseFloat(currentTrip.getTripDistance().trim())*0621371))));

        //fareToPay.setText(String.format("%s", currentTrip.getTripPayAmount()));
        //Change date formet
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {

            SimpleDateFormat inputFormat = new SimpleDateFormat
                    ("yyyy-MM-dd hh:mm:ss", Locale.US);
            inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            // Adjust locale and zone appropriately
            Date date1 = inputFormat.parse(currentTrip.getTripCreated().trim());
            String outputText = outputFormat.format(date1);
            System.out.println(outputText);


            /*Date date_formet = dateFormat.parse(currentTrip.getTripCreated().trim());
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");*/
            date.setText(outputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        ArrayList<Catagories> catList = new ParseJson(FareReviewActivity.this).getCatgory(controller.pref.getCatagoryResponce());
        String catgoryName = "";
        for(Catagories catagories:catList){
            if(catagories.getCategory_id().equals(currentTrip.getDriver().getCategory_id())){
                catgoryName=catagories.getCat_name();
            }
        };

        tvCarName.setText(catgoryName);

        switch (Integer.parseInt(currentTrip.getDriver().getCategory_id())) {
            case 1:
                // tvCarName.setText("Hatchback");
                ivCarIcon.setImageResource(R.drawable.hatchback_icon);
                break;
            case 2:
                //tvCarName.setText("Sedan");
                ivCarIcon.setImageResource(R.drawable.sedan_icon);
                break;
            case 3:
                //tvCarName.setText("SUV");
                ivCarIcon.setImageResource(R.drawable.suv_icon);
                break;
        }

        NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.icon);

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        thumbNail.setImageUrl(Constants.Urls.IMAGE_BASE_URL1 +  currentTrip.getDriver().getD_profile_image_path(), imageLoader);

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions marker = new MarkerOptions();
                LatLng pick = new LatLng(Double.parseDouble(currentTrip.getTripScheduledPickLat()), Double.parseDouble(currentTrip.getTripScheduledPickLng()));
                LatLng drop = new LatLng(Double.parseDouble(currentTrip.getTripScheduledDropLat()), Double.parseDouble(currentTrip.getTripScheduledDropLng()));
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


        amount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(currentTrip.getTripPayAmount().trim())));
        promoAmount.setText(String.format("-"+controller.currencyUnit()+"%.02f", Float.parseFloat(currentTrip.getTripPromoAmt().trim())));
        tax.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(currentTrip.getTaxAmount().trim())));

        //To evaluate Total Amount
        //Convert amount string to double
        amountDouble = Double.valueOf(currentTrip.getTripPayAmount());
        promoAmountDouble = Double.valueOf(currentTrip.getTripPromoAmt());
        Double grandTotal = amountDouble - promoAmountDouble;
        totalAmount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(String.valueOf(grandTotal).trim())));
//        amount.setText("$" + currentTrip.getTripPromoAmt());
//        promoAmount.setText("$" + currentTrip.getTripPromoAmt());


        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

    }
}
