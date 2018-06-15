package com.rider.xenia;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.custom.CustomProgressDialog;
import com.grepix.grepixutils.WebService;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by devin on 2017-10-06.
 */

public class VerifyForFacebookLogin extends Activity {
    private CustomProgressDialog progressDialog;
    private Controller controller;

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.mobile_no)
    EditText mobileNo;
    @BindView(R.id.edit_last_name)
    EditText last_name;
    @BindView(R.id.name)
    EditText first_name;
    private String facebookId;
    private String User_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_veruify_account);
        progressDialog=new CustomProgressDialog(VerifyForFacebookLogin.this);
        controller = (Controller) getApplicationContext();
        ButterKnife.bind(this);
        JSONObject jsonObject=controller.getFacebookResponce();

            try {
                if (jsonObject.has("email")) {
                 email.setText(jsonObject.getString("email"));
                }
                if (jsonObject.has("name")) {
                    String name = jsonObject.getString("name");
                    if (name != null) {
                        String names[] = name.split(" ");
                        if (names.length > 1) {
                            first_name.setText(names[0]);
                            last_name.setText(names[1]);
                        } else {
                            first_name.setText(name);
                        }
                    }
                }
                facebookId=jsonObject.getString("id");

            } catch (JSONException e) {
                e.printStackTrace();
            }



    }

    @OnClick(R.id.cancel)
    public void  backfromScreen(){
        finish();
    }

    @OnClick(R.id.facebookbns)
    public void  continuefacebookSignUp(){
        facebookSignUp();
    }
    boolean isCalling;
    private void facebookSignUp() {

        if (isCalling) {
            return;
        }


        if (!validateForm()) {
            return;
        }
        if (net_connection_check()) {
            facebookRegister();
        }
    }

    private String getSaveDiceToken() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(com.app.Config.SHARED_PREF, 0);
        return pref.getString("regId", null);
    }

    public void facebookRegister() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.showDialog();
            }
        });
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("u_email", email.getText().toString());
            params.put("u_fname",first_name.getText().toString() );
            params.put("u_lname",last_name.getText().toString());
            params.put("u_fbid", facebookId);
            params.put("u_phone", mobileNo.getText().toString());
            params.put("u_password", "test");
            String deviceToken = getSaveDiceToken();
            if (deviceToken != null) {
                params.put("u_device_token", deviceToken);
                params.put("u_device_type", "Android");
            }

            System.out.println("Params : " + params);
            WebService.excuteRequest(controller, params, Constants.Urls.URL_USER_REGISTRATION, new WebService.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    progressDialog.dismiss();
                    if (isUpdate) {

                        login_Progress(data.toString());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, getString(R.string.response_error) + e, Toast.LENGTH_LONG).show();
        }

    }



    public int login_Progress(String loginResponse) {
        ParseJson parseJson = new ParseJson(VerifyForFacebookLogin.this);
        DriverInfo responceLogin = parseJson.ParseLoginResponce(loginResponse.toString());
        controller.pref.saveUserLogin(loginResponse.toString());
        controller.pref.setUserApiKey(responceLogin.getApiKey());
        controller.pref.saveUserID(responceLogin.getUserId());
        controller.pref.saveEmail(email.getText().toString());
        controller.pref.savePassword("test");
        User_id = responceLogin.getUserId();
        controller.pref.saveIsLoginSucess(true);

        Intent main = new Intent(getApplicationContext(), MainScreenActivity.class);
        main.putExtra("userid", User_id);
        main.putExtra("whologin",responceLogin.getUsername());
        main.putExtra("password", "test");
        startActivity(main);
        finish();
        return 100;
    }

    private boolean validateForm() {
        boolean isValid = true;
        if (!isValidEmail(email.getText().toString())) {
            email.setError(getString(R.string.invalid_email_id));
            isValid = false;
        } else {
            email.setError(null);
        }

        if (mobileNo.getText().toString().length() < 6||mobileNo.getText().toString().length()>12) {
            isValid = false;
            mobileNo.setError(getString(R.string.please_enter_your_valid_number));
        } else {
            mobileNo.setError(null);
        }

        if (last_name.getText().toString().length() == 0) {
            last_name.setError(getText(R.string.required));
            isValid = false;
        } else {
            last_name.setError(null);
        }

        if (first_name.getText().toString().length() == 0) {
            first_name.setError(getText(R.string.required));
            isValid = false;
        } else {
           first_name.setError(null);
        }
        return isValid;
    }

    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
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
        window.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

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
