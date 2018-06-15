package com.utils;

import android.app.ProgressDialog;
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
import com.rider.xenia.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by devin on 2017-01-04.
 */
public class ApiHelper {

    Context context;
    Controller controller;


    public ApiHelper(Context context) {
        this.context=context;
        controller= (Controller) context.getApplicationContext();
    }

    public void callApi( final Map<String, String> params) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait....");
        dialog.setTitle("");
        dialog.show();
        dialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Urls.URL_USER_REGISTRATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        controller.setApiResponce(response);
                        Toast.makeText(context, "ress"+response, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if(error instanceof NoConnectionError)
                            Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();
                        else if(error instanceof ServerError){

                            String d= new  String(error.networkResponse.data);
                            try {
                                JSONObject jso= new JSONObject(d);
                                Toast.makeText(context, jso.getString("message"),Toast.LENGTH_LONG).show();
                                // signUpFacebook();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
}
