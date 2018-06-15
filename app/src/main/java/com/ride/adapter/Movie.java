package com.ride.adapter;

public class Movie {
	private String userid,date1,time1,rider,fpickup,carname;
	private String riderid,profile,driverid,time,waitingtime,tripid ,amount ,
			pickup,drop,datetime, service, distance, drivername, ridername, status;


	public Movie() {
		
	}

	public Movie(String riderid,String profile,String driverid,String time,String amount ,String pickup,String drop,String datetime,
				 String service,String distance,String drivername,String ridername,String status,String tripid,
				 String waitingtime,String date1,String time1,String rider,String fpickup,String carname) {
		
		
		this.riderid=riderid;
		this.amount=amount;
		this.pickup=pickup;
		this.drop=drop;
		this.datetime=datetime;
		this.service=service;
		this.distance=distance;
		this.drivername=drivername;
		this.ridername=ridername;
		this.status=status;
		this.date1=date1;
		this.time1=time1;
		this.rider=rider;
		this.fpickup=fpickup;
		this.carname=carname;
		this.profile=profile;
		this.time = time;
		this.driverid = driverid;
	}
	 //Detailed list space
	 
	public String getid() {
		return riderid;
	}
	public void setid(String riderid) {
		this.riderid = riderid;
	}
	public String getid1() {
		return driverid;
	}
	public void setid1(String driverid) {
		this.driverid = driverid;
	}
	
	public String getamount() {
		return amount;
	}
	public void setamount(String amount) {
		this.amount = amount;
	}
//*****address****************************

	public String getprofile() {
		return profile;
	}

	public void setprofile(String profile) {
		this.profile= profile;
	}

	public String gettime() {
		System.out.println("value of rating "+time);
		return time;
	}
	public void settime(String time) {
		this.time = time;
	}


	public String getpickup() {
		return pickup;
	}
	public void setpickup(String pickup) {
		this.pickup = pickup;
	}
	public String getdrop() {
		return drop;
	}
	public void setdrop(String drop) {
		this.drop = drop;
	}
//**************price*******************	
	public String getdatetime() {
		return datetime;
	}
	public void setdatetime(String datetime) {
		this.datetime = datetime;
	}
	
	//**************symbol*******************	
	
	public String getservice() {
		return service;
	}
	public void setservice(String service) {
		this.service = service;
	}
	//**************title*******************		 
	public String getdistance() {
		return distance;
	}
	public void setdistance(String distance) {
		this.distance = distance;
	}

//**************bg_image*******************
	public String getridername() {
		return ridername;
	}
	 
	public void setridername(String ridername) {
		this.ridername = ridername;
		
	}
	
//**************profile image*******************
	public String getdrivername() {
		return drivername;
	}
	 
	public void setdrivername(String drivername) {
		this.drivername= drivername;
	}
	//**************status*******************
		public String getstatus() {
			return status;
		}
		 
		public void setstatus(String status) {
			this.status= status;
		}
		public String gettripid() {
			return tripid;
		}
		 
		public void settripid(String tripid) {
			this.tripid= tripid;
		}
		
		public String getwaitingtime() {
			return waitingtime;
		}
		 
		public void setwaitingtime(String waitingtime) {
			this.waitingtime= waitingtime;
		}	

		public String getcarname() {
			return carname;
		}
		public void setcarname(String carname) {
			this.carname = carname;
		}
}
