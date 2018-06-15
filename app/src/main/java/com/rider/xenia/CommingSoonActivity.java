package com.rider.xenia;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommingSoonActivity extends Activity {

	
	Button cancel,done;
	String User_id,fbuserproimg,WhoLogin,checkpassword,accept;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comming_soon);
		
getActionBar().hide();
		
		Intent i= getIntent();
		User_id= i.getStringExtra("userid");
		fbuserproimg=i.getStringExtra("fbuserproimg");
		WhoLogin=i.getStringExtra("whologin");
		System.out.println("Insite the Slide Main Activity WhoLogin"+WhoLogin);
		checkpassword=i.getStringExtra("password");
		accept=i.getStringExtra("accept");
		
		cancel=(Button)findViewById(R.id.cancelcs);
		done=(Button)findViewById(R.id.donecs);
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent search=new Intent(getApplicationContext(),SlideMainActivity.class);
			search.putExtra("userid", User_id);
			search.putExtra("fbuserproimg",fbuserproimg);
			//System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
			search.putExtra("whologin",WhoLogin);
			search.putExtra("password", checkpassword);
			System.out.println("Slide Main Activity WhoLogin"+WhoLogin);
			search.putExtra("accept",accept);

			startActivity(search);
			finish();
			}
			});
done.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent search=new Intent(getApplicationContext(),SlideMainActivity.class);
			search.putExtra("userid", User_id);
			search.putExtra("fbuserproimg",fbuserproimg);
			//System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
			search.putExtra("whologin",WhoLogin);
			search.putExtra("password", checkpassword);
			System.out.println("Slide Main Activity WhoLogin"+WhoLogin);
			search.putExtra("accept",accept);

			startActivity(search);
			finish();
			}
			});
		
	}

}