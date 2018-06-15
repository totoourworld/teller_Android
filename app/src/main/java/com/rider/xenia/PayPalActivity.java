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
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class PayPalActivity extends Activity {
	WebView webView;
String User_id,fbuserproimg,WhoLogin,token;
String msg,done;
String checkpassword,Amount,drivername,driveremail,drivermobile,tax,wait,pickup,drop,date,service,distance,tripid,autotripid,driverid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_pal);
		
		Intent fare= getIntent();
		User_id=fare.getStringExtra("userid");
		fbuserproimg=fare.getStringExtra("fbuserproimg");
		WhoLogin=fare.getStringExtra("whologin");
		checkpassword=fare.getStringExtra("password");
		Amount=fare.getStringExtra("amount");
		drivername=fare.getStringExtra("drivername");
		driveremail=fare.getStringExtra("driveremail");
		drivermobile=fare.getStringExtra("drivermobile");
		tax=fare.getStringExtra("tax");
		wait=fare.getStringExtra("wait");
		pickup=fare.getStringExtra("pickup");
		drop=fare.getStringExtra("drop");
		date=fare.getStringExtra("date");
		service=fare.getStringExtra("service");
		distance=fare.getStringExtra("distance");
		
		tripid=fare.getStringExtra("tripid");
		autotripid=fare.getStringExtra("autotripid");
		driverid=fare.getStringExtra("driverid");
		
		token=fare.getStringExtra("token");
		 
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new MyBrowser());
		webView.getSettings().setLoadsImagesAutomatically(true);
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

	    webView.loadUrl(token);

	      String originalurl=webView.getOriginalUrl();
	      String currenturl=webView.getUrl();
	      System.out.println("Original Url is"+originalurl);
	      System.out.println("Current Url is"+currenturl);


	}
	 private class MyBrowser extends WebViewClient {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	  
	    	  view.loadUrl(url);
	    	  
	         System.out.println("Current url in webview client is"+url);
	 		System.out.println("Before trim"+url);

	 			         
	 		 if(url.contains("paypal_success"))
	         {
	 			
	         	System.out.println("Equals");
	         	done="done";
	         	msg="Your PayPal Payment made successfully";
	         	paymentalert(msg);
	         }
	         else if(url.contains("paypal_cancel"))
	         {
	         	System.out.println("Not Equals");
	         	done="notdone";
	         	msg="Your PayPal Payment is not successfully";
	         	paymentalert(msg);
	         }
	         else
	         {
	        	 
	         }

	         return true;
	      }
	   }
	 //02-23 22:14:38.192: I/System.out(29830): Current url in webview client ishttp://demo.cogzidel.com/arcaneweb/index.php?/administrator/payment/paypal_success?&token=EC-8LJ97980HX590704S&PayerID=SGLN9TGEHHUES
	 private void paymentalert(String msg) {
			
			android.support.v7.app.AlertDialog.Builder builder =
					new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
					builder.setTitle(R.string.payment_status);
			        builder.setMessage(msg);
			       builder.setCancelable(false);
			        builder.setInverseBackgroundForced(false);
			        
			       builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			            
			        

			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			                
			            	
			            	Intent x=new Intent(getApplicationContext(),FareActivity.class);
			        		x.putExtra("userid", User_id);
			        		x.putExtra("fbuserproimg",fbuserproimg);
			        		x.putExtra("whologin",WhoLogin);
			        		x.putExtra("password", checkpassword);
			        		x.putExtra("amount", Amount);
			        		x.putExtra("drivername",drivername);
			        		x.putExtra("driveremail",driveremail);
			        		x.putExtra("drivermobile",drivermobile);
			        		x.putExtra("tax",tax);
			        		x.putExtra("pickup",pickup);
			        		x.putExtra("drop",drop);
			        		x.putExtra("date",date);
			        		x.putExtra("wait",wait);
			        		x.putExtra("service",service);
			        		x.putExtra("distance",distance);
			        		x.putExtra("paydone",done);
			        		x.putExtra("tripid",tripid);
			            	x.putExtra("autotripid",autotripid);
			            	x.putExtra("driverid",driverid);
			        		startActivity(x);
			        		finish();
			

			            }
			            });
			



                  builder.show();
			
			}
	 public void onBackPressed()
	 {
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
        // dialog.setTitle("There is no network please conect");
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
