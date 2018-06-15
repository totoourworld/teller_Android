package com.ride.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app_controller.AppController;
import com.android.volley.toolbox.ImageLoader;
import com.rider.xenia.R;

import java.util.List;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CustomeFutureAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie1> movieItems;
    String userid="";
    String riderid;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomeFutureAdapter(Activity activity, List<Movie1> movieItems) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        userid = sharedPreferences.getString("userid",null);
        System.out.println("userid in shared preferences in custom futire adapter page"+userid);
        this.activity = activity;
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
            convertView = inflater.inflate(R.layout.futurelist, parent,false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        TextView pickup = (TextView) convertView.findViewById(R.id.txt2);
        TextView date1 = (TextView) convertView.findViewById(R.id.textView2);
        TextView time1= (TextView) convertView.findViewById(R.id.textView5);
        TextView car= (TextView) convertView.findViewById(R.id.textView7);


        // getting movie data for the row
        final   Movie1 m = movieItems.get(position);

        System.out.println("userby  after getting===="+m.getpickup());

        pickup.setText(m.getpickup());
        date1.setText(m.getdate1());
        time1.setText(m.gettime1());
        car.setText(m.getcarname());
        
      



        return convertView;
    }

}