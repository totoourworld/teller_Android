package com.rider.xenia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.animation.Animation;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;
import com.grepix.grepixutils.GrepixUtils;
import com.utils.Constants;
import com.utils.HelperMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class SplashScreenActivity extends FragmentActivity {

	private static final int SPLASH_SHOW_TIME = 5000;
	private static final int MY_PERMISSIONS_REQUEST_LOC = 30;
	// Animation
	Animation animZoomIn,animZoomout,blink;
	private Controller controller;
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	private double latitude;
	private double longitude;
	private HelperMethods helperMethods;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		controller=(Controller)getApplicationContext();
		new BackgroundSplashTask().execute();
//		 helperMethods=new HelperMethods(SplashScreenActivity.this);
//		///if (helperMethods.net_connection_check()) {
//			getcategory();
//	//	}
	}

	public void getcategory() {

		final ProgressDialog dialog = new ProgressDialog(SplashScreenActivity.this);
		dialog.setMessage(getString(R.string.please_wait));
		dialog.setTitle("");
		dialog.show();
		dialog.setCancelable(false);

		StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Urls.URL_USER_GET_CATEGORY ,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {


						dialog.dismiss();
						//Toast.makeText(SplashScreenActivity.this,"Int "+response, Toast.LENGTH_LONG).show();
						if (response != null) {
							controller.pref.setCatagoryResponce(response);
							try{
							if(!controller.pref.getIsLoginSucess()){
								Intent i = new Intent(SplashScreenActivity.this,
										SigninActivity.class);
								// any info loaded can during splash_show
								// can be passed to main activity using
								// below
								i.putExtra("loaded_info", " ");
								startActivity(i);
								finish();
							}else{
								Intent i = new Intent(SplashScreenActivity.this, MainScreenActivity.class);
								//Intent i = new Intent(SplashScreenActivity.this, SlideMainActivity.class);
								// any info loaded can during splash_show
								// can be passed to main activity using
								// below
								i.putExtra("loaded_info", " ");
								startActivity(i);
								finish();
							}
						}catch (Exception e){
							Intent i = new Intent(SplashScreenActivity.this,
									SigninActivity.class);
							// any info loaded can during splash_show
							// can be passed to main activity using
							// below
							i.putExtra("loaded_info", " ");
							startActivity(i);
							finish();
						}

						} else {
							//   Util.showdialog(getActivity(), "No Network !", "Internet Connection Failed");
							GrepixUtils.dialogcalling(SplashScreenActivity.this);

						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						dialog.dismiss();
						if(error instanceof NoConnectionError){
							GrepixUtils.dialogcalling(SplashScreenActivity.this);
							new BackgroundSplashTask().execute();
						}

						else if(error instanceof ServerError){

							String d= new  String(error.networkResponse.data);
							try {
								new BackgroundSplashTask().execute();
								GrepixUtils.dialogcalling(SplashScreenActivity.this);
								JSONObject jso= new JSONObject(d);
								Toast.makeText(SplashScreenActivity.this, jso.getString("message"),Toast.LENGTH_LONG).show();
								// signUpFacebook();
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}


				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				Map<String, String> params = new HashMap<String, String>();
				params.put(Constants.Keys.API_KEY,controller.pref.getUserApiKey());


				return params;
			}


		};
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				500000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		RequestQueue requestQueue = Volley.newRequestQueue(SplashScreenActivity.this);
		requestQueue.add(stringRequest);


	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			// I have just give a sleep for this thread
			// if you want to load database, make
			// network calls, load images
			// you can do here and remove the following
			// sleep

			// do not worry about this Thread.sleep
			// this is an async task, it will not disrupt the UI
			try {
				Thread.sleep(SPLASH_SHOW_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
           		try{
			if(!controller.pref.getIsLoginSucess()){
				Intent i = new Intent(SplashScreenActivity.this,
						SigninActivity.class);
				// any info loaded can during splash_show
				// can be passed to main activity using
				// below
				i.putExtra("loaded_info", " ");
				startActivity(i);
				finish();
			}else{
				Intent i = new Intent(SplashScreenActivity.this, MainScreenActivity.class);
				//Intent i = new Intent(SplashScreenActivity.this, SlideMainActivity.class);
				// any info loaded can during splash_show
				// can be passed to main activity using
				// below
				i.putExtra("loaded_info", " ");
				startActivity(i);
				finish();
			}
		}catch (Exception e){
			Intent i = new Intent(SplashScreenActivity.this,
					SigninActivity.class);
			// any info loaded can during splash_show
			// can be passed to main activity using
			// below
			i.putExtra("loaded_info", " ");
			startActivity(i);
			finish();
		}


	}

	}


}
