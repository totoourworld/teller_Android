package com.utils;

import android.content.Context;
import android.widget.Toast;

import com.addressHelper.DriverConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by devin on 2016-12-05.
 */
public class ParseJson {

    Context context;


    public ParseJson(Context context) {
        this.context = context;
    }


    public ArrayList<DriverConstants> driverConstantParseApi(String s) {
        ArrayList<DriverConstants> constantList = new ArrayList<DriverConstants>();
        //  loginResponseList = new ArrayList<SingleObject>();
        try {
            JSONObject rootObject = new JSONObject(s);
            String status = rootObject.getString("status");

            JSONArray jsonArray = rootObject.getJSONArray("response");


            // SingleObject singleObject = SingleObject.getInstance();
            for (int i = 0; i < jsonArray.length(); i++) {
                DriverConstants actors = new DriverConstants();
                JSONObject childObject = jsonArray.getJSONObject(i);

                String category_id = childObject.getString("constant_id");
                actors.setConstant_id(category_id);
                String cat_name = childObject.getString("constant_display");
                actors.setConstant_display(cat_name);
                String cat_desc = childObject.getString("constant_key");
                actors.setConstant_key(cat_desc);
                String cat_base_price = childObject.getString("constant_value");
                actors.setConstant_value(cat_base_price);
                String cat_fare_per_km = childObject.getString("active");
                actors.setActive(cat_fare_per_km);
                String cat_fare_per_min = childObject.getString("created");
                actors.setCreated(cat_fare_per_min);




                constantList.add(actors);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  constantList;
    }



    public DriverInfo ParseLoginResponce(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            DriverInfo responce = new DriverInfo();
            String status = jsonObject.getString("status");
            if (status == "OK" || status.equals("OK")) {
                JSONObject object = jsonObject.getJSONObject("response");
                responce.setUserId(object.getString("user_id"));
                responce.setApiKey(object.getString("api_key"));
                responce.setGroupId(object.getString("group_id"));
                responce.setUsername(object.getString("username"));
                responce.setUName(object.getString("u_name"));
                responce.setUFname(object.getString("u_fname"));
                responce.setULname(object.getString("u_lname"));
                responce.setUEmail(object.getString("u_email"));
                responce.setUPhone(object.getString("u_phone"));
                responce.setUAddress(object.getString("u_address"));
                responce.setUCity(object.getString("u_city"));
                responce.setUState(object.getString("u_state"));
                responce.setUCity(object.getString("u_country"));
                responce.setUZip(object.getString("u_zip"));
                responce.setULat(object.getString("u_lat"));
                responce.setULng(object.getString("u_lng"));
                responce.setUDegree(object.getString("u_degree"));
                responce.setImageId(object.getString("image_id"));
                responce.setUDeviceType(object.getString("u_device_type"));
                responce.setUIsAvailable(object.getString("u_is_available"));
//                responce.setActive(object.getString("active"));
                responce.setUCreated(object.getString("u_created"));
                responce.setUModified(object.getString("u_modified"));
                responce.setUProfileImagePath(object.getString("u_profile_image_path"));
                responce.setEmergency_contact_1(object.getString("emergency_contact_1"));
                responce.setEmergency_contact_2(object.getString("emergency_contact_2"));
                responce.setEmergency_contact_3(object.getString("emergency_contact_3"));
                responce.setEmergency_email_1(object.getString("emergency_email_1"));
                responce.setEmergency_email_2(object.getString("emergency_email_2"));
                responce.setEmergency_email_3(object.getString("emergency_email_3"));
                return responce;

            } else {
                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


    public ArrayList<Catagories> getCatgory(String json) {
        ArrayList<Catagories> catagoriesArrayList = new ArrayList<Catagories>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("response");

            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);

                Catagories responce = new Catagories();


                responce.setCategory_id(object.getString("category_id"));
                responce.setCat_name(object.getString("cat_name"));
                responce.setCat_desc(object.getString("cat_desc"));
                responce.setCat_base_price(object.getString("cat_base_price"));
                responce.setCat_fare_per_km(object.getString("cat_fare_per_km"));
                responce.setCat_fare_per_min(object.getString("cat_fare_per_min"));
                responce.setCat_max_size(object.getString("cat_max_size"));
                responce.setCat_is_fixed_price(object.getString("cat_is_fixed_price"));
                responce.setCat_prime_time_percentage(object.getString("cat_prime_time_percentage"));
                responce.setCat_status(object.getString("cat_status"));
                responce.setCat_created(object.getString("cat_created"));
                responce.setCat_modified(object.getString("cat_modified"));


                catagoriesArrayList.add(responce);

            }

            return catagoriesArrayList;


        } catch (JSONException e) {
            e.printStackTrace();
            return catagoriesArrayList;
        }
    }


    public ArrayList<TripHistory> getTripHistory(String json) {
        ArrayList<TripHistory> catagoriesArrayList = new ArrayList<TripHistory>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("response");

            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);

                TripHistory responce = new TripHistory();


                responce.setTripId(object.getString("trip_id"));
                responce.setTripDate(object.getString("trip_date"));
                responce.setDriverId(object.getString("driver_id"));
                responce.setUserId(object.getString("user_id"));
                responce.setTripFromLoc(object.getString("trip_from_loc"));
                responce.setTripToLoc(object.getString("trip_to_loc"));
                responce.setTripDistance(object.getString("trip_distance"));
                //responce.setTripFare(object.getString("trip_fare"));
                responce.setTripPickupTime(object.getString("trip_pickup_time"));
                responce.setTripDropTime(object.getString("trip_drop_time"));
                responce.setTripReason(object.getString("trip_reason"));
                responce.setTripValidity(object.getString("trip_validity"));
                responce.setTripStatus(object.getString("trip_status"));
                responce.setTripRating(object.getString("trip_rating"));
                responce.setTripScheduledPickLat(object.getString("trip_scheduled_pick_lat"));
                responce.setTripScheduledPickLng(object.getString("trip_scheduled_pick_lng"));
                responce.setTripActualPickLat(object.getString("trip_actual_pick_lat"));
                responce.setTripActualPickLng(object.getString("trip_actual_pick_lng"));
                responce.setTripScheduledDropLat(object.getString("trip_scheduled_drop_lat"));
                responce.setTripScheduledDropLng(object.getString("trip_scheduled_drop_lng"));
                responce.setTripActualDropLat(object.getString("trip_actual_drop_lat"));
                responce.setTripActualDropLng(object.getString("trip_actual_drop_lng"));
                responce.setTripSearchedAddr(object.getString("trip_searched_addr"));
                responce.setTripSearchResultAddr(object.getString("trip_search_result_addr"));
                responce.setTripPayMode(object.getString("trip_pay_mode"));
                responce.setTripPayAmount(object.getString("trip_pay_amount"));
                responce.setTripPayDate(object.getString("trip_pay_date"));
                responce.setTripPayStatus(object.getString("trip_pay_status"));
                responce.setTripDriverCommision(object.getString("trip_driver_commision"));
                responce.setTripCreated(object.getString("trip_created"));
                responce.setTripModified(object.getString("trip_modified"));
                responce.setPromoId(object.getString("promo_id"));
                responce.setTrip_promo_amt(object.getString("trip_promo_amt"));
                responce.setTripPromoCode(object.getString("trip_promo_code"));
                responce.setTripPromoAmt(object.getString("trip_promo_amt"));
                responce.setTaxAmount(object.getString("tax_amt"));

                JSONObject object1 = object.getJSONObject("User");
                DriverInfo responceUser = new DriverInfo();
                responceUser.setUserId(object1.getString("user_id"));
                responceUser.setApiKey(object1.getString("api_key"));
//                responceUser.setGroupId(object1.getString("group_id"));
//                responceUser.setUsername(object1.getString("username"));
                responceUser.setUName(object1.getString("u_name"));
                responceUser.setUFname(object1.getString("u_fname"));
                responceUser.setULname(object1.getString("u_lname"));
                responceUser.setUEmail(object1.getString("u_email"));
                responceUser.setUPhone(object1.getString("u_phone"));
                responceUser.setUAddress(object1.getString("u_address"));
                responceUser.setUCity(object1.getString("u_city"));
                responceUser.setUState(object1.getString("u_state"));
                responceUser.setUCity(object1.getString("u_country"));
                responceUser.setUZip(object1.getString("u_zip"));
                responceUser.setULat(object1.getString("u_lat"));
                responceUser.setULng(object1.getString("u_lng"));
                responceUser.setUDegree(object1.getString("u_degree"));
                responceUser.setImageId(object1.getString("image_id"));
                responceUser.setUDeviceType(object1.getString("u_device_type"));
                responceUser.setUIsAvailable(object1.getString("u_is_available"));
//                responceUser.setActive(object1.getString("active"));
                responceUser.setUCreated(object1.getString("u_created"));
                responceUser.setUModified(object1.getString("u_modified"));
                responceUser.setUProfileImagePath(object1.getString("u_profile_image_path"));
                responce.setUser(responceUser);
//
//
                JSONObject object2 = object.getJSONObject("Driver");
                DriverInfo responceDriver = new DriverInfo();
                responceDriver.setUserId(object2.getString("driver_id"));
                responceDriver.setApiKey(object2.getString("api_key"));
                responceDriver.setUName(object2.getString("d_name"));
                responceDriver.setUFname(object2.getString("d_fname"));
                responceDriver.setULname(object2.getString("d_lname"));
                responceDriver.setUEmail(object2.getString("d_email"));
                responceDriver.setUPhone(object2.getString("d_phone"));
                responceDriver.setUAddress(object2.getString("d_address"));
                responceDriver.setUCity(object2.getString("d_city"));
                responceDriver.setUState(object2.getString("d_state"));
                responceDriver.setUCity(object2.getString("d_country"));
                responceDriver.setUZip(object2.getString("d_zip"));
                responceDriver.setCar_name(object2.getString("car_name"));
                responceDriver.setCategory_id(object2.getString("category_id"));
                responceDriver.setD_profile_image_path(object2.getString("d_profile_image_path"));
                responceDriver.setULat(object2.getString("d_lat"));
                responceDriver.setULng(object2.getString("d_lng"));
                responceDriver.setUDegree(object2.getString("d_degree"));
                responceDriver.setImageId(object2.getString("image_id"));
                responceDriver.setUDeviceType(object2.getString("d_device_type"));
                responceDriver.setUIsAvailable(object2.getString("d_is_available"));
                responceDriver.setUCreated(object2.getString("d_created"));
                responceDriver.setUModified(object2.getString("d_modified"));
                responceDriver.setUProfileImagePath(object2.getString("d_profile_image_path"));
                responceDriver.setDriver_id(object2.getString("driver_id"));
                responceDriver.setCar_reg_no(object2.getString("car_reg_no"));
                responce.setDriver(responceDriver);

                catagoriesArrayList.add(responce);
            }

