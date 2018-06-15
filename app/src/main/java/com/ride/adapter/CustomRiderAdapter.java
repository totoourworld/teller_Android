package com.ride.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app_controller.AppController;
import com.rider.xenia.Controller;
import com.rider.xenia.R;
import com.rider.xenia.TripDetailActivity;
import com.utils.Catagories;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;
import com.utils.TripHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CustomRiderAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<TripHistory> movieItems;
    String userid = "";
    Controller controller;
    String riderid, driverid;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomRiderAdapter(Activity activity, List<TripHistory> movieItems) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        userid = sharedPreferences.getString("userid", null);
        System.out.println("userid in shared preferences in custom rider adapter page" + userid);
        this.activity = activity;
        controller= (Controller) activity.getApplicationContext();
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.review_list, parent, false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();


        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.icon);

        RelativeLayout review = (RelativeLayout) convertView.findViewById(R.id.revi);
        TextView pickup = (TextView) convertView.findViewById(R.id.pickup_location);
        TextView dropup = (TextView) convertView.findViewById(R.id.drop_location);
        TextView driver = (TextView) convertView.findViewById(R.id.textView5);
        TextView fare = (TextView) convertView.findViewById(R.id.textView7);
        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);


        TextView name = (TextView) convertView.findViewById(R.id.name);

        TextView amount = (TextView) convertView.findViewById(R.id.amount2);

        TextView pickup1 = (TextView) convertView.findViewById(R.id.detailpickup1);
        TextView drop = (TextView) convertView.findViewById(R.id.detaildrop1);
        TextView distance = (TextView) convertView.findViewById(R.id.detaildistance1);
        TextView tvCarName = (TextView) convertView.findViewById(R.id.car_name);
        ImageView ivCarIcon = (ImageView) convertView.findViewById(R.id.car_icon);
        // getting movie data for the row


        final TripHistory m = movieItems.get(position);

        riderid = m.getUserId();
        driverid = m.getUserId();

        final DriverInfo driverdetails = m.getDriver();
        pickup.setText(m.getTripToLoc() );
        dropup.setText(m.getTripFromLoc());

        ArrayList<Catagories> catList = new ParseJson(activity).getCatgory(controller.pref.getCatagoryResponce());
        String catgoryName = "";
        for(Catagories catagories:catList){
            if(catagories.getCategory_id().equals(m.getDriver().getCategory_id())){
                catgoryName=catagories.getCat_name();
            }
        };
        tvCarName.setText(catgoryName);

        switch (Integer.parseInt(m.getDriver().getCategory_id())){
            case 1:
                //tvCarName.setText("Hatchback");
                ivCarIcon.setImageResource(R.drawable.hatchback_icon);
                break;
            case 2:
                //  tvCarName.setText("Sedan");
                ivCarIcon.setImageResource(R.drawable.sedan_icon);
                break;
            case 3:
                // tvCarName.setText("SUV");
                ivCarIcon.setImageResource(R.drawable.suv_icon);
                break;
        }


        name.setText(driverdetails.getUName());

        String tripDate = m.getTripCreated();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {

            SimpleDateFormat inputFormat = new SimpleDateFormat
                    ("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            // Adjust locale and zone appropriately
            Date date1 = inputFormat.parse(tripDate);
            String outputText = outputFormat.format(date1);
            System.out.println(outputText);
           /* Date date = dateFormat.parse(tripDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");*/
            timestamp.setText(outputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        amount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(m.getTripPayAmount())));

        String img = driverdetails.getUProfileImagePath();
        if(img.equals(null)||img.equals("")){
            thumbNail.setImageResource(R.drawable.circleuser);
            // thumbNail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            thumbNail.setImageUrl(Constants.Urls.IMAGE_BASE_URL + driverdetails.getUProfileImagePath(), imageLoader);

            // thumbNail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }



        review.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent can = new Intent(activity, TripDetailActivity.class);
                can.putExtra("userid", userid);
                can.putExtra("drivername", driverdetails.getUFname()+" "+driverdetails.getULname());
                can.putExtra("time", m.getTripCreated());
                can.putExtra("trip_status", m.getTripStatus());
                can.putExtra("amount", m.getTripPayAmount());
                can.putExtra("promo_amount", m.getTripPromoAmt());
                can.putExtra("tax_amount", m.getTaxAmount());
                can.putExtra("pickup", m.getTripFromLoc());
                can.putExtra("drop", m.getTripToLoc());
                can.putExtra("distance", m.getTripDistance());
                can.putExtra("date",m.getTripCreated());
                can.putExtra("pic_lat", m.getTripScheduledPickLat());
                can.putExtra("pic_lan", m.getTripScheduledPickLng());
                can.putExtra("drop_lat", m.getTripScheduledDropLat());
                can.putExtra("drop_lan", m.getTripScheduledDropLng());
                can.putExtra("car_name",m.getDriver().getCar_name());
                can.putExtra("cat_id",m.getDriver().getCategory_id());
                can.putExtra("user_image",m.getDriver().getD_profile_image_path());

                activity.startActivity(can);
            }
        });
        return convertView;
    }

}