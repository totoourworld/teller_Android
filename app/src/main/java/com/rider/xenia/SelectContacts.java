package com.rider.xenia;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


 
 
public class SelectContacts extends Activity {
 
	private static final String TAG = SelectContacts.class.getSimpleName();
	
    // ArrayList
    ArrayList<SelectUser> selectUsers;
    List<SelectUser> temp;
    // Contact List
    public ListView listView;
    // Cursor to load contacts list
    public Cursor phones, email;
 
    // Pop up
    ContentResolver resolver;
    SearchView search;
    SelectUserAdapter adapter;
    
    String selectedphone;
	String selectedname;
	Bitmap selectedprofile;    
    private JSONArray splitdetails_jsonarray;
	private JSONObject splitdetails_jsonobj;
	private String splitdetailsjson_status,ridersharephonenumbers=null;
	    
	Button ridecancel,rideshare;
	
	String Liveurl1="";
	String Liveurl="";
	
	String User_id,fbuserproimg,WhoLogin,checkpassword,location,desinationbuttonaction,pickupaddress,accept;
	String originlat,originlot,acceptdriverid,gpscountry,cancel,destaddress;
	
	
	 
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_contact);
        
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);
        Liveurl=sharedPreferences.getString("liveurl",null);	
		
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#1b1b1b")));
		Intent i= getIntent();
     	User_id= i.getStringExtra("userid");
     	fbuserproimg=i.getStringExtra("fbuserproimg");
     	WhoLogin=i.getStringExtra("whologin");
     	System.out.println("Slide Main Activity WhoLogin"+WhoLogin);
     	checkpassword=i.getStringExtra("password");
     	location=i.getStringExtra("location");
     	desinationbuttonaction=i.getStringExtra("desinationbutton");
    	pickupaddress=i.getStringExtra("address");
    	accept=i.getStringExtra("accept");
     	originlat=i.getStringExtra("lat");
     	originlot=i.getStringExtra("lot");
     	acceptdriverid=i.getStringExtra("acceptdriverid");
     	gpscountry=i.getStringExtra("gpscountry");
     	cancel=i.getStringExtra("cancel");
     	destaddress=i.getStringExtra("destaddress");
 
        selectUsers = new ArrayList<SelectUser>();
        resolver = this.getContentResolver();
        listView = (ListView) findViewById(R.id.contacts_list);
 
        phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        LoadContact loadContact = new LoadContact();
        loadContact.execute();
        
        ridecancel = (Button) findViewById(R.id.cancelsplit);
        rideshare = (Button) findViewById(R.id.sendsplit);
 
        search = (SearchView) findViewById(R.id.searchView);
        //search.setIconifiedByDefault(true);
        search.setSubmitButtonEnabled(true);
        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
 
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
 
                return false;
            }
 
            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                adapter.filter(newText);
                return false;
            }
        });
        
    
     	

		ridecancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
		            	getActionBar().hide();
			
		            	Intent search=new Intent(getApplicationContext(),SlideMainActivity.class);
		            	search.putExtra("userid", User_id);
		            	search.putExtra("fbuserproimg",fbuserproimg);
		            	search.putExtra("whologin",WhoLogin);
		            	search.putExtra("password", checkpassword);
		            	search.putExtra("desinationbutton", desinationbuttonaction);
		            	search.putExtra("address",pickupaddress );
		            	search.putExtra("gpscountry", gpscountry);
		            	search.putExtra("desinationbutton", desinationbuttonaction);
		            	search.putExtra("destaddress",destaddress);
		            	search.putExtra("lat",originlat );
		            	search.putExtra("lot",originlot);
		            	search.putExtra("acceptdriverid", acceptdriverid);
		            	search.putExtra("rideshare","cancel");
		            	if(accept==null)
		        	 	{ 		
		        	 		search.putExtra("accept", "null");
		        	 	}
		        		else if(accept.equals("yes"))
		        		{
		        			search.putExtra("accept", accept);
		        			accept=accept;
		        		}
		        		else
		        		{
		        			search.putExtra("accept", "null");
		        		}
		            	startActivity(search);
			
			}
			});
		
		rideshare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
		            	getActionBar().hide();
		            	int n=phones.getCount();
		            	int i;
		            	int count=0;
		            	SelectUser selectUser = new SelectUser();
		            	
		            	
		            	
		            	System.out.println("Total Count"+n);
	                    System.out.println("Check Box status"+selectUser.getCheckedBox());
	                    
		            	for(i=1;i<=n;i++)
		            	{
		            		
		            		System.out.println("i =========================:"+i);
		            		

			            	 try {
			            		 SelectUser data = selectUsers.get(i);
			                    
		            		
		            		 
		            		
		            		System.out.println("Check Boc status ==========:"+data.getCheckedBox());

		            		
		            		if(data.getCheckedBox())
		            		{
		            			count=count+1;
		            		selectedphone=data.getPhone();
		            		selectedname=data.getName();
		            		selectedprofile=data.getThumb();
		            		
		            		if(ridersharephonenumbers==null)
		            		{
		            		ridersharephonenumbers=selectedphone;
                                System.out.println("Selected Rider Phone Number"+ridersharephonenumbers);
                                System.out.println("Checked phone"+selectedphone);
                                System.out.println("Checked name"+selectedname);
		            		}
		            		else
		            		{
		            			ridersharephonenumbers=ridersharephonenumbers+","+selectedphone;
		            		}
		            		System.out.println("Selected Rider Phone Number"+ridersharephonenumbers);
		            		System.out.println("Checked phone"+selectedphone);
		                    System.out.println("Checked name"+selectedname);

		            		}
			            	 } catch (IndexOutOfBoundsException e) {
			                        e.printStackTrace();
			                    }
		            	}
		            	
		            	if(count<2)
		            	{
		            	sendinvite();
		            	
		            	}
		            	else
		            	{
							System.out.println("Insite the Drive cancel after accept requst dialog alert show");
		            			android.support.v7.app.AlertDialog.Builder builder =
										new android.support.v7.app.AlertDialog.Builder(SelectContacts.this, R.style.AppCompatAlertDialogStyle);
		            			builder.setTitle(R.string.message);
		            		   builder.setMessage(R.string.select_max_1_contacts);

		            		    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		            		     	 
		            		        @Override
		            		        public void onClick(DialogInterface dialog, int which) {
		            		        	
		            			    
		            		        	
		            		        	dialog.dismiss();
		            			    	 }
		            		      //  alertdialog2.cancel();
		            		            
		            		            }); 




		            		    builder.show();

		            		
		            	}
			}
			});
    }
    
    
    // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
 
        }
 
        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone
 
            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                    Toast.makeText(SelectContacts.this, R.string.no_contacts_in_your_contact_list, Toast.LENGTH_LONG).show();
                }
 
                while (phones.moveToNext()) {
                    Bitmap bit_thumb = null;
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                    String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                    try {
                        if (image_thumb != null) {
                            bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                        } else {
                            Log.e("No Image Thumb", "--------------");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
 
                    SelectUser selectUser = new SelectUser();
                    selectUser.setThumb(bit_thumb);
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);
                    selectUser.setEmail(id);
                    selectUser.setCheckedBox(false);
                    selectUsers.add(selectUser);
                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }

            return null;
        }
 
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new SelectUserAdapter(selectUsers, SelectContacts.this);
            listView.setAdapter(adapter);
 
            // Select item on listclick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
 
                    Log.e("search", "here---------------- listener");
 
                    SelectUser data = selectUsers.get(i);

                    
                   
                }
            });
 
            listView.setFastScrollEnabled(true);
        }
    }
 
    @Override
    protected void onStop() {
        super.onStop();
        phones.close();
    }
    
    public void sendinvite()
	{
        if(ridersharephonenumbers!=null)
    	ridersharephonenumbers=ridersharephonenumbers.replaceAll(" ", "");
		final String url=Liveurl+"send_split_request?userid="+User_id+"&splitid="+ridersharephonenumbers;//"9750034438,65686";
		System.out.println("URL is"+url);


	 // Creating volley request obj
	JsonArrayRequest movieReq = new JsonArrayRequest(url,
			new Response.Listener<JSONArray>() {
				@Override
				public void onResponse(JSONArray response) {
					 
					
					// Parsing json
					for (int i = 0; i < response.length(); i++) {
						try {
							splitdetails_jsonobj = response.getJSONObject(i);
							
							splitdetailsjson_status= splitdetails_jsonobj.getString("status"); 
							
							System.out.println("Get Category details "+response);
							Log.d("OUTPUT IS",splitdetailsjson_status);
							if(splitdetailsjson_status.matches("Success")){
								
								 System.out.println("User details updated");
								 
								 Intent search=new Intent(getApplicationContext(),SlideMainActivity.class);
					            	search.putExtra("userid", User_id);
					            	search.putExtra("fbuserproimg",fbuserproimg);
					            	search.putExtra("whologin",WhoLogin);
					            	search.putExtra("password", checkpassword);
					            	search.putExtra("desinationbutton", desinationbuttonaction);
					            	search.putExtra("address",pickupaddress );
					            	search.putExtra("destaddress",destaddress);
					            	search.putExtra("gpscountry", gpscountry);
					            	search.putExtra("lat",originlat );
					            	search.putExtra("lot",originlot);
					            	search.putExtra("acceptdriverid", acceptdriverid);
					            	search.putExtra("rideshare","share");
					            	
					            	search.putExtra("splitname", selectedname);
					            	search.putExtra("splitphone",selectedphone );
					            	search.putExtra("splitprofile", selectedprofile);
					            	
					            	if(accept==null)
					        	 	{ 		
					        	 		search.putExtra("accept", "null");
					        	 	}
					        		else if(accept.equals("yes"))
					        		{
					        			search.putExtra("accept", accept);
					        			accept=accept;
					        		}
					        		else
					        		{
					        			search.putExtra("accept", "null");
					        		}
					            	startActivity(search);
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
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.there_is_no_connection, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 70);
                    toast.show();
                }
				VolleyLog.d(TAG, R.string.error + error.getMessage());
				 

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