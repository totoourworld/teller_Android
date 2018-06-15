/* Copyright 2017 freecodeformat.com */
package com.utils;

import java.io.Serializable;

/* Time: 2017-01-04 19:44:15 @author freecodeformat.com @website http://www.freecodeformat.com/json2javabean.php */
public class TripHistory  implements Serializable{


    private String tripId;
    private String tripDate;
    private String driverId;
    private String userId;
    private String tripFromLoc;
    private String tripToLoc;
    private String tripDistance;
    private String tripFare;
    private String tripWaitTime;
    private String tripPickupTime;
    private String tripDropTime;
    private String tripReason;
    private String tripValidity;
    private String tripFeedback;
    private String tripStatus;
    private String tripRating;
    private String tripScheduledPickLat;
    private String tripScheduledPickLng;
    private String tripActualPickLat;
    private String tripActualPickLng;
    private String tripScheduledDropLat;
    private String tripScheduledDropLng;
    private String tripActualDropLat;
    private String tripActualDropLng;
    private String tripSearchedAddr;
    private String tripSearchResultAddr;
    private String tripPayMode;
    private String tripPayAmount;
    private String tripPayDate;
    private String tripPayStatus;
    private String tripDriverCommision;
    private String tripCreated;
    private String tripModified;
    private String promoId;
    private String tripPromoCode;
    private String tripPromoAmt;

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    private String taxAmount;

    public String getTrip_promo_amt() {
        return trip_promo_amt;
    }

    public void setTrip_promo_amt(String trip_promo_amt) {
        this.trip_promo_amt = trip_promo_amt;
    }

    private String trip_promo_amt;
    private DriverInfo driver;
    //@JsonProperty("User")
    private DriverInfo user;
    public void setTripId(String tripId) {
         this.tripId = tripId;
     }
     public String getTripId() {
         return tripId;
     }

    public void setTripDate(String tripDate) {
         this.tripDate = tripDate;
     }
     public String getTripDate() {
         return tripDate;
     }

    public void setDriverId(String driverId) {
         this.driverId = driverId;
     }
     public String getDriverId() {
         return driverId;
     }

    public void setUserId(String userId) {
         this.userId = userId;
     }
     public String getUserId() {
         return userId;
     }

    public void setTripFromLoc(String tripFromLoc) {
         this.tripFromLoc = tripFromLoc;
     }
     public String getTripFromLoc() {
         return tripFromLoc;
     }

    public void setTripToLoc(String tripToLoc) {
         this.tripToLoc = tripToLoc;
     }
     public String getTripToLoc() {
         return tripToLoc;
     }

    public void setTripDistance(String tripDistance) {
         this.tripDistance = tripDistance;
     }
     public String getTripDistance() {
         return tripDistance;
     }

    public void setTripFare(String tripFare) {
         this.tripFare = tripFare;
     }
     public String getTripFare() {
         return tripFare;
     }

    public void setTripWaitTime(String tripWaitTime) {
         this.tripWaitTime = tripWaitTime;
     }
     public String getTripWaitTime() {
         return tripWaitTime;
     }

    public void setTripPickupTime(String tripPickupTime) {
         this.tripPickupTime = tripPickupTime;
     }
     public String getTripPickupTime() {
         return tripPickupTime;
     }

    public void setTripDropTime(String tripDropTime) {
         this.tripDropTime = tripDropTime;
     }
     public String getTripDropTime() {
         return tripDropTime;
     }

    public void setTripReason(String tripReason) {
         this.tripReason = tripReason;
     }
     public String getTripReason() {
         return tripReason;
     }

    public void setTripValidity(String tripValidity) {
         this.tripValidity = tripValidity;
     }
     public String getTripValidity() {
         return tripValidity;
     }

    public void setTripFeedback(String tripFeedback) {
         this.tripFeedback = tripFeedback;
     }
     public String getTripFeedback() {
         return tripFeedback;
     }

    public void setTripStatus(String tripStatus) {
         this.tripStatus = tripStatus;
     }
     public String getTripStatus() {
         return tripStatus;
     }

    public void setTripRating(String tripRating) {
         this.tripRating = tripRating;
     }
     public String getTripRating() {
         return tripRating;
     }

