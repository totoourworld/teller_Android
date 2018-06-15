package com.rider.xenia;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.addressHelper.DriverConstants;
import com.android.volley.VolleyError;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.custom.CustomProgressDialog;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.grepix.grepixutils.WebService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.ride.adapter.PromoCode;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;
import com.utils.TripHistory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FareActivity extends FragmentActivity {


    String CONFIG_ENVIRONMENT, CONFIG_CLIENT_ID, paypaltype;

    private static final int REQUEST_PAYPAL_PAYMENT = 1;

    String submerchantid;
    Boolean categorynetcheck = false;
    String payment_type;

    private JSONObject login_jsonobj;
    String login_inputline1, login_status, verify;
    Dialog dialog1;
    String promo_code, price;
    Double promo_amount = 0.0;

    TextView amount;
    String Amount;
    String token;
    String paymentId;
    String gpscountry;
    String User_id, rideshareid, fbuserproimg, WhoLogin, checkpassword, location;
    Button mappage, Logout, farereview;
    LinearLayout farereviewlayout, submitlayout, ratinglayout;
    String drivername, driveremail, drivermobile, tax, wait, pickup, drop, date, service, distance, waiting, paydone = null, autotripid, tripid, driverid;

    Button paypal, braintree, mercadopago, cash, cancel;

    protected static final String TAG = "Fareactivity";

    public static final String PREFS_NAME = "MyPrefsFile";

    String Liveurl = "";
    String Liveurl1 = "";
    String Liveurl2 = "";

    private JSONObject remove_jsonobj;
    private String remove_status;
    String CLIENT_TOKEN_FROM_SERVER;
    int REQUEST_CODE = 100;


    String rating;
    RatingBar rate;

    Button Promocode, off;


    Boolean Promocodesuccess = false, Promocodeamount = false;

    //   new Bind View

    @BindView(R.id.fare_review)
    LinearLayout fareReviewView;

    @BindView(R.id.fare_rating_view)
    RelativeLayout fareRatingView;

    @BindView(R.id.fare_ratingbar)
    RatingBar fareRatingBar;

    @BindView(R.id.fare_submit_button)
    Button submit;

//    @BindView(R.id.fare_card_user_name)
//    TextView cardUserName;

    @BindView(R.id.fare_promo_layout)
    RelativeLayout promoLayout;

    @BindView(R.id.rl_promo_amount)
    RelativeLayout rlPromoAmount;

    @BindView(R.id.fare_et_promocode)
    TextView tvPromocode;

    //    @BindView(R.id.amount)
//    TextView ammount;
//
    @BindView(R.id.big_amount)
    TextView bigAmount;

    @BindView(R.id.actual_amount)
    TextView actualAmount;

    @BindView(R.id.promo_code_price)
    TextView promoCodePrice;

    @BindView(R.id.fare_promocode)
    TextView btPromocode;

    @BindView(R.id.promo_code_text)
    TextView promoCodeText;

    private Controller controller;
    DriverInfo userInfo;
    TripHistory currentTrip;

    private boolean isPromocodeUsed;
    private PromoCode promoCode;
    private float amtAfterApplyPromo;
    private float amtPromo;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private CustomProgressDialog dialog;
    private float driver_get_amount=0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare);
        getActionBar().hide();
        ButterKnife.bind(this);
        fareRatingView.setVisibility(View.GONE);
        fareReviewView.setVisibility(View.GONE);
        controller = (Controller) getApplication();
        ParseJson parseJson = new ParseJson(this);
        dialog=new CustomProgressDialog(this);
        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        LayerDrawable stars = (LayerDrawable) fareRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellow_color), PorterDuff.Mode.SRC_ATOP);
        //cardUserName.setText("" + userInfo.getUFname() + " " + userInfo.getULname());
        parseJson = new ParseJson(this);
        //createdTrip = parseJson.getTripDetails(controller.pref.getTripResponce());
        updateTripData();
/*
        CustomerSession.initCustomerSession(
                new ExampleEphemeralKeyProvider(FareActivity.this,
                        new ExampleEphemeralKeyProvider.ProgressListener() {
                            @Override
                            public void onStringResponse(String string) {
                                if (string.startsWith("Error: ")) {

                                }
                            }
                        }));*/
