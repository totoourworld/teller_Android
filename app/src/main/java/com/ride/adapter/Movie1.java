package com.ride.adapter;

import java.util.ArrayList;

public class Movie1 {
	private String userid,date1,time1,rider,fpickup,carname;
	private String riderid,waitingtime,tripid ,amount ,pickup,drop,datetime, service, distance, drivername, ridername, status;
	
private Double rate;
private int count;

	public Movie1() {
		
	}

	public Movie1(String pickup,String ridername,String date1,String time1,String carname) {
		
		
		
		this.pickup=pickup;
		
		
		this.ridername=ridername;
		
		this.date1=date1;
		this.time1=time1;
		this.carname=carname;
		
		
	}

	
	public String getpickup() {
		return pickup;
	}
	public void setpickup(String pickup) {
		this.pickup = pickup;
	}
	



	 

	
		
		
		public String gettime1() {
			return time1;
		}
		 
		public void settime1(String time1) {
			this.time1= time1;
		}
		public String getdate1() {
			return date1;
		}
		 
		public void setdate1(String date1) {
			this.date1= date1;
		}
		public String getcarname() {
			return carname;
		}
		public void setcarname(String carname) {
			this.carname = carname;
		}
		
}