    public void setTripScheduledPickLat(String tripScheduledPickLat) {
         this.tripScheduledPickLat = tripScheduledPickLat;
     }
     public String getTripScheduledPickLat() {
         return tripScheduledPickLat;
     }

    public void setTripScheduledPickLng(String tripScheduledPickLng) {
         this.tripScheduledPickLng = tripScheduledPickLng;
     }
     public String getTripScheduledPickLng() {
         return tripScheduledPickLng;
     }

    public void setTripActualPickLat(String tripActualPickLat) {
         this.tripActualPickLat = tripActualPickLat;
     }
     public String getTripActualPickLat() {
         return tripActualPickLat;
     }

    public void setTripActualPickLng(String tripActualPickLng) {
         this.tripActualPickLng = tripActualPickLng;
     }
     public String getTripActualPickLng() {
         return tripActualPickLng;
     }

    public void setTripScheduledDropLat(String tripScheduledDropLat) {
         this.tripScheduledDropLat = tripScheduledDropLat;
     }
     public String getTripScheduledDropLat() {
         return tripScheduledDropLat;
     }

    public void setTripScheduledDropLng(String tripScheduledDropLng) {
         this.tripScheduledDropLng = tripScheduledDropLng;
     }
     public String getTripScheduledDropLng() {
         return tripScheduledDropLng;
     }

    public void setTripActualDropLat(String tripActualDropLat) {
         this.tripActualDropLat = tripActualDropLat;
     }
     public String getTripActualDropLat() {
         return tripActualDropLat;
     }

    public void setTripActualDropLng(String tripActualDropLng) {
         this.tripActualDropLng = tripActualDropLng;
     }
     public String getTripActualDropLng() {
         return tripActualDropLng;
     }

    public void setTripSearchedAddr(String tripSearchedAddr) {
         this.tripSearchedAddr = tripSearchedAddr;
     }
     public String getTripSearchedAddr() {
         return tripSearchedAddr;
     }

    public void setTripSearchResultAddr(String tripSearchResultAddr) {
         this.tripSearchResultAddr = tripSearchResultAddr;
     }
     public String getTripSearchResultAddr() {
         return tripSearchResultAddr;
     }

    public void setTripPayMode(String tripPayMode) {
         this.tripPayMode = tripPayMode;
     }
     public String getTripPayMode() {
         return tripPayMode;
     }

    public void setTripPayAmount(String tripPayAmount) {
         this.tripPayAmount = tripPayAmount;
     }
     public String getTripPayAmount() {
         return tripPayAmount;
     }

    public void setTripPayDate(String tripPayDate) {
         this.tripPayDate = tripPayDate;
     }
     public String getTripPayDate() {
         return tripPayDate;
     }

    public void setTripPayStatus(String tripPayStatus) {
         this.tripPayStatus = tripPayStatus;
     }
     public String getTripPayStatus() {
         return tripPayStatus;
     }

    public void setTripDriverCommision(String tripDriverCommision) {
         this.tripDriverCommision = tripDriverCommision;
     }
     public String getTripDriverCommision() {
         return tripDriverCommision;
     }

    public void setTripCreated(String tripCreated) {
         this.tripCreated = tripCreated;
     }
     public String getTripCreated() {
         return tripCreated;
     }

    public void setTripModified(String tripModified) {
         this.tripModified = tripModified;
     }
     public String getTripModified() {
         return tripModified;
     }

    public void setPromoId(String promoId) {
         this.promoId = promoId;
     }
     public String getPromoId() {
         return promoId;
     }

    public void setTripPromoCode(String tripPromoCode) {
         this.tripPromoCode = tripPromoCode;
     }
     public String getTripPromoCode() {
         return tripPromoCode;
     }

    public void setTripPromoAmt(String tripPromoAmt) {
         this.tripPromoAmt = tripPromoAmt;
     }
     public String getTripPromoAmt() {
         return tripPromoAmt;
     }

    public void setDriver(DriverInfo driver) {
         this.driver = driver;
     }
     public DriverInfo getDriver() {
         return driver;
     }

    public void setUser(DriverInfo user) {
         this.user = user;
     }
     public DriverInfo getUser() {
         return user;
     }

}