//        controller


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        handleProfileUpdate();
    }

    private void updateTripData() {
        currentTrip = controller.getCurrentTrip();

        if (currentTrip != null) {
            // ammount.setText("$" + currentTrip.getTripPayAmount());
            try
            {
                Float promo= Float.parseFloat(currentTrip.getTripPromoAmt());
                if(promo>0){
                    Float tripCalculated=Float.parseFloat(currentTrip.getTripPayAmount())-promo;
                    actualAmount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(currentTrip.getTripPayAmount())));
                    bigAmount.setText(String.format(controller.currencyUnit()+"%.02f", tripCalculated));
                }else{
                    actualAmount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(currentTrip.getTripPayAmount())));
                    bigAmount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(currentTrip.getTripPayAmount())));
                }

                int commision = 0;
                ArrayList<DriverConstants> constantList = controller.getConstantsList();
                for(DriverConstants constants:constantList){
                    if(constants.getConstant_key().equals("appicial_commission")){
                        commision=Integer.parseInt(constants.getConstant_value());
                        break;
                    }
                }

                float tripPayAmt = Float.parseFloat(currentTrip.getTripPayAmount());
                driver_get_amount = ((tripPayAmt * (100-commision)) / 100);

            }catch (Exception e){

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(FareActivity.this).registerReceiver(mNotificationReceiver, new IntentFilter("some_custom_id"));
       // handleProfileUpdate();
    }


    private Timer timer;

    public void handleProfileUpdate() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                FareActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getTripStatus();
                    }
                });
            }
        }, 0, 5000);//put here time 1000 milliseconds=1 second
    }

    int count = 0;
    private int dialogCount = 0;
    protected BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Toast.makeText(BEverywareHomeActivity.this,""+count++,Toast.LENGTH_LONG).show();
            Bundle bundle = getIntent().getExtras();
            //  if(bundle.getString("action_id")
            if (dialogCount == 0) {
                handleOnPick();
            }
        }
    };

    private boolean isShown=false;
    private void getTripStatus(){
        ParseJson parseJson=new ParseJson(FareActivity.this);
        TripHistory createdTrip = parseJson.getTripDetails(controller.pref.getTripResponce());
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.TRIP_ID, createdTrip.getTripId());
        params.put(Constants.Keys.USER_ID, controller.pref.getUserID());
        System.out.println("GetTripByID : " + params);
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_GET_TRIP, new  WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    try{
                        if(data!=null){
                            ParseJson parseJson=new ParseJson(FareActivity.this);
                            final TripHistory tripHistory = parseJson.parseSingleTrip(data.toString());
                            controller.setCurrentTrip(tripHistory);
                            updateTripData();
                            if(tripHistory.getTripStatus().equals("Cash")){
                                submit.setVisibility(View.GONE);
                                btPromocode.setVisibility(View.GONE);
                                promoLayout.setVisibility(View.GONE);
                                isShown=true;
                                showViewWithAnimation(fareRatingView);
                            }else if(tripHistory.getTripPayStatus().equals("Paid")){
                                if(!isShown){
                                    isShown=true;
                                    submit.setVisibility(View.GONE);
                                    btPromocode.setVisibility(View.GONE);
                                    promoLayout.setVisibility(View.GONE);
                                    showViewWithAnimation(fareRatingView);
                                    timer.cancel();
                                }
                            }
                            try{

                                if(Float.parseFloat(tripHistory.getTripPromoAmt())>0){
                                    promoCodePrice.setVisibility(View.VISIBLE);
                                    promoCodeText.setVisibility(View.VISIBLE);
                                    promoCodePrice.setText(controller.currencyUnit()+String.format("%.02f ", amtPromo));
                                }else{
                                   // promoCodePrice.setVisibility(View.GONE);
                                   // promoCodeText.setVisibility(View.GONE);
                                }
                            }catch (Exception e){
                               // promoCodePrice.setVisibility(View.GONE);
                               // promoCodeText.setVisibility(View.GONE);
                            }



                        }

                    }catch (Exception e){
                        submit.setVisibility(View.GONE);
                        btPromocode.setVisibility(View.GONE);
                        promoLayout.setVisibility(View.GONE);
                        isShown=true;
                        showViewWithAnimation(fareRatingView);
                    }
                } else {

                }
            }
        });
    }
    private void handleOnPick() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Map<String, String> data = controller.getRemoteMessageData();
        final String tripStatus = data.get(Constants.Keys.TRIP_STATUS);
        if(tripStatus.equals("end")){
            LocalBroadcastManager.getInstance(FareActivity.this).unregisterReceiver(mNotificationReceiver);
        }
    }






    /*  BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
            System.out.println("Rider slide main activity New Message" + newMessage);

            if (newMessage != null || !newMessage.equals("null")) {
                System.out.println("Rider REceive the MESSAGE" + newMessage);
                if (newMessage == "payaccept" || newMessage.equals("payaccept")) {
                    final AlertDialog.Builder builder =
                            new AlertDialog.Builder(FareActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setMessage(R.string.thanks_driving);
                    builder.setTitle(R.string.cash_received);
                    builder.setCancelable(false);
                    builder.setInverseBackgroundForced(false);

                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            farereviewlayout.setVisibility(View.VISIBLE);
                            farereview.setVisibility(View.VISIBLE);
                            builder.setCancelable(true);
                        }
                    });


                    builder.show();

                }
                if (newMessage == "promoaccept" || newMessage.equals("promoaccept")) {
                    final AlertDialog.Builder builder =
                            new AlertDialog.Builder(FareActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setMessage(R.string.thanks_driving);
                    builder.setTitle(R.string.promoaccepted);
                    builder.setCancelable(false);
                    builder.setInverseBackgroundForced(false);

                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            farereviewlayout.setVisibility(View.VISIBLE);
                            farereview.setVisibility(View.VISIBLE);
                            builder.setCancelable(true);
                        }
                    });


                    builder.show();

                }
            }
        }

    };*/


    @OnClick(R.id.fare_go_fare_review)
    public void goFareReview(View view) {
        controller.pref.setTripStatus("no");
        timer.cancel();
        Intent intent = new Intent(this, FareReviewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fare_go_home)
    public void goFareHome(View view) {
        controller.pref.setTripStatus("no");
        timer.cancel();
        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    // Hanbdle button clicked

    @OnClick(R.id.fare_rating_done)
    public void ratingDoneButton(View view) {
        if (currentTrip == null) {
            return;
        }
        showProgress();
        float totalRating = 0;
        int ratingCount = 0;

        float rating = fareRatingBar.getRating();
        try {
            totalRating = Float.parseFloat(currentTrip.getDriver().getD_rating());
            ratingCount = Integer.parseInt(currentTrip.getDriver().getD_rating_count());
        } catch (Exception e) {
        }
        totalRating = (float) ((totalRating * ratingCount + rating) / (ratingCount + 1.0));
        HashMap<String, String> params = new HashMap<>();
        params.put("d_rating", String.valueOf(totalRating));
        params.put("d_rating_count", String.valueOf(totalRating));
        params.put("api_key", currentTrip.getDriver().getApiKey());
        params.put("driver_id", currentTrip.getDriver().getUserId());
        WebService.excuteRequest(this, params, Constants.Urls.URL_DRIVER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                hideProgress();
                if (isUpdate) {
                    hideViewWithAnimation(fareRatingView);
                }
            }
        });
    }

    @OnClick(R.id.fare_apply_promo)
    public void fareApplyPromoCode(View view) {
        if (tvPromocode.getText().toString().length() == 0) {
            tvPromocode.setError(getString(R.string.enter_promo_code));
            return;
        } else {
            tvPromocode.setError(null);
        }
        applyPromoCode(tvPromocode.getText().toString());
    }

    @OnClick(R.id.fare_promocode)
    public void farePromoCode(View view) {
        if (promoLayout.getVisibility() == View.INVISIBLE) {
            promoLayout.setVisibility(View.VISIBLE);
        } else {
            promoLayout.setVisibility(View.INVISIBLE);
        }
    }


    @OnClick(R.id.fare_rating_skip)
    public void ratingSkipButton(View view) {
        hideViewWithAnimation(fareRatingView);
    }


    @OnClick(R.id.fare_submit_button)
    public void fareSubmitButton(View view) {
        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(this);
        actionSheetDialog.show(getSupportFragmentManager().beginTransaction(), "Action");
    }


    private void showViewWithAnimation(final View view) {
        view.setAlpha(0);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void hideViewWithAnimation(final View view) {

        view.setAlpha(1);
        view.animate().alpha(0).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                view.setVisibility(View.GONE);
                fareReviewView.setVisibility(View.VISIBLE);
                rlPromoAmount.setVisibility(View.VISIBLE);
                //rlPromoAmount.setVisibility(View.GONE);





            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName(getString(R.string.fare_page)) // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    @SuppressLint("ValidFragment")
    public static class ActionSheetDialog extends DialogFragment {
        private FareActivity activity;

        public ActionSheetDialog(FareActivity activity) {

            this.activity = activity;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.frag_actionsheet, container, false);
            handleView(view);
            return view;
        }


        public void handleView(View view) {
            view.findViewById(R.id.action_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            view.findViewById(R.id.action_cash).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    activity.controller.pref.setTripStatus("no");
                    activity.payWithCashOnHand();
                }
            });
            view.findViewById(R.id.action_paypal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();

                      activity.payWithPaypal();
                    //activity.payWithCreditCard();

                }
            });
            view.findViewById(R.id.action_main_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    activity.controller.pref.setTripStatus("no");
                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
            setCancelable(true);
        }
    }

    private void payWithCreditCard() {
       /* Intent intent = new Intent(FareActivity.this, PaymentByNewCard.class);
        startActivityForResult(intent, 2);*/
    }






   /* private ProgressDialog dialog;*/

    public void showProgress() {
        dialog.showDialog();
        /*dialog = new ProgressDialog(FareActivity.this);
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setTitle("");
        dialog.show();
        dialog.setCancelable(false);*/
    }

    public void hideProgress() {
        dialog.dismiss();
       /* if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }*/

    }

    private void payWithCashOnHand() {
        showProgress();
//        curURL = NSURL(string: "\(trip_url)\(UPDATE_TRIP)?api_key=\(self.appDelegate.apikey)&trip_id=\(self.appDelegate.tripid)&trip_pay_mode=\(pay_type)&trip_pay_status=Paid")!
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        //if (currentTrip != null) {
        params.put(Constants.Keys.TRIP_ID, currentTrip.getTripId());
      /*  } else {
            params.put(Constants.Keys.TRIP_ID, "503");
        }*/

        params.put(Constants.Keys.TRIP_PAY_MODE, "Cash");
        params.put(Constants.Keys.TRIP_PAY_STATUS, "Paid");
        params.put("trip_status", "end");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        params.put("trip_pay_date", simpleDateFormat.format(new Date()));
        if (isPromocodeUsed) {
            params.put("promo_id", promoCode.getPromoId());
            params.put("trip_promo_code", promoCode.getPromoCode());
            params.put("trip_promo_amt", String.format("%.02f", amtPromo));
        }
        else{
            rlPromoAmount.setVisibility(View.GONE);
        }
        params.put("trip_driver_commision",""+driver_get_amount);
        System.out.println("PayWithCashOnHand  Params : " + params);
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_UPDATE_TRIP, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {

                if (isUpdate) {
                    sendNotification("Cash");
                    // payment completed
                } else {
                    hideProgress();
                    Toast.makeText(FareActivity.this, "" + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void savePayment() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.TRIP_ID, currentTrip.getTripId());
        params.put("pay_mode", "Cash");
        params.put("pay_status", "Paid");
//        2017-01-25 07:02:31
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        params.put("pay_date", simpleDateFormat.format(new Date()));
        float fare=Float.parseFloat(currentTrip.getTripPayAmount());
        if (isPromocodeUsed) {
            params.put("promo_id", promoCode.getPromoId());
            params.put("pay_promo_code", promoCode.getPromoCode());
            params.put("pay_promo_amt", String.format("%.02f", amtPromo));
            fare=fare-amtPromo;

        }
        params.put("pay_amount", ""+fare);
        //  params.put("total_fare", ""+currentTrip.getTripPayAmount());
        System.out.println("PayWithCashOnHand  Params : " + params);
        WebService.excuteRequest(this, params, Constants.Urls.URL_PAYMENT_SAVE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                hideProgress();
                if (isUpdate) {
                    showAlertOnPayCashDone();
                } else {
                    hideProgress();
                    Toast.makeText(FareActivity.this, "" + error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private AlertDialog alertDialog;

    public void showAlertOnPayCashDone() {
        submit.setVisibility(View.GONE);
        btPromocode.setVisibility(View.GONE);
        promoLayout.setVisibility(View.GONE);
        isShown=true;
        showViewWithAnimation(fareRatingView);
       /* AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(FareActivity.this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setMessage(R.string.cah_payment_received);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setInverseBackgroundForced(false);
        alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
    }

    private int maxAttammp;

    public void sendNotification(final String message) {

        if (currentTrip == null) {
            hideProgress();
            showAlertOnPayCashDone();
            return;
        }

        boolean isAndroid = currentTrip.getDriver().getUDeviceType().equalsIgnoreCase("Android");
        Map<String, String> params = new HashMap<String, String>();
        params.put("message", "Please collect cash from Rider.");
        params.put("trip_id", currentTrip.getTripId());
        params.put("trip_status", "Cash");
        if (isAndroid) {
            params.put("android", currentTrip.getDriver().getUDeviceToken());
        } else {
            params.put("ios", currentTrip.getDriver().getUDeviceToken());
        }
        System.out.println("Params Notification : " + params + "\n Attamp :" + maxAttammp);
        WebService.excuteRequest(this, params, Constants.Urls.URL_DRIVER_NOTIFICATION, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (error != null) {
                    if (maxAttammp < 3) {
                        sendNotification(message);
                        maxAttammp++;
                    } else {
                        savePayment();
                    }
                } else {
                    savePayment();
                }
            }
        });
    }

    private void payWithPaypal() {
//        curURL = NSURL(string: "\(trip_url)\(UPDATE_TRIP)?api_key=\(self.appDelegate.apikey)&trip_id=\(self.appDelegate.tripid)&trip_pay_mode=\(pay_type)")!
        // oncomplete
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.TRIP_ID, "503");
        params.put(Constants.Keys.TRIP_PAY_MODE, "PayPal");
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_UPDATE_TRIP, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                hideProgress();
                if (isUpdate) {
                    submit.setVisibility(View.GONE);
                    showViewWithAnimation(fareRatingView);
                } else {
                    Toast.makeText(FareActivity.this, "" + error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void applyPromoCode(String promo_code) {
        showProgress();
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put("promo_code", promo_code);
        WebService.excuteRequest(this, params, Constants.Urls.URL_VAAIDATE_PROMO, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isValid, VolleyError error) {
                hideProgress();
                if (isValid) {
                   // promoCodePrice.setVisibility(View.VISIBLE);
                   // promoCodeText.setVisibility(View.VISIBLE);
                    handlePromoCode(data.toString());
                    tvPromocode.setError(null);
                    btPromocode.setVisibility(View.INVISIBLE);
                    promoLayout.setVisibility(View.INVISIBLE);

                } else {
                    tvPromocode.setError(getString(R.string.invalid_promo_code));
                }

            }
        });
    }

    private void sendNotificationforPromo(String accept_payment_promo) {
        boolean isAndroid = currentTrip.getDriver().getUDeviceType().equalsIgnoreCase("Android");
        Map<String, String> params = new HashMap<String, String>();
        params.put("message", "Promo Code Applied");
        params.put("trip_id", currentTrip.getTripId());
        params.put("trip_status", accept_payment_promo);
        if (isAndroid) {
            params.put("android", currentTrip.getDriver().getUDeviceToken());
        } else {
            params.put("ios", currentTrip.getDriver().getUDeviceToken());
        }
        System.out.println("Params Notification : " + params + "\n Attamp :" + maxAttammp);
        WebService.excuteRequest(this, params, Constants.Urls.URL_DRIVER_NOTIFICATION, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {

            }
        });
    }

    public void handlePromoCode(String promoResponse) {
        this.promoCode = PromoCode.parsePromoCode(promoResponse);
        isPromocodeUsed = true;
        try {
            amtPromo = promoCode.calucalateAmtByPromoCode(Float.parseFloat(currentTrip.getTripPayAmount()));
            final TripHistory tripHistory = controller.getCurrentTrip();
            tripHistory.setTripPromoAmt(""+amtPromo);
            controller.setCurrentTrip(tripHistory);
            amtAfterApplyPromo = Float.parseFloat(currentTrip.getTripPayAmount()) - amtPromo;
            if (amtAfterApplyPromo <= 0) {
                amtAfterApplyPromo = 0;
            }
            if(isPromocodeUsed) {
                promoCodePrice.setVisibility(View.VISIBLE);
                promoCodeText.setVisibility(View.VISIBLE);
                bigAmount.setText(controller.currencyUnit() +String.format("%.02f ", amtAfterApplyPromo));
                rlPromoAmount.setVisibility(View.VISIBLE);
                promoCodePrice.setText(controller.currencyUnit()+String.format("%.02f ", amtPromo));

            }else{
                promoCodePrice.setVisibility(View.GONE);
                promoCodeText.setVisibility(View.GONE);
            }
            updateTripAfterPromo();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTripAfterPromo() {
        //   showProgress();
//        curURL = NSURL(string: "\(trip_url)\(UPDATE_TRIP)?api_key=\(self.appDelegate.apikey)&trip_id=\(self.appDelegate.tripid)&trip_pay_mode=\(pay_type)&trip_pay_status=Paid")!
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        //if (currentTrip != null) {
        params.put(Constants.Keys.TRIP_ID, currentTrip.getTripId());


        params.put("promo_id", promoCode.getPromoId());
        params.put("trip_promo_code", promoCode.getPromoCode());
        params.put("trip_promo_amt", String.format("%.02f", amtPromo));
        //  params.put("trip_pay_amount", String.format("%.02f", amtAfterApplyPromo));
        params.put("trip_status", "accept_payment_promo");


        System.out.println("PayWithCashOnHand  Params : " + params);
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_UPDATE_TRIP, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {

                sendNotificationforPromo("accept_payment_promo");
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                    startActivity(i, bndlanimation);
                    finish();
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void onBackPressed() {

    }

    //Braintree Payment
    protected void onActivityResult1(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            controller.pref.setTripStatus("no");
            sendNotification("Cash");
            //do the things u wanted
        }
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case BraintreePaymentActivity.RESULT_OK:
                    String paymentMethodNonce = data
                            .getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                    paymentId = paymentMethodNonce;
                    farereviewlayout.setVisibility(View.VISIBLE);
                    submitlayout.setVisibility(View.GONE);
                    ratinglayout.setVisibility(View.GONE);
                    farereview.setVisibility(View.VISIBLE);

                    if (rideshareid.equals("null")) {
//                        update_paymentid();
                    } else {
//                        update_paymentid1();
                    }
                    //passtheNonce to your server–>payment gets completed here
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    // handle errors here, a throwable may be available in
                    // data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case BraintreePaymentActivity.RESULT_OK:
                    String paymentMethodNonce = data
                            .getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                    paymentId = paymentMethodNonce;

//                    updatenonce();

                    farereviewlayout.setVisibility(View.VISIBLE);
                    submitlayout.setVisibility(View.GONE);
                    ratinglayout.setVisibility(View.GONE);
                    Promocode.setVisibility(View.GONE);
                    farereview.setVisibility(View.VISIBLE);
                    System.out.println("Rider Share ID" + rideshareid);
               /* if(rideshareid.equals("null"))
                {*/
//                    update_paymentid();
               /* }
                else
                {
                    update_paymentid1();
                }*/

                    if (gpscountry != null || !gpscountry.equals(null)) {
                        if (gpscountry == "United States" || gpscountry.equals("United States")) {
//                            auto_braintree_payment();
                        } else {
                            payment_type = "Braintree";
//                            other_braintree_payment();
                        }
                    }

                    //passtheNonce to your server–>payment gets completed here
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    //  handle errors here, a throwable may be available in
                    // data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)
                    break;
                default:
                    break;
            }
        } else if (requestCode == REQUEST_PAYPAL_PAYMENT) {
            System.out.println("Request Code" + requestCode);
            if (resultCode == Activity.RESULT_OK) {
                System.out.println("resultCode" + resultCode);

                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                System.out.println("Confirm" + confirm);
                if (confirm != null) {
                    try {

                        System.out.println("Responseeee" + confirm);
                        Log.i("paymentExample", confirm.toJSONObject().toString());


                        JSONObject jsonObj = new JSONObject(confirm.toJSONObject().toString());

                        paymentId = jsonObj.getJSONObject("response").getString("id");
                        System.out.println("payment id:-==" + paymentId);


                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                        builder.setCancelable(false);
                        builder.setMessage(R.string.payment_complete_successfully);
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                farereviewlayout.setVisibility(View.VISIBLE);
                                submitlayout.setVisibility(View.GONE);
                                ratinglayout.setVisibility(View.GONE);
                                Promocode.setVisibility(View.GONE);
                                farereview.setVisibility(View.VISIBLE);
                                dialog.cancel();
                            }
                        });
                        builder.show();

                        if (rideshareid.equals("null")) {
//                            update_paymentid();
                        } else {
//                            update_paymentid1();
                        }

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
            }
        }


    }











    /*public boolean net_connection_check() {
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
        window.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);

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
    }*/

    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {
            dialogcalling();
            categorynetcheck = true;
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


    public class NotificationDialog {





    }
}
