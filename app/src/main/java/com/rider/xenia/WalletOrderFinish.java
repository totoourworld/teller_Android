package com.rider.xenia;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.custom.CustomProgressDialog;
import com.grepix.grepixutils.WebService;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WalletOrderFinish extends FragmentActivity implements View.OnClickListener {

    private static String INDEX_TAG = "CARD_INDEX";
    private static String JOIN_URL  = "https://www.six38ppn.com/api/v1/card_holders?";
    private ImageView imgCardHold, imgCancel;
    private TextView txtTitle, txtContent, txtJoinInvite, txtLater;

    private int[] myImageList = new int[]{R.drawable.card_hold_purple_naira, R.drawable.card_hold_purple_euro,
            R.drawable.card_hold_purple_dollar, R.drawable.card_hold_purple_rand };

    private boolean isFinished = false;
    private Controller controller;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_order_finish);

        getActionBar().hide();

        imgCardHold = (ImageView) findViewById(R.id.imgCardHold);
        txtTitle = (TextView) findViewById(R.id.txtFinishTitle);
        txtContent = (TextView) findViewById(R.id.txtFinishContent);

        imgCancel = (ImageView) findViewById(R.id.recancel);
        imgCancel.setOnClickListener(this);

        txtJoinInvite = (TextView) findViewById(R.id.txtJoin);
        txtJoinInvite.setOnClickListener(this);

        txtLater   = (TextView) findViewById(R.id.txtGetLater);
        txtLater.setOnClickListener(this);

        controller = (Controller) getApplication();

        init();
    }

    private void init()
    {
        int cardIndex = getIntent().getIntExtra(INDEX_TAG, 0);
        imgCardHold.setImageResource(myImageList[cardIndex]);

        txtTitle.setText(R.string.card_order_almost_title);
        txtContent.setText(R.string.card_order_almost_content);

        txtJoinInvite.setText(R.string.join_list);

        isFinished = false;

        progressDialog=new CustomProgressDialog(WalletOrderFinish.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.recancel:
                onBackPressed();
                break;

            case R.id.txtJoin:
                if(!isFinished)         //Join
                    sendJoinRequest();
                else                    //Invite friend
                {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent
                            .putExtra(
                                    android.content.Intent.EXTRA_TEXT,
                                    getResources().getString(R.string.invite_msg)
                                            + " "
                                            + getResources().getString(
                                            R.string.app_name)
                                            + " - "
                                            + "https://play.google.com/store/apps/details?id="
                                            + getPackageName());

                    startActivity(Intent.createChooser(sharingIntent,
                               getResources().getString(R.string.invite_friend)
                    ));
                }
                break;

            case R.id.txtGetLater:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    private void sendJoinRequest()
    {
        String url = JOIN_URL;

        ParseJson parseJson = new ParseJson(this);
        DriverInfo userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        String userEmail = userInfo.getUEmail();
        String userName  = userInfo.getUFname() + " " + userInfo.getULname();

        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", "864a94657f70285adb81c9a9ed292826");
        params.put("card_requests[request_from]", "xenia_teller");
        params.put("card_requests[email]=", userEmail);
        params.put("card_requests[name]", userName);

        progressDialog.showDialog();

        WebService.excuteRequest(this, params, url, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    progressDialog.dismiss();
                    parseJoinResponse(data.toString());
                } else {
                    progressDialog.dismiss();
                    if (error == null) {
                        Toast.makeText(WalletOrderFinish.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void parseJoinResponse(final String response)
    {
        Log.e("Join Response : ", response);

        JSONArray join_response = null;
        JSONObject obj = null;
        try {
            join_response = new JSONArray(response);
            obj = join_response.getJSONObject(0);
            String status = obj.getString("status");
            if (status.equals("success"))
            {
                int id = obj.getInt("id");
                updateText(id);
            }
            else
            {
                Toast.makeText(this, "Something goes wrong.Please try again.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateText(int id)
    {
        txtTitle.setText(R.string.card_order_finish_title);

        StringBuilder builder = new StringBuilder();
        builder.append(getResources().getString(R.string.card_order_finish_prefix));
        builder.append(id);
        builder.append(" ");
        builder.append(getResources().getString(R.string.card_order_finish_endfix));
        txtContent.setText(builder.toString());

        txtJoinInvite.setText(R.string.invite_friend);

        isFinished = true;
    }
}
