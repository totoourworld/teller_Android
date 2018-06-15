package com.grepix.grepixutils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import com.grepix.grepixutils.R;

import java.util.Map;

/**
 * Created by devin on 2017-10-13.
 */

public class WebService {
    public interface DeviceTokenServiceListener {
        void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error);
    }
    public static void excuteRequest(final Context contaxt, final Map<String, String> params, String url, final DeviceTokenServiceListener listener) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onUpdateDeviceTokenOnServer(response, response != null, null);
                        System.out.println("Success : " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("VolleyError : " + error);
                        if (error instanceof NoConnectionError)
                            Toast.makeText(contaxt, "No internet available", Toast.LENGTH_SHORT).show();
                        else if (error instanceof ServerError) {

                            String d = new String(error.networkResponse.data);
                            try {
                                JSONObject jso = new JSONObject(d);
                                Toast.makeText(contaxt, jso.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onUpdateDeviceTokenOnServer(null, false, error);
                            }

                        }
                        listener.onUpdateDeviceTokenOnServer(null, false, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(contaxt);
        requestQueue.add(stringRequest);
    }
}
