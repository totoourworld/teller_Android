package com.rider.xenia;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.utils.DriverInfo;
import com.utils.ParseJson;

public class WalletSelectActivity extends FragmentActivity implements View.OnClickListener
{
    private TextView txtHolderName, txtJoinList;
    private Button btnNaira, btnEuro, btnUsd, btnZar;
    private ImageView btnCancel, imgCard;
    private Controller controller;
    private int selectedCardIndex;

    private static String INDEX_TAG = "CARD_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_select);

        getActionBar().hide();

        txtHolderName = (TextView) findViewById(R.id.txtHolderName);
        txtJoinList   = (TextView) findViewById(R.id.txtJoin);
        txtJoinList.setOnClickListener(this);

        btnNaira = (Button) findViewById(R.id.btnNaira);
        btnNaira.setOnClickListener(this);

        btnEuro  = (Button) findViewById(R.id.btnEuro);
        btnEuro.setOnClickListener(this);

        btnUsd   = (Button) findViewById(R.id.btnUsd);
        btnUsd.setOnClickListener(this);

        btnZar   = (Button) findViewById(R.id.btnZar);
        btnZar.setOnClickListener(this);

        btnCancel = (ImageView) findViewById(R.id.recancel);
        btnCancel.setOnClickListener(this);

        imgCard = (ImageView) findViewById(R.id.imgCard);

        controller = (Controller) getApplication();

        init();
    }

    public void init()
    {
        selectCard(btnNaira);
        imgCard.setImageResource(R.drawable.card_naira_purple);
        selectedCardIndex = 0;

        ParseJson parseJson = new ParseJson(this);
        DriverInfo userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        txtHolderName.setText("" + userInfo.getUFname() + " " + userInfo.getULname());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.recancel:
                onBackPressed();
                break;

            case R.id.btnNaira:
                unSelectAllCards();
                selectCard(btnNaira);
                imgCard.setImageResource(R.drawable.card_naira_purple);
                selectedCardIndex = 0;
                break;

            case R.id.btnEuro:
                unSelectAllCards();
                selectCard(btnEuro);
                imgCard.setImageResource(R.drawable.card_euro_purple);
                selectedCardIndex = 1;
                break;

            case R.id.btnUsd:
                unSelectAllCards();
                selectCard(btnUsd);
                imgCard.setImageResource(R.drawable.card_dollar_purple);
                selectedCardIndex = 2;
                break;

            case R.id.btnZar:
                unSelectAllCards();
                selectCard(btnZar);
                imgCard.setImageResource(R.drawable.card_rand_purple);
                selectedCardIndex = 3;
                break;

            case R.id.txtJoin:
                Intent finishIntent = new Intent(this, WalletOrderFinish.class);
                finishIntent.putExtra(INDEX_TAG, selectedCardIndex);
                startActivity(finishIntent);
                break;

            default:
                break;

        }
    }

    public void unSelectAllCards()
    {
        btnNaira.setBackground((Drawable)getResources().getDrawable(R.drawable.bg_border_blue_rounded));
        btnNaira.setTextColor(getResources().getColor(R.color.blue_light));

        btnEuro.setBackground((Drawable)getResources().getDrawable(R.drawable.bg_border_blue_rounded));
        btnEuro.setTextColor(getResources().getColor(R.color.blue_light));

        btnUsd.setBackground((Drawable)getResources().getDrawable(R.drawable.bg_border_blue_rounded));
        btnUsd.setTextColor(getResources().getColor(R.color.blue_light));

        btnZar.setBackground((Drawable)getResources().getDrawable(R.drawable.bg_border_blue_rounded));
        btnZar.setTextColor(getResources().getColor(R.color.blue_light));
    }

    public void selectCard(Button selectionCardButton)
    {
        selectionCardButton.setBackground((Drawable)getResources().getDrawable(R.drawable.bg_border_cyan_rounded));
        selectionCardButton.setTextColor(getResources().getColor(R.color.cyan));
    }
}
