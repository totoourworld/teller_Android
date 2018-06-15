package com.rider.xenia;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends Activity {

	AutoCompleteTextView atvPlaces;
	Button scancel;
	ImageView clear;
	 public static final String PREFS_NAME = "MyPrefsFile";

	// ListView lv=(ListView)findViewById(R.id.listView1);
	PlacesTask placesTask;
	ParserTask parserTask;
	String User_id,fbuserproimg,WhoLogin,checkpassword,location,pickupaddress,desinationbutton,gpscountry,destcountry;
 	private String prefkey;
 	String gpscountry1=null,regionsearchstatus;
    String googleapikey;
	String accept;
	String lat,lng,driverdetailsuserid,category,desinationbuttonaction;
	Boolean showHandle=false;
	String showhandle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		getActionBar().hide();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        googleapikey = sharedPreferences.getString("googleapikey", null);
		gpscountry1 = sharedPreferences.getString("gpscountry", null);


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
		showhandle=i.getStringExtra("showhandle");
    	
    	if(showhandle!=null)
		{
			showHandle=true;
		}
    	
    	  SharedPreferences settings = getSharedPreferences(SigninActivity.PREFS_NAME,1);
          //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
          boolean trLoggedIn = settings.getBoolean("trLoggedIn", false);
          	              
        regionsearchstatus=settings.getString("regionsearchstatus",prefkey);
    	System.out.println("GPS COUNTRY START"+gpscountry1);
        System.out.println("REGION SEARCH"+regionsearchstatus);
//    	desinationbutton="enterdesnation";
		atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
		if(desinationbutton=="fareestimate"||desinationbutton.equals("fareestimate")||desinationbutton=="enterdesination"||desinationbutton.equals("enterdesination"))
		{
			atvPlaces.setHint(R.string.enter_your_destination);
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
			


            String key="key="+googleapikey;


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
				System.out.println("Place URL "+url);

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
			
			System.out.println("RESULT"+result);
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

			              final String  destaddress=hm.get("description");
							getLocationFromAddress(destaddress);

			                if(desinationbutton=="enterdesination"||desinationbutton.equals("enterdesination"))
			        		{
			                	System.out.println("Selected ITem In the List is"+hm.get("description"));
				                
				                location=hm.get("description");


							/*	StringTokenizer tokens = new StringTokenizer(location, ",");
							     
						    	 
						    		while (tokens.hasMoreElements()) {
							    		destcountry=(String) tokens.nextElement();
							    	

						    		}

						    	 		destcountry=destcountry.replaceAll(" ","");*/
                                if(gpscountry1!=null) {
                                    gpscountry1 = gpscountry1.replaceAll(" ", "");
                                }
							         	System.out.println("++++++++++++++++++++++++++++++++++++++");
						                System.out.println("DEST COUNTRY" + destcountry);
						                System.out.println("GPS  COUNTRY" + gpscountry1);
								System.out.println("++++++++++++++++++++++++++++++++++++++");





									if(destcountry.equals(gpscountry1))
									{

										Intent request=new Intent(getApplicationContext(),RequestActivity.class);
										request.putExtra("userid", User_id);
										request.putExtra("fbuserproimg",fbuserproimg);
										System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
										request.putExtra("whologin",WhoLogin);
										request.putExtra("password", checkpassword);
										request.putExtra("Driverid",driverdetailsuserid);
										System.out.println("Request Function Driver id"+driverdetailsuserid);
										request.putExtra("accept", accept);
										request.putExtra("desinationbutton", desinationbutton);
										request.putExtra("destaddress",destaddress);
										request.putExtra("category",category);
										System.out.println("Slide Main Activity WhoLogin"+WhoLogin);
										System.out.println("Slide Lat "+lat);
										System.out.println("Slide Lng"+lng);
										request.putExtra("Lat", lat);
										request.putExtra("Long", lng);
										request.putExtra("category",category);

										startActivity(request);
										finish();
									}
									else
									{

										location="null";
										android.support.v7.app.AlertDialog.Builder builder =
												new android.support.v7.app.AlertDialog.Builder(SearchActivity.this, R.style.AppCompatAlertDialogStyle);
										builder.setMessage(R.string.your_destination_address_is_not_available);
										builder.setCancelable(false);
										builder.setInverseBackgroundForced(false);

										builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {



											@Override
											public void onClick(DialogInterface dialog, int which) {

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

												System.out.println("Profile Activity Fb Profile imag"+fbuserproimg);
												startActivity(can);
												finish();

											}
										});
										builder.show();
									}


			                	
			        		
			        		}else
			        		{
			                System.out.println("Selected ITem In the List is"+hm.get("description"));
			                
			              location=hm.get("description");

								/*	 StringTokenizer tokens = new StringTokenizer(location, ",");
						        
						 		
					    		while (tokens.hasMoreElements()) {
						    		destcountry=(String) tokens.nextElement();
						    	

					    		}*/

					    	 		destcountry=destcountry.replaceAll(" ","");
					    	 		gpscountry=gpscountry.replaceAll(" ", "");
						         	System.out.println("++++++++++++++++++++++++++++++++++++++");
					                System.out.println("DEST COUNTRY CODE"+destcountry);
					                System.out.println("GPS  COUNTRY CODE"+gpscountry);
						         	System.out.println("++++++++++++++++++++++++++++++++++++++");




								if(showHandle) {

									SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
									SharedPreferences.Editor editor = sharedPreferences.edit();
									//Set "hasLoggedIn" to true
									editor.putString("location", location);
									editor.putString("desinationbutton", desinationbutton);
									editor.putString("address", pickupaddress);
									editor.putString("accept", accept);
									editor.putString("gpscountry", gpscountry);
									editor.putString("showhandle", "showhandle");

									// Commit the edits!
									editor.commit();


									finish();
								}else {

									if (destcountry.equals(gpscountry)) {


										Intent can = new Intent(getApplicationContext(), SlideMainActivity.class);
										can.putExtra("userid", User_id);
										can.putExtra("fbuserproimg", fbuserproimg);
										can.putExtra("whologin", WhoLogin);
										can.putExtra("password", checkpassword);
										can.putExtra("location", location);
										can.putExtra("accept", accept);
										can.putExtra("desinationbutton", desinationbutton);
										can.putExtra("address", pickupaddress);
										can.putExtra("gpscountry", gpscountry);

										System.out.println("Profile Activity Fb Profile imag" + fbuserproimg);
										startActivity(can);
										finish();
									} else {

										location = "null";
										android.support.v7.app.AlertDialog.Builder builder =
												new android.support.v7.app.AlertDialog.Builder(SearchActivity.this, R.style.AppCompatAlertDialogStyle);
										builder.setTitle(R.string.fare_lookup_failed);
										builder.setMessage(R.string.could_you_retrive_fare);
										builder.setCancelable(false);
										builder.setInverseBackgroundForced(false);

										builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {


											@Override
											public void onClick(DialogInterface dialog, int which) {

												Intent can = new Intent(getApplicationContext(), SlideMainActivity.class);
												can.putExtra("userid", User_id);
												can.putExtra("fbuserproimg", fbuserproimg);
												can.putExtra("whologin", WhoLogin);
												can.putExtra("password", checkpassword);
												can.putExtra("location", location);
												can.putExtra("accept", accept);
												can.putExtra("desinationbutton", desinationbutton);
												can.putExtra("address", pickupaddress);
												can.putExtra("gpscountry", gpscountry);

												System.out.println("Profile Activity Fb Profile imag" + fbuserproimg);
												startActivity(can);
												finish();

											}
										});
										builder.show();
									}

								}
			        		}
			                
			            }
			        };
			        atvPlaces.setOnItemClickListener(itemClickListener);
			        
			       atvPlaces.setAdapter(adapter);
            adapter.notifyDataSetChanged();


		}			
    }

	public LatLng getLocationFromAddress(String strAddress) {

		Geocoder coder = new Geocoder(SearchActivity.this);
		List<Address> address;
		LatLng p1 = null;

		try {
			address = coder.getFromLocationName(strAddress, 5);
			if (address == null) {
				return null;
			}
			Address location = address.get(0);
			location.getLatitude();
			location.getLongitude();

			p1 = new LatLng(location.getLatitude(), location.getLongitude());

			getAddressFromLocation(location.getLatitude(),location.getLongitude());
		} catch (Exception ex) {

			ex.printStackTrace();
		}

		return p1;
	}
	public void getAddressFromLocation(double lat, double lon) {
		Geocoder geocoder;
		List<Address> addresses;
		geocoder = new Geocoder(SearchActivity.this);

		try {
			addresses = geocoder.getFromLocation(lat, lon, 1);
			destcountry = addresses.get(0).getCountryCode();

			System.out.println("get Country Code" + destcountry);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Here 1 represent max location result to returned, by documents it recommended 1 to 5

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
		can.putExtra("location",location);
		can.putExtra("accept",accept);
		can.putExtra("desinationbutton", desinationbutton);
		can.putExtra("address",pickupaddress );
		can.putExtra("gpscountry", gpscountry);

 		Bundle bndlanimation =ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
 	    startActivity(can,bndlanimation);
 	    finish();
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