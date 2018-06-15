package com.rider.xenia;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.addressHelper.DriverConstants;
import com.app_controller.AppController;
import com.google.android.gcm.GCMRegistrar;
import com.utils.DriverInfo;
import com.utils.TripHistory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Controller extends AppController {

    private final int MAX_ATTEMPTS = 5;
    private final int BACKOFF_MILLI_SECONDS = 2000;
    private final Random random = new Random();

    public String getApiResponce() {
        return apiResponce;
    }

    public void setApiResponce(String apiResponce) {
        this.apiResponce = apiResponce;
    }

    private String apiResponce;

    void register(final Context context, final String regId) {

        Log.i(Config.TAG, "registering device (regId = " + regId + ")");


        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);

        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {

            Log.d(Config.TAG, "Attempt #" + i + " to register");

            //Send Broadcast to Show message on screen
            displayMessageOnScreen(context, context.getString(
                    R.string.server_registering, i, MAX_ATTEMPTS));

            // Post registration values to web server

            GCMRegistrar.setRegisteredOnServer(context, true);

            //Send Broadcast to Show message on screen
            String message = context.getString(R.string.server_registered);
            displayMessageOnScreen(context, message);

            return;
        }

        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);

        //Send Broadcast to Show message on screen
        displayMessageOnScreen(context, message);
    }

    // Unregister this account/device pair within the server.
    void unregister(final Context context, final String regId) {

        Log.i(Config.TAG, "unregistering device (regId = " + regId + ")");


        GCMRegistrar.setRegisteredOnServer(context, false);
        String message = context.getString(R.string.server_unregistered);
        displayMessageOnScreen(context, message);
    }


    // Checking for all possible internet providers
    public boolean isConnectingToInternet() {

        ConnectivityManager connectivity =
                (ConnectivityManager) getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    // Notifies UI to display a message.
    void displayMessageOnScreen(Context context, String message) {

        Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(Config.EXTRA_MESSAGE, message);
        System.out.println("Message send to Broad Cast from controller" + message);
        // Send Broadcast to Broadcast receiver with message
        context.sendBroadcast(intent);

    }

    public ArrayList<DriverConstants> getConstantsList() {
        return constantsList;
    }

    public void setConstantsList(ArrayList<DriverConstants> constantsList) {
        this.constantsList = constantsList;
    }

    public ArrayList<DriverConstants> constantsList;

    String checkDistanceUnit() {
        String distanceUnit = "";
        for (DriverConstants driverConstants : getConstantsList()) {
            if (driverConstants.getConstant_key().equalsIgnoreCase("distance_paramiter")) {
                distanceUnit = driverConstants.getConstant_value();
            }

        }
        return  distanceUnit;
    }

    public String currencyUnit() {
        String distanceUnit = "";
        for (DriverConstants driverConstants : getConstantsList()) {
            if (driverConstants.getConstant_key().equalsIgnoreCase("currency")) {
                distanceUnit = driverConstants.getConstant_value();
            }

        }
        return  distanceUnit;
    }

    public  String formattedDistance(float distanceInKm)
    {
        return checkDistanceUnit().equalsIgnoreCase("km")?String.format("%.02f km", distanceInKm ):String.format("%.02f mi", distanceInKm*0.621371);
    }

    public void setCurrentTrip(TripHistory trip) {
        this.currentTrip = trip;
    }

    public TripHistory getCurrentTrip() {
        return currentTrip;
    }

    private TripHistory currentTrip;

    public void setUpdateCurrentTrip(DriverInfo driverInfo) {

        if (this.currentTrip != null) {
            this.currentTrip.setDriver(driverInfo);
        }
    }

    JSONObject facebookResponce;

    public void setFacebookResponce(JSONObject facebookResponce) {
        this.facebookResponce= facebookResponce;
    }

    public JSONObject getFacebookResponce(){
        return facebookResponce;
    }
}
