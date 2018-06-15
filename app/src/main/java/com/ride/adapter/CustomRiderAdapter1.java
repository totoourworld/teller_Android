package com.ride.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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


public class CustomRiderAdapter1 extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    String userid="";
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomRiderAdapter1(Activity activity, List<Movie> movieItems) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        userid = sharedPreferences.getString("userid",null);
        System.out.println("userid in shared preferences in custom rider adapter page"+userid);
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
            convertView = inflater.inflate(R.layout.ridelist, parent,false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView pickup = (TextView) convertView.findViewById(R.id.txt2);
        TextView dropup= (TextView) convertView.findViewById(R.id.textView2);
        TextView driver= (TextView) convertView.findViewById(R.id.textView5);
        TextView fare= (TextView) convertView.findViewById(R.id.textView7);
        TextView carname= (TextView) convertView.findViewById(R.id.textView9);


        final   Movie m = movieItems.get(position);

        pickup.setText(m.getpickup());
        dropup.setText(m.getdrop());
        driver.setText(m.getdrivername());
        fare.setText(m.getamount());
        carname.setText(m.getcarname());

        return convertView;
    }
}
