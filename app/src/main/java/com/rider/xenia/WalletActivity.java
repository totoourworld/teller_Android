package com.rider.xenia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

import com.custom.BButton;

/**
 * Created by King on 2/18/2018 0018.
 */

public class WalletActivity extends Activity
{
    private static String url = "https://ico.xeniacoin.org/Account/Register";

    private ImageButton btnRecancel;
    private BButton btnXeniaSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coinwallet);

        btnRecancel = (ImageButton)findViewById(R.id.recancel);
        btnRecancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnXeniaSignup = (BButton) findViewById(R.id.xenia_signup);
        btnXeniaSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXeniaSignup();
            }
        });
    }

    private void gotoXeniaSignup()
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        finish();
    }
}
