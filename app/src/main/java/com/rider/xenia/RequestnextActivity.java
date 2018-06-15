package com.rider.xenia;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class RequestnextActivity extends Activity {
Button enterdesination,back;
String User_id,fbuserproimg,WhoLogin,checkpassword,location,desinationbuttonaction,picupaddress,accept,originlat,originlot,driverdetailsuserid,pickupaddress;
String category,lat,lng,desinationbutton;

	Boolean categorynetcheck = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_requestnext);
		getActionBar().hide();
		enterdesination=(Button)findViewById(R.id.enterurdesination);
		back=(Button)findViewById(R.id.backline);
		
		Intent i= getIntent();
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
    	
    	desinationbutton="enterdesination";
    	
    	enterdesination.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (net_connection_check()) {
					//close=false;
					Intent request = new Intent(getApplicationContext(), SearchActivity.class);
					request.putExtra("userid", User_id);
					request.putExtra("fbuserproimg", fbuserproimg);
					System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
					request.putExtra("whologin", WhoLogin);
					request.putExtra("password", checkpassword);
					request.putExtra("Driverid", driverdetailsuserid);
					System.out.println("Request Function Driver id" + driverdetailsuserid);
					request.putExtra("accept", accept);
					request.putExtra("desinationbutton", desinationbutton);
					request.putExtra("category", category);
					System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
					System.out.println("Slide Lat " + lat);
					System.out.println("Slide Lng" + lng);
					request.putExtra("Lat", lat);
					request.putExtra("Long", lng);
					request.putExtra("category", category);

					startActivity(request);
					finish();
				}
			}
			});
    	
    	back.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    		Intent request=new Intent(getApplicationContext(),SlideMainActivity.class);
    		request.putExtra("userid", User_id);
    		request.putExtra("fbuserproimg",fbuserproimg);
    		System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
    		request.putExtra("whologin",WhoLogin);
    		request.putExtra("password", checkpassword);
    		request.putExtra("Driverid",driverdetailsuserid);
    		System.out.println("Request Function Driver id"+driverdetailsuserid);
    		request.putExtra("accept", accept);
    		request.putExtra("desinationbutton", desinationbutton);
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
    	});
    	
	}


	public boolean net_connection_check() {
		ConnectionManager cm = new ConnectionManager(this);

		boolean connection = cm.isConnectingToInternet();


		if (!connection) {
			dialogcalling();
			categorynetcheck = true;
		}
		return connection;
	}
	public void dialogcalling() {
		final Dialog dialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);

		// Setting dialogview
		Window window = dialog.getWindow();
		window.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

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



	public void onBackPressed()
	{
		Intent request=new Intent(getApplicationContext(),SlideMainActivity.class);
		request.putExtra("userid", User_id);
		request.putExtra("fbuserproimg",fbuserproimg);
		System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
		request.putExtra("whologin",WhoLogin);
		request.putExtra("password", checkpassword);
		request.putExtra("Driverid",driverdetailsuserid);
		System.out.println("Request Function Driver id"+driverdetailsuserid);
		request.putExtra("accept", accept);
		request.putExtra("desinationbutton", desinationbutton);
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
	


}
