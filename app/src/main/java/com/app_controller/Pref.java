package com.app_controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashSet;
import java.util.Set;

@SuppressLint("NewApi")
public class Pref {
	private SharedPreferences sharedPreferences;
	private final String USER_API_KEY = "api_key";
	private final String USER_EMAIL_ID = "email";
	private final String USER_PASSWORD= "password";
	private final String USER_ID= "user_id";
	private final String IS_LOGIN= "is_login";
	private final String LOGIN_RESPONCE= "login_responce";
	private final String MY_LATITUTE= "my_latitute";
	private final String MY_LONGITUTE= "my_longitute";
	private final String CATEGORY_RESPOCE= "catagory_responce";
	private final String TRIP_STATUS= "trip_status";
	private final String TRIP_RESPONCE= "trip_responce";
	private final String TRIP_ID= "trip_id";
	private final String ESTIMATED_DETAILS= "estimated_detaills";

	public SharedPreferences getSharedPreferences() {
		return sharedPreferences;
	}

	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}

	public void setTripResponce(String userApikey){
		edit().putString(TRIP_RESPONCE, userApikey).commit();
	}
	public String getTripResponce(){
		return sharedPreferences.getString(TRIP_RESPONCE,"No");
	}
	public void setCurrentTripID(String userApikey){
		edit().putString(TRIP_ID, userApikey).commit();
	}
	public String getCurrenTripID(){
		return sharedPreferences.getString(TRIP_ID,"No");
	}


	public void setTripStatus(String userApikey){
		edit().putString(TRIP_STATUS, userApikey).commit();
	}
	public String getTripStatus(){
		return sharedPreferences.getString(TRIP_STATUS,"dddd");
	}

	public void setCatagoryResponce(String userApikey){
		edit().putString(CATEGORY_RESPOCE, userApikey).commit();
	}
	public String getCatagoryResponce(){
		return sharedPreferences.getString(CATEGORY_RESPOCE,"");
	}

	public void setUserApiKey(String userApikey){
		edit().putString(USER_API_KEY, userApikey).commit();
	}
	public String getUserApiKey(){
		return sharedPreferences.getString(USER_API_KEY,null);
	}

	public void setMyLatitute(String userApikey){
		edit().putString(USER_API_KEY, userApikey).commit();
	}
	public String getMyLatitute(){
		return sharedPreferences.getString(USER_API_KEY,null);
	}

	public void setMyLongitute(String userApikey){
		edit().putString(USER_API_KEY, userApikey).commit();
	}
	public String getMyLongitute(){
		return sharedPreferences.getString(USER_API_KEY,null);
	}

	public void saveEmail(String userApikey){
		edit().putString(USER_EMAIL_ID, userApikey).commit();
	}
	public String getEmail(){
		return sharedPreferences.getString(USER_EMAIL_ID,null);
	}

	public void savePassword(String userApikey){
		edit().putString(USER_PASSWORD, userApikey).commit();
	}

	public void setTripId(String userApikey){
		edit().putString(TRIP_ID, userApikey).commit();
	}
	public String getTripid(){
		return sharedPreferences.getString(TRIP_ID,"No");
	}


	public void setEstimatedDetails(String estimatedDetails){
		edit().putString(ESTIMATED_DETAILS, estimatedDetails).commit();
	}
	public String getEstimatedDetails(){
		return sharedPreferences.getString(ESTIMATED_DETAILS,"No");
	}

	public String getPassword(){
		return sharedPreferences.getString(USER_PASSWORD,null);
	}

	public void saveUserID(String userApikey){
		edit().putString(USER_ID, userApikey).commit();
	}
	public String getUserID(){
		return sharedPreferences.getString(USER_ID,null);
	}

	public void saveIsLoginSucess(boolean isLogin){
		edit().putBoolean(IS_LOGIN,isLogin).commit();
	}
	public boolean getIsLoginSucess(){
		return sharedPreferences.getBoolean(IS_LOGIN,false);
	}

	public void saveUserLogin(String loginRes) {
		edit().putString(LOGIN_RESPONCE, loginRes).commit();
	}

	public String getUserLogin(){
		return sharedPreferences.getString(LOGIN_RESPONCE,null);
	}


	public Pref(Context context) {
		sharedPreferences = context.getSharedPreferences("rama", Context.MODE_PRIVATE);
		Set<String> set = new HashSet<String>();
	}
	public Editor edit() {
		return sharedPreferences.edit();
	}


}
