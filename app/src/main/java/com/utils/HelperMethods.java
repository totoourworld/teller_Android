package com.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.rider.xenia.FragmentRouteNavigation;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by devin on 2017-03-29.
 */

public class HelperMethods {
    Context context;
    public HelperMethods(Context context) {
        this.context=context;
    }

    public static void manageTripStatus(Activity context, String tripStatus, BroadcastReceiver mNotificationReceiver) {
        switch(tripStatus){
            case "accept":
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mNotificationReceiver);
                Intent intent=new Intent(context, FragmentRouteNavigation.class);
                context.startActivity(intent);
                context.finish();
                break;
            case "arrive":
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mNotificationReceiver);
                Intent intent0=new Intent(context, FragmentRouteNavigation.class);
                context.startActivity(intent0);
                context.finish();
                break;
            case "begin":
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mNotificationReceiver);
                Intent intent2=new Intent(context, FragmentRouteNavigation.class);
                context.startActivity(intent2);
                context.finish();
                break;
            case "end":
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mNotificationReceiver);
                Intent intent3=new Intent(context, FragmentRouteNavigation.class);
                context.startActivity(intent3);
                context.finish();
                break;
            case "driver_cancel_at_drop":
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mNotificationReceiver);
                Intent intent4=new Intent(context, FragmentRouteNavigation.class);
                context.startActivity(intent4);
                context.finish();
                break;
            default:
                break;
        }



    }

    public String getAddressFromLatLong(double lat, double lon) {

        JSONObject result = getLocationFormGoogle(lat + "," + lon );
        return getCityAddress(result);
    }

    protected static JSONObject getLocationFormGoogle(String placesName) {

        String apiRequest = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + placesName; //+ "&ka&sensor=false"
        HttpGet httpGet = new HttpGet(apiRequest);
        HttpClient client = new DefaultHttpClient();
        org.apache.http.HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return jsonObject;
    }

    protected static String getCityAddress( JSONObject result ){
        if( result.has("results") ){
            try {
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
                                        return component.getString("long_name");
                                    }
                                }
                            }
                        }
                    }else{
                        return completeAddress;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }




}