            return catagoriesArrayList;


        } catch (JSONException e) {
            e.printStackTrace();
            return catagoriesArrayList;
        }
    }

    public ArrayList<GetDriverInfo> getDriverInfos(String json,boolean isFatchProfile) {
        ArrayList<GetDriverInfo> arrayList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("response");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                GetDriverInfo driverInfo = new GetDriverInfo();
                driverInfo.setDriver_id(jsonObject.getString("driver_id"));
                driverInfo.setApi_key(jsonObject.getString("api_key"));
                driverInfo.setD_name(jsonObject.getString("d_name"));
                driverInfo.setD_fname(jsonObject.getString("d_fname"));
                driverInfo.setApi_key(jsonObject.getString("d_lname"));
                driverInfo.setD_email(jsonObject.getString("d_email"));
                driverInfo.setD_password(jsonObject.getString("d_password"));
                driverInfo.setD_phone(jsonObject.getString("d_phone"));
                driverInfo.setD_address(jsonObject.getString("d_address"));
                driverInfo.setD_city(jsonObject.getString("d_city"));
                driverInfo.setD_state(jsonObject.getString("d_state"));
                driverInfo.setD_country(jsonObject.getString("d_country"));
                driverInfo.setD_zip(jsonObject.getString("d_zip"));
                driverInfo.setD_lat(jsonObject.getString("d_lat"));
                driverInfo.setD_lng(jsonObject.getString("d_lng"));
                driverInfo.setD_degree(jsonObject.getString("d_degree"));
                driverInfo.setImage_id(jsonObject.getString("image_id"));
                driverInfo.setD_license_id(jsonObject.getString("d_license_id"));
                driverInfo.setD_device_type(jsonObject.getString("d_device_type"));
                driverInfo.setD_device_token(jsonObject.getString("d_device_token"));
                driverInfo.setD_rating(jsonObject.getString("d_rating"));
                driverInfo.setD_rating_count(jsonObject.getString("d_rating_count"));
                driverInfo.setD_is_available(jsonObject.getString("d_is_available"));
                driverInfo.setD_is_verified(jsonObject.getString("d_is_verified"));
                driverInfo.setD_created(jsonObject.getString("d_created"));
                driverInfo.setD_modified(jsonObject.getString("d_modified"));
                driverInfo.setD_profile_image_path(jsonObject.getString("d_profile_image_path"));
                driverInfo.setD_license_image_path(jsonObject.getString("d_license_image_path"));
                driverInfo.setD_rc_image_path(jsonObject.getString("d_rc_image_path"));
                driverInfo.setCategory_id(jsonObject.getString("category_id"));
                driverInfo.setCar_name(jsonObject.getString("car_name"));
                driverInfo.setCar_desc(jsonObject.getString("car_desc"));
                driverInfo.setCar_reg_no(jsonObject.getString("car_reg_no"));
                driverInfo.setCar_currency(jsonObject.getString("car_currency"));
                driverInfo.setCar_fare_per_km(jsonObject.getString("car_fare_per_km"));
                driverInfo.setCar_fare_per_min(jsonObject.getString("car_fare_per_min"));
                driverInfo.setCar_created(jsonObject.getString("car_created"));
                driverInfo.setCar_modified(jsonObject.getString("car_modified"));
                try {
                    driverInfo.setDistance(jsonObject.getString("distance"));
                } catch (Exception e) {

                }
                if (jsonObject.getString("d_is_available").equals("1") || jsonObject.getString("d_is_available") == "1") {
                    arrayList.add(driverInfo);
                }
                if(isFatchProfile){
                    arrayList.add(driverInfo);
                }
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return arrayList;
        }


    }


    public TripHistory getTripDetails(String json) {
        TripHistory responce = new TripHistory();

        try {
            JSONObject obj = new JSONObject(json);
            JSONObject object = obj.getJSONObject("response");
            responce.setTripId(object.getString("trip_id"));
            responce.setTripStatus(object.getString("trip_status"));
            responce.setTripDate(object.getString("trip_date"));
            responce.setDriverId(object.getString("driver_id"));
            responce.setUserId(object.getString("user_id"));
            responce.setTripFromLoc(object.getString("trip_from_loc"));
            responce.setTripToLoc(object.getString("trip_to_loc"));
            responce.setTripDistance(object.getString("trip_distance"));
         //   responce.setTripFare(object.getString("trip_fare"));
            responce.setTripPickupTime(object.getString("trip_pickup_time"));
            responce.setTripDropTime(object.getString("trip_drop_time"));
            responce.setTripReason(object.getString("trip_reason"));
            responce.setTripValidity(object.getString("trip_validity"));
            responce.setTripRating(object.getString("trip_rating"));
            responce.setTripScheduledPickLat(object.getString("trip_scheduled_pick_lat"));
            responce.setTripScheduledPickLng(object.getString("trip_scheduled_pick_lng"));
            responce.setTripActualPickLat(object.getString("trip_actual_pick_lat"));
            responce.setTripActualPickLng(object.getString("trip_actual_pick_lng"));
            responce.setTripScheduledDropLat(object.getString("trip_scheduled_drop_lat"));
            responce.setTripScheduledDropLng(object.getString("trip_scheduled_drop_lng"));
            responce.setTripActualDropLat(object.getString("trip_actual_drop_lat"));
            responce.setTripActualDropLng(object.getString("trip_actual_drop_lng"));
            responce.setTripSearchedAddr(object.getString("trip_searched_addr"));
            responce.setTripSearchResultAddr(object.getString("trip_search_result_addr"));
            responce.setTripPayMode(object.getString("trip_pay_mode"));
            responce.setTripPayAmount(object.getString("trip_pay_amount"));
            responce.setTripPayDate(object.getString("trip_pay_date"));
            responce.setTripPayStatus(object.getString("trip_pay_status"));
            responce.setTripDriverCommision(object.getString("trip_driver_commision"));
            responce.setTripCreated(object.getString("trip_created"));
            responce.setTripModified(object.getString("trip_modified"));
            responce.setPromoId(object.getString("promo_id"));
            responce.setTripPromoCode(object.getString("trip_promo_code"));
            responce.setTripPromoAmt(object.getString("trip_promo_amt"));
            responce.setTrip_promo_amt(object.getString("trip_promo_amt"));
            responce.setTaxAmount(object.getString("tax_amt"));


            JSONObject object1 = object.getJSONObject("User");
            DriverInfo responceUser = new DriverInfo();
            responceUser.setUserId(object1.getString("user_id"));
            responceUser.setApiKey(object1.getString("api_key"));
            responceUser.setGroupId(object1.getString("group_id"));
            responceUser.setUsername(object1.getString("username"));
            responceUser.setUName(object1.getString("u_name"));
            responceUser.setUFname(object1.getString("u_fname"));
            responceUser.setULname(object1.getString("u_lname"));
            responceUser.setUEmail(object1.getString("u_email"));
            responceUser.setUPhone(object1.getString("u_phone"));
            responceUser.setUAddress(object1.getString("u_address"));
            responceUser.setUCity(object1.getString("u_city"));
            responceUser.setUState(object1.getString("u_state"));
            responceUser.setUCity(object1.getString("u_country"));
            responceUser.setUZip(object1.getString("u_zip"));
            responceUser.setULat(object1.getString("u_lat"));
            responceUser.setULng(object1.getString("u_lng"));
            responceUser.setUDegree(object1.getString("u_degree"));
            responceUser.setImageId(object1.getString("image_id"));
            responceUser.setUDeviceType(object1.getString("u_device_type"));
            responceUser.setUIsAvailable(object1.getString("u_is_available"));
//            responceUser.setActive(object1.getString("active"));
            responceUser.setUCreated(object1.getString("u_created"));
            responceUser.setUModified(object1.getString("u_modified"));
            responceUser.setUProfileImagePath(object1.getString("u_profile_image_path"));
            responce.setUser(responceUser);

            JSONObject object2 = object.getJSONObject("Driver");
            DriverInfo responceDriver = new DriverInfo();
            responceDriver.setUserId(object2.getString("driver_id"));
            responceDriver.setApiKey(object2.getString("api_key"));
            responceDriver.setUName(object2.getString("d_name"));
            responceDriver.setUFname(object2.getString("d_fname"));
            responceDriver.setULname(object2.getString("d_lname"));
            responceDriver.setUEmail(object2.getString("d_email"));
            responceDriver.setUPhone(object2.getString("d_phone"));
            responceDriver.setUAddress(object2.getString("d_address"));
            responceDriver.setUCity(object2.getString("d_city"));
            responceDriver.setUState(object2.getString("d_state"));
            responceDriver.setUCity(object2.getString("d_country"));
            responceDriver.setUZip(object2.getString("d_zip"));
            responceDriver.setULat(object2.getString("d_lat"));
            responceDriver.setULng(object2.getString("d_lng"));
            responceDriver.setUDegree(object2.getString("d_degree"));
            responceDriver.setImageId(object2.getString("image_id"));
            responceDriver.setUDeviceType(object2.getString("d_device_type"));
            responceDriver.setUIsAvailable(object2.getString("d_is_available"));
//            responceDriver.setActive(object2.getString("active"));
            responceDriver.setUCreated(object2.getString("d_created"));
            responceDriver.setUModified(object2.getString("d_modified"));
            responceDriver.setUProfileImagePath(object2.getString("d_profile_image_path"));
            responceDriver.setCategory_id(object2.getString("category_id"));
            responceDriver.setD_profile_image_path(object2.getString("d_profile_image_path"));
            responceDriver.setCar_name(object2.getString("car_name"));
            responceDriver.setDriver_id(object2.getString("driver_id"));
            responceDriver.setCar_reg_no(object2.getString("car_reg_no"));
            responce.setDriver(responceDriver);
            return responce;

        } catch (JSONException e) {
            e.printStackTrace();

            return responce;
        }
    }

    public TripHistory parseSingleTrip(String json) {
        TripHistory responce = new TripHistory();
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.getJSONArray("response");
            JSONObject object = arr.getJSONObject(0);

            responce.setTripId(object.getString("trip_id"));
            responce.setTripStatus(object.getString("trip_status"));
            responce.setTripDate(object.getString("trip_date"));
            responce.setDriverId(object.getString("driver_id"));
            responce.setUserId(object.getString("user_id"));
            responce.setTripFromLoc(object.getString("trip_from_loc"));
            responce.setTripToLoc(object.getString("trip_to_loc"));
            responce.setTripDistance(object.getString("trip_distance"));
          //  responce.setTripFare(object.getString("trip_fare"));
            responce.setTripPickupTime(object.getString("trip_pickup_time"));
            responce.setTripDropTime(object.getString("trip_drop_time"));
            responce.setTripReason(object.getString("trip_reason"));
            responce.setTripValidity(object.getString("trip_validity"));
            responce.setTripRating(object.getString("trip_rating"));
            responce.setTripScheduledPickLat(object.getString("trip_scheduled_pick_lat"));
            responce.setTripScheduledPickLng(object.getString("trip_scheduled_pick_lng"));
            responce.setTripActualPickLat(object.getString("trip_actual_pick_lat"));
            responce.setTripActualPickLng(object.getString("trip_actual_pick_lng"));
            responce.setTripScheduledDropLat(object.getString("trip_scheduled_drop_lat"));
            responce.setTripScheduledDropLng(object.getString("trip_scheduled_drop_lng"));
            responce.setTripActualDropLat(object.getString("trip_actual_drop_lat"));
            responce.setTripActualDropLng(object.getString("trip_actual_drop_lng"));
            responce.setTripSearchedAddr(object.getString("trip_searched_addr"));
            responce.setTripSearchResultAddr(object.getString("trip_search_result_addr"));
            responce.setTripPayMode(object.getString("trip_pay_mode"));
            responce.setTripPayAmount(object.getString(Constants.Keys.TRIP_PAY_AMOUNT));
            responce.setTripPayDate(object.getString("trip_pay_date"));
            responce.setTripPayStatus(object.getString("trip_pay_status"));
            responce.setTripDriverCommision(object.getString("trip_driver_commision"));
            responce.setTripCreated(object.getString("trip_created"));
            responce.setTripModified(object.getString("trip_modified"));
            responce.setPromoId(object.getString("promo_id"));
            responce.setTripPromoCode(object.getString("trip_promo_code"));
            responce.setTripPromoAmt(object.getString("trip_promo_amt"));
            responce.setTrip_promo_amt(object.getString("trip_promo_amt"));
            responce.setTaxAmount(object.getString("tax_amt"));


            JSONObject object1 = object.getJSONObject("User");
            DriverInfo responceUser = new DriverInfo();
            responceUser.setUserId(object1.getString("user_id"));
            responceUser.setApiKey(object1.getString("api_key"));
            responceUser.setGroupId(object1.getString("group_id"));
            responceUser.setUsername(object1.getString("username"));
            responceUser.setUName(object1.getString("u_name"));
            responceUser.setUFname(object1.getString("u_fname"));
            responceUser.setULname(object1.getString("u_lname"));
            responceUser.setUEmail(object1.getString("u_email"));
            responceUser.setUPhone(object1.getString("u_phone"));
            responceUser.setUAddress(object1.getString("u_address"));
            responceUser.setUCity(object1.getString("u_city"));
            responceUser.setUState(object1.getString("u_state"));
            responceUser.setUCity(object1.getString("u_country"));
            responceUser.setUZip(object1.getString("u_zip"));
            responceUser.setULat(object1.getString("u_lat"));
            responceUser.setULng(object1.getString("u_lng"));
            responceUser.setUDegree(object1.getString("u_degree"));
            responceUser.setImageId(object1.getString("image_id"));
            responceUser.setUDeviceType(object1.getString("u_device_type"));
            responceUser.setUIsAvailable(object1.getString("u_is_available"));
//            responceUser.setActive(object1.getString("active"));
            responceUser.setUCreated(object1.getString("u_created"));
            responceUser.setUModified(object1.getString("u_modified"));
            responceUser.setUProfileImagePath(object1.getString("u_profile_image_path"));
            responce.setUser(responceUser);


            JSONObject object2 = object.getJSONObject("Driver");
            DriverInfo responceDriver = new DriverInfo();
            responceDriver.setUserId(object2.getString("driver_id"));
            responceDriver.setApiKey(object2.getString("api_key"));
            responceDriver.setUName(object2.getString("d_name"));
            responceDriver.setUFname(object2.getString("d_fname"));
            responceDriver.setULname(object2.getString("d_lname"));
            responceDriver.setUEmail(object2.getString("d_email"));
            responceDriver.setUPhone(object2.getString("d_phone"));
            responceDriver.setUAddress(object2.getString("d_address"));
            responceDriver.setUCity(object2.getString("d_city"));
            responceDriver.setUState(object2.getString("d_state"));
            responceDriver.setUCity(object2.getString("d_country"));
            responceDriver.setUZip(object2.getString("d_zip"));
            responceDriver.setULat(object2.getString("d_lat"));
            responceDriver.setULng(object2.getString("d_lng"));
            responceDriver.setUDegree(object2.getString("d_degree"));
            responceDriver.setImageId(object2.getString("image_id"));
            responceDriver.setUDeviceType(object2.getString("d_device_type"));
            responceDriver.setUDeviceToken(object2.getString("d_device_token"));
            responceDriver.setUIsAvailable(object2.getString("d_is_available"));
//            responceDriver.setActive(object2.getString("active"));
            responceDriver.setUCreated(object2.getString("d_created"));
            responceDriver.setUModified(object2.getString("d_modified"));
            responceDriver.setUProfileImagePath(object2.getString("d_profile_image_path"));
            responceDriver.setD_rating_count(object2.getString("d_rating_count"));
            responceDriver.setD_rating(object2.getString("d_rating"));
            responceDriver.setCategory_id(object2.getString("category_id"));
            responceDriver.setD_profile_image_path(object2.getString("d_profile_image_path"));
            responceDriver.setCar_name(object2.getString("car_name"));
            responceDriver.setDriver_id(object2.getString("driver_id"));
            responceDriver.setCar_reg_no(object2.getString("car_reg_no"));
            responce.setDriver(responceDriver);
            return responce;

        } catch (JSONException e) {
            e.printStackTrace();
            return responce;
        }


    }


}
