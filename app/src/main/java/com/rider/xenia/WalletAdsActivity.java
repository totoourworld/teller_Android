package com.rider.xenia;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class WalletAdsActivity extends FragmentActivity implements View.OnClickListener
{
    private ImageButton btnRecancel;
    private TextView txtMoreFeature, txtOrderNow, txtOrderLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_ads);

        getActionBar().hide();

        btnRecancel = (ImageButton)findViewById(R.id.recancel);
        btnRecancel.setOnClickListener(this);

        txtMoreFeature = (TextView) findViewById(R.id.txtMoreFeatures);
        txtMoreFeature.setOnClickListener(this);

        txtOrderNow = (TextView) findViewById(R.id.txtOrderNow);
        txtOrderNow.setOnClickListener(this);

        txtOrderLater = (TextView) findViewById(R.id.txtGetLater);
        txtOrderLater.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.recancel:
                onBackPressed();
                break;

            case R.id.txtMoreFeatures:

                break;

            case R.id.txtOrderNow:
                Intent selectIntent = new Intent(this, WalletSelectActivity.class);
                startActivity(selectIntent);
                break;

            case R.id.txtGetLater:
                onBackPressed();
                break;

            default:
                 break;
        }
    }
}
