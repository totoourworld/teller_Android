package com.rider.xenia;

import com.android.volley.VolleyError;
import com.app_controller.AppController;
import com.grepix.grepixutils.WebService;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by grepixinfotech on 05/04/17.
 */

public class DeviceTokenService {

    public interface DeviceTokenServiceListener {
        void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error);
    }

    public static void sendDeviceTokenToServer(final AppController controller, final String deviceToken, final DeviceTokenServiceListener listener) {
        ParseJson parseJson = new ParseJson(controller);
        DriverInfo responceLogin = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, responceLogin.getApiKey());
        params.put(Constants.Keys.USER_ID, responceLogin.getUserId());
        params.put(Constants.Keys.U_DEVICE_TYPE, "Android");
        params.put(Constants.Keys.U_DEVICE_TOKEN, deviceToken);
        params.put("u_is_available", String.valueOf(1));
        WebService.excuteRequest(controller, params, Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                listener.onUpdateDeviceTokenOnServer(data,isUpdate,error);
            }
        });
    }

    public static void sendDeviceTokenToServer(final AppController controller, final String deviceToken) {
        ParseJson parseJson = new ParseJson(controller);
        DriverInfo responceLogin = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, responceLogin.getApiKey());
        params.put(Constants.Keys.USER_ID, responceLogin.getUserId());
        params.put(Constants.Keys.U_DEVICE_TYPE, "Android");
        params.put(Constants.Keys.U_DEVICE_TOKEN, deviceToken);
        WebService.excuteRequest(controller, params, Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
            }
        });
    }

    public static void loginWithFacebook(final AppController controller, String fbId  , final DeviceTokenServiceListener listener) {
        ParseJson parseJson = new ParseJson(controller);
        Map<String, String> params = new HashMap<String, String>();
        params.put("u_fbid",fbId);
        WebService.excuteRequest(controller, params, Constants.Urls.URL_USER_FB_LOGIN, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                listener.onUpdateDeviceTokenOnServer(data,isUpdate,error);
            }
        });
    }

    public static void logout(final AppController controller , final DeviceTokenServiceListener listener) {
        ParseJson parseJson = new ParseJson(controller);
        DriverInfo responceLogin = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, responceLogin.getApiKey());
        params.put(Constants.Keys.USER_ID, responceLogin.getUserId());
        params.put("u_is_available", String.valueOf(0));
        WebService.excuteRequest(controller, params, Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                listener.onUpdateDeviceTokenOnServer(data,isUpdate,error);
            }
        });
    }

}
