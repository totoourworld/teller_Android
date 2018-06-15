package com.rider.xenia;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.app_controller.AppController;
import com.custom.CustomProgressDialog;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebService;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Settings extends Activity {

    @BindView(R.id.settg_contact1)
    EditText contact1;
    @BindView(R.id.settg_contact2)
    EditText contact2;
    @BindView(R.id.settg_contact3)
    EditText contact3;
    @BindView(R.id.settg_email1)
    EditText email1;
    @BindView(R.id.settg_email2)
    EditText email2;
    @BindView(R.id.settg_email3)
    EditText email3;
    @BindView(R.id.settg_bt_update_contact)
    Button btUpdateContact;
    @BindView(R.id.settg_bt_update_email)
    Button btUpdateEmail;


    protected static final String TAG = "Settings";
    private CustomProgressDialog dialog;

    Button Edit, Signout;

    private AppController controller;
    private DriverInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().hide();
        ButterKnife.bind(this);
        controller = (AppController) getApplication();
        ParseJson parseJson = new ParseJson(this);
        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        setUserInfo();

    }

    private void setUserInfo() {

        contact1.setText(Validations.checkNull(userInfo.getEmergency_contact_1())?userInfo.getEmergency_contact_1():"");
        contact2.setText(Validations.checkNull(userInfo.getEmergency_contact_2())?userInfo.getEmergency_contact_2():"");
        contact3.setText(Validations.checkNull(userInfo.getEmergency_contact_3())?userInfo.getEmergency_contact_3():"");
        email1.setText(Validations.checkNull(userInfo.getEmergency_email_1())?userInfo.getEmergency_email_1():"");
        email2.setText(Validations.checkNull(userInfo.getEmergency_email_2())?userInfo.getEmergency_email_2():"");
        email3.setText(Validations.checkNull(userInfo.getEmergency_email_3())?userInfo.getEmergency_email_3():"");
    }



    @OnClick(R.id.settg_bt_update_contact)
    public void onUpdateContact(View view) {
        if (!Validations.validateContactForm(Settings.this,contact1,contact2,contact3)) {
            return;
        }
        showProgress();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put("user_id", controller.pref.getUserID());
        params.put("emergency_contact_1", contact1.getText().toString());
        params.put("emergency_contact_2", contact2.getText().toString());
        params.put("emergency_contact_3", contact3.getText().toString());
        WebService.excuteRequest(this, params,Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                hideProgress();
                if (isUpdate) {

                    if (data != null) {
                        controller.pref.saveUserLogin(data.toString());
                        ParseJson parseJson = new ParseJson(getApplication());
                        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
                        setUserInfo();
                    } else {
                        Toast.makeText(getApplication(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (error == null) {
                        Toast.makeText(Settings.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }


    @OnClick(R.id.settg_bt_update_email)
    public void onUpdateEmail(View view) {
        if (!Validations.validateEmailForm(Settings.this,email1,email2,email3)) {
            return;

        }
        showProgress();

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put("user_id", controller.pref.getUserID());
        params.put("emergency_email_1", email1.getText().toString());
        params.put("emergency_email_2", email2.getText().toString());
        params.put("emergency_email_3", email3.getText().toString());
        WebService.excuteRequest(this, params,Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                hideProgress();
                if (isUpdate) {

                    if (data != null) {
                        controller.pref.saveUserLogin(data.toString());
                        ParseJson parseJson = new ParseJson(getApplication());
                        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
                        setUserInfo();
                    } else {
                        Toast.makeText(getApplication(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (error == null) {
                        Toast.makeText(Settings.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }




    @OnClick(R.id.settg_back)
    public void onBack(View view) {
        finish();
    }








    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




    private void showProgress() {
        dialog = new CustomProgressDialog(this);
        dialog.showDialog();

    }

    private void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }




}
