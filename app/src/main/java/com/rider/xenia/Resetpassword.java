package com.rider.xenia;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.custom.CustomProgressDialog;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebService;
import com.utils.Constants;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Resetpassword extends Activity {

    protected static final String TAG = "Resetpassword Activity";


    @BindView(R.id.rp_email)
    EditText email;

    private CustomProgressDialog dialog;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog=new CustomProgressDialog(Resetpassword.this);

        setContentView(R.layout.activity_resetpassword);

        ButterKnife.bind(this);


        getActionBar().hide();

    }

    @OnClick(R.id.rp_go_back)
    public void goBack(View view) {
        onBackPressed();
    }


    @OnClick(R.id.rp_bt_reset_password)

    public void onResetPassword(View view) {
        String emailString = email.getText().toString();
        if (Validations.isValidateForgotPass(Resetpassword.this,email)) {

            showProgress();
            HashMap<String, String> params = new HashMap<>();
            params.put("u_email", emailString);
            WebService.excuteRequest(this, params, Constants.Urls.URL_RESET_PASSWORD, new WebService.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    hideProgress();
                    if (isUpdate) {
                        finish();
                    } else {
                        Toast.makeText(Resetpassword.this, R.string.check_your_emailId, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }




    public void showProgress() {
        dialog.showDialog();
    }

    public void hideProgress() {
        dialog.dismiss();

    }



}
