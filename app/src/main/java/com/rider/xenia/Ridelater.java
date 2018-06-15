package com.rider.xenia;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class Ridelater extends FragmentActivity implements OnClickListener {

    String fcar,fcategory;
    private ProgressDialog pDialog;
    AutoCompleteTextView atvPlaces,atvPlaces1;
    Button scancel,next;
    ImageView clear,clear1;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = Allrides.class.getSimpleName();

    PlacesTask placesTask;
    ParserTask parserTask;
    PlacesTask1 placesTask1;
    ParserTask1 parserTask1;
    String User_id,fbuserproimg,WhoLogin,checkpassword,location,pickupaddress,desinationbutton,gpscountry,destcountry;
    private String prefkey;
    String gpscountry1;
    String pickup,pickupdate,pickuptime;
    String accept,Liveurl,Liveurl1,status;
    String lat,lng,driverdetailsuserid,category,desinationbuttonaction;
    Spinner categorylist,carlist;
    String[] category1,car;
    private JSONArray login_jsonarray;
    private JSONObject login_jsonobj;
    String newcar=null;
    String cartype,login_status;
    String seletedcategory,drop;
    private InputMethodManager mIMEMgr;
    EditText addcar;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView,timeview;
    private int year, month, day;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 1111;
    static final int DATE_DIALOG_ID = 2222;
    String aTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ridelater);
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

        User_id= i.getStringExtra("userid");
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
        Liveurl1 = sharedPreferences.getString("liveurl1", null);
        Liveurl=sharedPreferences.getString("liveurl",null);
        SharedPreferences settings = getSharedPreferences(SigninActivity.PREFS_NAME,1);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean trLoggedIn = settings.getBoolean("trLoggedIn", false);
        mIMEMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        gpscountry1 = settings.getString("gpscountry",prefkey);
        System.out.println("userid in rider later=="+User_id);
        categorylist = (Spinner)findViewById(R.id.categorylist);
        carlist=(Spinner)findViewById(R.id.carlist);
        addcar=(EditText)findViewById(R.id.addcar);
        ////carrrrrrrrrr///



        getcategory();

        categorylist.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        mIMEMgr.hideSoftInputFromWindow(findViewById(R.id.addcar).getWindowToken(), 0);
                        //((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);

                        int position = categorylist.getSelectedItemPosition();
                        seletedcategory=category1[+position];
                        fcategory=seletedcategory;
                        if(position!=0)
                        {
                            getcar();
                            carlist.setVisibility(View.VISIBLE);

                        }
                        else if(seletedcategory.equals("Select your car category"))
                        {
                            carlist.setVisibility(View.GONE);
                            addcar.setVisibility(View.GONE);
                            mIMEMgr.hideSoftInputFromWindow(findViewById(R.id.addcar).getWindowToken(), 0);
                            seletedcategory=null;

                        }
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub


                    }

                }
        );

        carlist.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        mIMEMgr.hideSoftInputFromWindow(findViewById(R.id.addcar).getWindowToken(), 0);

                        int position = carlist.getSelectedItemPosition();

                        cartype=car[+position];
                        fcar=cartype;
                        if(position==0)
                        {

                            cartype=null;
                            newcar=null;
                            addcar.setVisibility(View.GONE);
                            mIMEMgr.hideSoftInputFromWindow(findViewById(R.id.addcar).getWindowToken(), 0);
                        }
                        else if(cartype=="Other")
                        {

                            addcar.setVisibility(View.VISIBLE);
                            addcar.setFocusableInTouchMode(true);
                            addcar.requestFocus();
                            mIMEMgr.showSoftInput(findViewById(R.id.addcar), 0);

                        }
                        else
                        {
                            addcar.setVisibility(View.GONE);
                            mIMEMgr.hideSoftInputFromWindow(findViewById(R.id.addcar).getWindowToken(), 0);
                            newcar=null;
                        }
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub


                    }

                }
        );



        dateView = (TextView) findViewById(R.id.txtselectdate);
        dateView.setOnClickListener(this);
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);




        //call next button to insert data base
        next=(Button)findViewById(R.id.promo);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                pickup=atvPlaces.getText().toString();
                pickupdate=dateView.getText().toString();
                pickuptime=timeview.getText().toString();
                drop=atvPlaces1.getText().toString();
                System.out.println("pickup location"+pickup);
                System.out.println("pickupdate"+pickupdate);
                System.out.println("pickuptime"+pickuptime);
                System.out.println("categoruy=="+fcategory);

                System.out.println("car=="+fcar);
                System.out.println("dropup=="+drop);

                showdialog1();

            }
        });

        /********* display current time on screen Start ********/



        /********* display current time on screen End ********/

        // Add Button Click Listener
        addButtonClickListener();

        desinationbutton="enterdesnation";
        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);

        atvPlaces.setThreshold(1);

        clear=(ImageView)findViewById(R.id.clear);
        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("clearr avvtplaes");

                atvPlaces.setText("");
            }
        });
        atvPlaces1 = (AutoCompleteTextView) findViewById(R.id.atv_places1);

        atvPlaces1.setThreshold(1);

        clear1=(ImageView)findViewById(R.id.clear1);
        clear1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                atvPlaces1.setText("");
                String abc = null;
                System.out.println("india" +abc);
            }
        });
        clear1=(ImageView)findViewById(R.id.clear1);
        clear1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                atvPlaces1.setText("");
            }
        });
        scancel=(Button)findViewById(R.id.cancelp);
        scancel.setOnClickListener(new OnClickListener() {

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
                if(net_connection_check()) {
                    placesTask = new PlacesTask();
                    placesTask.execute(s.toString());
                }
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



        atvPlaces1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(net_connection_check()) {
                    placesTask1 = new PlacesTask1();
                    placesTask1.execute(s.toString());
                }
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






    private void showDate(int year, int month, int day) {

        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

    }

    public void addButtonClickListener() {

        timeview = (TextView) findViewById(R.id.txtselecttime);

        timeview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                // Current Hour
               if(aTime==null) {
                   hour = c.get(Calendar.HOUR_OF_DAY);
                   // Current Minute
                   minute = c.get(Calendar.MINUTE);
                   // set current time into output textview
                   updateTime(hour, minute);
               }

                showDialog(TIME_DIALOG_ID);

            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                TimePickerDialog dialog = new TimePickerDialog(this, timePickerListener, hour, minute, false);
                return dialog;

            case DATE_DIALOG_ID:


        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            pickupdate=dateView.getText().toString();
            System.out.println("pcikup dateee"+pickupdate);
            DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
            Calendar cal = Calendar.getInstance();
            String dd=dateFormat.format(cal.getTime());
            System.out.println("current date ffff="+dd);

            if(pickupdate.equals(dd)||pickupdate==dd)
            {
                System.out.println("equal");
                if (hourOfDay < hour || (hourOfDay == hour && minutes < minute)){
                    System.out.println("equalnot valid");
                    Toast.makeText(getApplicationContext(), R.string.please_select_valid_time, Toast.LENGTH_LONG).show();
                }
                else
                {
                    hour   = hourOfDay;
                    minute = minutes;

                    updateTime(hour,minute);
                }
            }
            else
            {
                hour   = hourOfDay;
                minute = minutes;

                updateTime(hour,minute);
            }
        }

    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        timeview.setText(aTime);
        System.out.println("update timeeeeeeeeee");



    }
    //for google place text
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


                    if(desinationbutton=="enterdesination"||desinationbutton.equals("enterdesination"))
                    {
                        System.out.println("Selected ITem In the List is"+hm.get("description"));

                        location=hm.get("description");


                        StringTokenizer tokens = new StringTokenizer(location, ",");


                        while (tokens.hasMoreElements()) {
                            destcountry=(String) tokens.nextElement();


                        }







                    }

                }
            };
            atvPlaces.setOnItemClickListener(itemClickListener);


            atvPlaces.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            /** Setting the adapter to the listView */

        }
    }
    ///destination
    /** A method to download json data from url */
    private String downloadUrl1(String strUrl) throws IOException{
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
    private class PlacesTask1 extends AsyncTask<String, Void, String>{

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
                data = downloadUrl1(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask1 = new ParserTask1();

            // Starting Parsing the JSON string returned by Web Service
            parserTask1.execute(result);
        }
    }


    /** A class to parse the Google Places in JSON format */
    private class ParserTask1 extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

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
            final SimpleAdapter adapter1 = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

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

                    if(desinationbutton=="enterdesination"||desinationbutton.equals("enterdesination"))
                    {
                        System.out.println("Selected ITem In the List is"+hm.get("description"));

                        location=hm.get("description");


                        StringTokenizer tokens = new StringTokenizer(location, ",");


                        while (tokens.hasMoreElements()) {
                            destcountry=(String) tokens.nextElement();


                        }







                    }

                }
            };
            atvPlaces1.setOnItemClickListener(itemClickListener);


            atvPlaces1.setAdapter(adapter1);
            /** Setting the adapter to the listView */
            adapter1.notifyDataSetChanged();
        }
    }


    ///end destinatio
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void onBackPressed()
    {
        Intent can=new Intent(getApplicationContext(),SlideMainActivity.class);
        can.putExtra("userid", User_id);
        can.putExtra("fbuserproimg",fbuserproimg);
        can.putExtra("whologin",WhoLogin);
        can.putExtra("password",checkpassword);
        can.putExtra("location",location);
        can.putExtra("accept",accept);
        can.putExtra("desinationbutton", desinationbutton);
        can.putExtra("address",pickupaddress );
        can.putExtra("gpscountry", gpscountry);
        if (Build.VERSION.SDK_INT >= 16) {
            Bundle bndlanimation =ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
            startActivity(can,bndlanimation);
        }
        else
        {
            startActivity(can);
        }

        finish();
    }
    private void callwebservice()
    {

        pickup=atvPlaces.getText().toString();
        pickup=	pickup.replaceAll(" ", "%20");
        drop=atvPlaces1.getText().toString();
        drop=drop.replaceAll(" ", "%20");
        System.out.println("drop====="+drop);
        pickupdate=dateView.getText().toString();
        pickuptime=timeview.getText().toString();
        pickuptime=pickuptime.replaceAll(" ", "%20");
        fcar=fcar.replaceAll(" ", "%20");
        String time =  pickuptime;
        String out="";
        SimpleDateFormat dateFormat = new SimpleDateFormat("hmmaa");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
        try {
            Date date = dateFormat.parse(time);

            out= dateFormat2.format(date);
            Log.e("Time", out);
        } catch (ParseException e) {
        }



        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate;
        String date1="";
        try {
            currentDate = sdf.parse(pickupdate);
            date1=sdf.format(currentDate);
            date1=date1.replace("/","-");
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println("Current Date and Time"+date1);

        System.out.println("pickup location"+pickup);
        System.out.println("pickupdate"+pickupdate);
        System.out.println("pickuptime"+pickuptime);

        final String url=Liveurl+"futurebook?userid="+User_id+"&pickup="+pickup+"&pickupdate="+date1+"&pickuptime="+pickuptime+"&carcategory="+fcategory+"&carname="+fcar+"&dropup="+drop;


        //*******************************************ListView code start*****************************************************
        System.out.println("url in allrides page==="+url);

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {


                                JSONObject obj = response.getJSONObject(i);
                                System.out.println("inseide  for loop");
                                status=obj.getString("status");
                                if(status.matches("success"))
                                {
                                    Toast.makeText(getApplicationContext(), "saved success", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "saved fail", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        hidePDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NoConnectionError) {
                    System.out.println("No internet connection");
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.there_is_no_connection, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 70);
                    toast.show();
                }
                VolleyLog.d(TAG, R.string.error + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

        System.out.println("after loop listview view ");
    }
    private void callwebservice1()
    {

        pickup=atvPlaces.getText().toString();
        pickup=	pickup.replaceAll(" ", "%20");
        drop=atvPlaces1.getText().toString();
        drop=drop.replaceAll(" ", "%20");
        System.out.println("drop====="+drop);
        fcar=fcar.replaceAll(" ", "%20");
        pickupdate=dateView.getText().toString();
        pickuptime=timeview.getText().toString();
        pickuptime=pickuptime.replaceAll(" ", "%20");
        String time =  pickuptime;
        String out="";
        SimpleDateFormat dateFormat = new SimpleDateFormat("hmmaa");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
        try {
            Date date = dateFormat.parse(time);

            out= dateFormat2.format(date);
            Log.e("Time", out);
        } catch (ParseException e) {
        }



        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate;
        String date1="";
        try {
            currentDate = sdf.parse(pickupdate);
            date1=sdf.format(currentDate);
            date1=date1.replace("/","-");

        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println("Current Date and Time"+date1);

        System.out.println("pickup location"+pickup);
        System.out.println("pickupdate"+pickupdate);
        System.out.println("pickuptime"+pickuptime);

        final String url=Liveurl+"futurebookcopy?userid="+User_id+"&pickup="+pickup+"&pickupdate="+date1+"&pickuptime="+pickuptime+"&carcategory="+fcategory+"&carname="+fcar+"&dropup="+drop;

        //*******************************************ListView code start*****************************************************
        System.out.println("url in allrides page==="+url);

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d(TAG, response.toString());
                        //hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {


                                JSONObject obj = response.getJSONObject(i);
                                System.out.println("inseide  for loop");

                                {

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        hidePDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NoConnectionError) {
                    System.out.println("No internet connection");
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.no_network_please_connect, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 70);
                    toast.show();
                }
                VolleyLog.d(TAG, R.string.error + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

        System.out.println("after loop listview view ");
    }
    private void showdialog1()
    {
        final Dialog dialog1 = new Dialog(Ridelater.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.booking_alert);

        Button calladmin= (Button) dialog1.findViewById(R.id.settg_bt_update_email);





        calladmin.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if(!(pickup.equals(""))&&(!pickuptime.equals("SelectTime"))&&(!pickupdate.equals(""))&&(!fcategory.equals("Select your car category"))&&(!fcar.equals("Select your car type"))&&!(drop.equals("")))
                {
                    callwebservice();
                    callwebservice1();

                    Intent can=new Intent(getApplicationContext(),SlideMainActivity.class);
                    can.putExtra("userid", User_id);
                    can.putExtra("fbuserproimg",fbuserproimg);
                    can.putExtra("whologin",WhoLogin);
                    can.putExtra("password",checkpassword);
                    can.putExtra("location",location);
                    can.putExtra("accept",accept);
                    can.putExtra("desinationbutton", desinationbutton);
                    can.putExtra("address",pickupaddress );
                    can.putExtra("gpscountry", gpscountry);
                    if (Build.VERSION.SDK_INT >= 16) {
                        Bundle bndlanimation =ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                        startActivity(can,bndlanimation);
                    }
                    else
                    {
                        startActivity(can);
                    }

                    dialog1.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.select_all_fields,Toast.LENGTH_LONG).show();			}
                dialog1.dismiss();
            }
        });
        Button cancel= (Button) dialog1.findViewById(R.id.settg_bt_update_contact);


        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialog1.show();



    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }

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

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Ridelater.this, android.R.layout.simple_list_item_1, category1);
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
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.there_is_no_connection, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 70);
                    toast.show();
                }
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    public void getcar()
    {
        final String url=Liveurl1+"getcarlist?catname="+seletedcategory;
        System.out.println("URL is"+url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json

                        int n=response.length()+2;
                        System.out.println("The N value is car list"+n);
                        System.out.println("the JSON vakue is car list"+response.length()+1);
                        car=new String[n];
                        System.out.println("Car length"+car.length);

                        for (int i = 0; i < n-1; i++) {
                            try {

                                login_jsonobj = response.getJSONObject(i);
                                login_status= login_jsonobj.getString("carlist");
                                Log.d("OUTPUT IS",login_status);
                                car[0]="Select your car type";
                                car[i+1]=login_status;
                                car[n-1]="Other";

                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Ridelater.this, android.R.layout.simple_list_item_1, car);
                                carlist.setAdapter(adapter1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//
                if(error instanceof NoConnectionError) {
                    System.out.println("No internet connection");
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.no_network_please_connect, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 70);
                    toast.show();
                }
                VolleyLog.d(TAG, R.string.error + error.getMessage());


            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }
    public void onClick(View v) {
        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year1,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox

                        if (year1 < year)
                            view.updateDate(year,month,day);

                        if (monthOfYear < month && year1 == year)
                            view.updateDate(year,month,day);

                        if (dayOfMonth < day && year1 == year && monthOfYear == month)
                            view.updateDate(year,month,day);

                        dateView.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year1);

                    }
                }, year, month, day);
        Date newDate2 = calendar.getTime();
        dpd.getDatePicker().setMinDate(newDate2.getTime());


        dpd.show();
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