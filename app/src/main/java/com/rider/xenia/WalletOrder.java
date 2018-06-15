package com.rider.xenia;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adaptor.SlidingImage_Adapter;
import com.util.ImageModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WalletOrder extends FragmentActivity implements View.OnClickListener{

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.card_dollar_purple, R.drawable.card_euro_purple,
            R.drawable.card_naira_purple, R.drawable.card_rand_purple };

    private ImageButton btnRecancel;
    private ImageView btnCreateCard;
    private TextView txtChooseCard, txtLoadWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_order);

        getActionBar().hide();

        btnRecancel = (ImageButton)findViewById(R.id.recancel);
        btnRecancel.setOnClickListener(this);

        btnCreateCard = (ImageView)findViewById(R.id.imgCardCreate);
        btnCreateCard.setOnClickListener(this);

        txtChooseCard = (TextView) findViewById(R.id.textChooseCard);
        txtChooseCard.setOnClickListener(this);

        txtLoadWallet = (TextView) findViewById(R.id.textLoadXeniaWallet);
        txtLoadWallet.setOnClickListener(this);

        mPager = (ViewPager) findViewById(R.id.pager);
        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        initPager();
    }

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < myImageList.length; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void initPager()
    {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(WalletOrder.this,imageModelArrayList));

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        /*final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);*/
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.recancel:
                onBackPressed();
                break;

            case R.id.imgCardCreate:
                //Create card button
                break;

            case R.id.textChooseCard:
                //Choose card text

                break;

            case R.id.textLoadXeniaWallet:
                //Load Xenia Wallet text

                break;

            default:
                    break;
        }
    }
}
