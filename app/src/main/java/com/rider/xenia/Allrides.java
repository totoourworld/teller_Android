package com.rider.xenia;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app_controller.AppController;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class Allrides extends Activity {
    String etas,sizes,minfares,price_km,price_min,catname;
    AutoCompleteTextView atvPlaces;
    Button scancel;
    TextView eta,size,minfare,rates,wait;
    ImageView clear;
    public static final String PREFS_NAME = "MyPrefsFile";
    private JSONObject driverdetails_jsonobj;
    private String driverdetailsjson_status;
    PlacesTask placesTask;
    ParserTask parserTask;
    String User_id,fbuserproimg,WhoLogin,checkpassword,location,pickupaddress,desinationbutton,gpscountry,destcountry;
    private String prefkey;
    String gpscountry1;

    String accept;
    String lat,lng,driverdetailsuserid,category,desinationbuttonaction;
    Spinner categorylist;
    String seletedcategory;
    String[] category1;
    private JSONObject login_jsonobj;
    private String login_status;
    String Liveurl="";
    String Liveurl1="";
    private InputMethodManager mIMEMgr;
    String location1="";
    float address2distance,address2mins;
    protected static final String TAG = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allrides);
        getActionBar().hide();
        Intent i= getIntent();
        User_id= i.getStringExtra("userid");
        fbuserproimg=i.getStringExtra("fbuserproimg");
        WhoLogin=i.getStringExtra("whologin");
        checkpassword=i.getStringExtra("password");
        accept=i.getStringExtra("accept");
        desinationbutton=i.getStringExtra("desinationbutton");
        pickupaddress=i.getStringExtra("address");
        gpscountry=i.getStringExtra("gpscountry");


        fbuserproimg=i.getStringExtra("fbuserproimg");
        WhoLogin=i.getStringExtra("whologin");
        System.out.println("Slide Main Activity WhoLogin"+WhoLogin);
        checkpassword=i.getStringExtra("password");

        accept=i.getStringExtra("accept");
        lat=i.getStringExtra("Lat");
        lng=i.getStringExtra("Long");
        driverdetailsuserid=i.getStringExtra("Driverid");
        category=i.getStringExtra("category");

        desinationbuttonaction=i.getStringExtra("desinationbutton");
        pickupaddress=i.getStringExtra("address");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);
        Liveurl1=sharedPreferences.getString("liveurl1", null);

        System.out.println("The Live URL Is "+Liveurl);
        mIMEMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        getcategory();

        categorylist = (Spinner)findViewById(R.id.categorylist);
        minfare=(TextView)findViewById(R.id.textView1);
        rates=(TextView)findViewById(R.id.textView4);
        wait=(TextView)findViewById(R.id.textView6);
        System.out.println("lat===allrides"+lat+lng);
        categorylist.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        ((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
                        int position = categorylist.getSelectedItemPosition();
                        seletedcategory=category1[+position];
                        get_allcategory_details();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub


                    }

                }
        );

        SharedPreferences settings = getSharedPreferences(SigninActivity.PREFS_NAME,1);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean trLoggedIn = settings.getBoolean("trLoggedIn", false);

        gpscountry1 = settings.getString("gpscountry",prefkey);


        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        if(desinationbutton=="fareestimate"||desinationbutton.equals("fareestimate")||desinationbutton=="enterdesination"||desinationbutton.equals("enterdesination"))
        {
            atvPlaces.setHint("Enter your destination");
        }else
        {
            atvPlaces.setText(pickupaddress);
        }
        atvPlaces.setThreshold(1);

        clear=(ImageView)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                atvPlaces.setText("");
                wait.setText("$0.0");
            }
        });

        scancel=(Button)findViewById(R.id.scancel);
        scancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                location="null";
                Intent slide=new Intent(getApplicationContext(),SlideMainActivity.class);
                slide.putExtra("userid", User_id);
                slide.putExtra("fbuserproimg",fbuserproimg);
                System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
                slide.putExtra("whologin",WhoLogin);
                slide.putExtra("password", checkpassword);
                slide.putExtra("accept",accept);
                slide.putExtra("desinationbutton", desinationbutton);
                slide.putExtra("location",location);
                slide.putExtra("address",pickupaddress );
                slide.putExtra("gpscountry", gpscountry);

                System.out.println("Slide Main Activity WhoLogin"+WhoLogin);
                startActivity(slide);
                finish();
            }
        });
        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {


                // TODO Auto-generated method stub
            }


        });


    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";


            String key = "key=AIzaSyCAvQqq2dvTA6ZIuqsADVY4ZiqvzCFOWKI";
            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }


            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from web service in background
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            //System.out.println("The Data is"+data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }


    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }


            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            final SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            OnItemClickListener itemClickListener = new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                    /** Each item in the adapter is a HashMap object.
                     *  So this statement creates the currently clicked hashmap object
                     * */
                    @SuppressWarnings("unchecked")
                    HashMap<String, String> hm = (HashMap<String, String>) arg0.getAdapter().getItem(position);

                    /** Getting a reference to the TextView of the layout file activity_main to set Currency */


                    /**  Getting currency from the HashMap and setting it to the textview */
                    atvPlaces.setText(hm.get("description"));


                    System.out.println("Selected ITem In the List is"+hm.get("description"));

                    location=hm.get("description");

                    StringTokenizer tokens = new StringTokenizer(location, ",");


                    while (tokens.hasMoreElements()) {
                        destcountry=(String) tokens.nextElement();


                    }

                    destcountry=destcountry.replaceAll(" ","");
                    //	gpscountry=gpscountry.replaceAll(" ", "");
                    System.out.println("++++++++++++++++++++++++++++++++++++++");
                    System.out.println("DEST COUNTRY"+destcountry);
                    System.out.println("GPS  COUNTRY"+gpscountry);
                    System.out.println("++++++++++++++++++++++++++++++++++++++");




                }
            };
            atvPlaces.setOnItemClickListener(itemClickListener);


            atvPlaces.setAdapter(adapter);
            /** Setting the adapter to the listView */

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void onBackPressed()
    {
        Intent can=new Intent(getApplicationContext(),SlideMainActivity.class);
        can.putExtra("userid", User_id);
        can.putExtra("fbuserproimg",fbuserproimg);
        can.putExtra("whologin",WhoLogin);
        can.putExtra("password",checkpassword);
        can.putExtra("accept",accept);
        can.putExtra("desinationbutton", desinationbutton);
        can.putExtra("address",pickupaddress );
        can.putExtra("gpscountry", gpscountry);


        startActivity(can);
        finish();
    }

    public void getcategory()
    {
        final String url=Liveurl1+"getcarcategory?userid="+User_id;
        System.out.println("URL is"+url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        category1=new String[response.length()+1];
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                login_jsonobj = response.getJSONObject(i);
                                login_status= login_jsonobj.getString("car");
                                Log.d("OUTPUT IS",login_status);
                                category1[0]="Select your car category";
                                category1[i+1]=login_status;
                                System.out.println("CATEGORY"+category1[i]);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Allrides.this, android.R.layout.simple_spinner_item, category1);
                                categorylist.setAdapter(adapter);
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
    public void estimatefarecalculation()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                float KM=(float)address2distance;
                float min=address2mins;
                float pm,pkm,minfaref;



                pkm=Float.valueOf(price_km);
                minfaref=Float.valueOf(minfares);
                pm=Float.valueOf(price_min);

                float maxamount=minfaref+KM*pkm;
                System.out.println("Maximum  amount"+maxamount);
                System.out.println("KM"+KM);
                System.out.println("MIN"+min);
                System.out.println("Calculate Price per KM"+price_km);
                System.out.println("Calculate Price per MIN"+price_min);
                System.out.println("Calculate Minium Rate"+minfares);

                wait.setText("$"+maxamount);

            }
        }, 1500);
    }

    public void get_allcategory_details()
    {
        final String url=Liveurl+"get_category_details?category="+seletedcategory;
        System.out.println("URL is"+url);
        location1=atvPlaces.getText().toString();
        if(!(TextUtils.isEmpty(location1)))
        {
            location1=location1.replaceAll(" ", "%20");
            System.out.println("enttttttloo");
        }

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                driverdetails_jsonobj = response.getJSONObject(i);
                                driverdetailsjson_status= driverdetails_jsonobj.getString("status");


                                Log.d("OUTPUT IS",driverdetailsjson_status);
                                if(driverdetailsjson_status.matches("Success")){

                                    catname=driverdetails_jsonobj.getString("categoryname");
                                    price_min=driverdetails_jsonobj.getString("price_minute");
                                    price_km=driverdetails_jsonobj.getString("price_km");
                                    sizes=driverdetails_jsonobj.getString("max_size");
                                    minfares=driverdetails_jsonobj.getString("price_fare");

                                    System.out.println("Price per KM"+price_km);
                                    System.out.println("Price per MIN"+price_min);
                                    System.out.println("sizeeees="+sizes);
                                    System.out.println("minifares =="+minfares);
                                    minfare.setText("$"+minfares);
                                    rates.setText(price_km+"/KM");

                                    System.out.println("location======"+location1);

                                    if(!(TextUtils.isEmpty(location1)))
                                    {

                                        getdistance();
                                    }
                                    else
                                    {
                                        address2distance=0;
                                        wait.setText("$0.0");
                                        Toast.makeText(getApplicationContext(), "select your pickup locaion", Toast.LENGTH_LONG).show();
                                    }



                                    System.out.println("User details updated successfully");



                                }

                                else  {

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
    public void getdistance()

    {
        final String url=Liveurl+"viewlong?pickup="+location1+"&lat="+lat+"&lon="+lng+"&unit="+"K";
        System.out.println("URL is"+url);

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                driverdetails_jsonobj = response.getJSONObject(i);
                                driverdetailsjson_status= driverdetails_jsonobj.getString("status");


                                Log.d("OUTPUT IS",driverdetailsjson_status);
                                if(driverdetailsjson_status.matches("Success")){

                                    String address2distance1=driverdetails_jsonobj.getString("distance1");

                                    address2distance=Float.parseFloat(address2distance1);
                                    System.out.println("Distenation addderes from web seer1="+address2distance1);


                                    address2distance1=new DecimalFormat("##.##").format(address2distance);
                                    System.out.println("Distenation addderes from web se22="+address2distance1);
                                    System.out.println("Distenation addderes from web see222="+address2distance);
                                    estimatefarecalculation();
                                    System.out.println("User details updated successfully");

                                }

                                else  {

                                    System.out.println("not calculate in distance updated");

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


    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {
            dialogcalling();
        }
        return connection;
    }

    public void dialogcalling() {
        final Dialog dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar);

        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);

        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
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
}