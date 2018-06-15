package com.rider.xenia;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.grepix.grepixutils.WebService;
import com.utils.Constants;
import com.utils.TripHistory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThankyouActivity extends FragmentActivity {
    private Controller controller;
    TripHistory currentTrip;
    @BindView(R.id.fare_ratingbar)
    RatingBar fareRatingBar;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        getActionBar().hide();
        ButterKnife.bind(this);
        LocalBroadcastManager.getInstance(ThankyouActivity.this).registerReceiver(mNotificationReceiver, new IntentFilter("some_custom_id"));
        controller= (Controller) getApplication();
        currentTrip = controller.getCurrentTrip();
        findViewById(R.id.submit_but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();

                try {
                    float totalRating = 0;
                    int ratingCount = 0;
                    float rating = fareRatingBar.getRating();
                    totalRating = Float.parseFloat(currentTrip.getDriver().getD_rating());
                    ratingCount = Integer.parseInt(currentTrip.getDriver().getD_rating_count());
                    totalRating = (float) ((totalRating * ratingCount + rating) / (ratingCount + 1.0));
                    HashMap<String, String> params = new HashMap<>();
                    params.put("d_rating", String.valueOf(totalRating));
                    params.put("d_rating_count", String.valueOf(totalRating));
                    params.put("api_key", currentTrip.getDriver().getApiKey());
                    params.put("driver_id", currentTrip.getDriver().getUserId());
                    WebService.excuteRequest(ThankyouActivity.this, params, Constants.Urls.URL_DRIVER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
                        @Override
                        public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                            hideProgress();
                            if (isUpdate) {
                                controller.pref.setTripStatus("no");
                                Intent intent=new Intent(ThankyouActivity.this,MainScreenActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                } catch (Exception e) {

                        controller.pref.setTripStatus("no");
                        Intent intent=new Intent(ThankyouActivity.this,MainScreenActivity.class);
                        startActivity(intent);
                        finish();

                }

            }
        });
    }

    public void showProgress() {
        dialog = new ProgressDialog(ThankyouActivity.this);
        dialog.setMessage("Please wait....");
        dialog.setTitle("");
        dialog.show();
        dialog.setCancelable(false);
    }

    public void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNotificationReceiver);
    }

    int count = 0;
    private int dialogCount = 0;
    protected BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("onReceive== "+intent.getStringExtra("message"));
            // Toast.makeText(MainScreenActivity.this, intent + "  " + intent + intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
            Bundle bundle = getIntent().getExtras();
            //  if(bundle.getString("action_id")
            if (dialogCount == 0) {
                try {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    // NotificationDialog fareDialog = new NotificationDialog();
                    DialogFragment newFragment = NotificationDialog.newInstance(ThankyouActivity.this);
                    newFragment.show(ft, "dsads");
                } catch (Exception e) {

                }

            }

        }
    };

    public static class NotificationDialog extends DialogFragment {

        private static ThankyouActivity mainScreenActivity;


        public static NotificationDialog newInstance(ThankyouActivity mainScreenActivityTemp) {
             NotificationDialog f = new NotificationDialog();
            mainScreenActivity = mainScreenActivityTemp;
            return f;
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            mainScreenActivity.controller.setPush(false);
            mainScreenActivity.dialogCount++;
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            View v = inflater.inflate(R.layout.dialog_notification, container, false);

            Map<String, String> data = mainScreenActivity.controller.getRemoteMessageData();
            final String tripStatus = data.get(Constants.Keys.TRIP_STATUS);
//            final String message = controller.getPushMessage();

            ((TextView) v.findViewById(R.id.noti_dialog_message)).setText(data.get(Constants.Keys.PRICE));
            ((TextView) v.findViewById(R.id.noti_dialog_ok)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainScreenActivity.dialogCount--;
                    mainScreenActivity.controller.pref.setTripStatus(tripStatus);
                    getDialog().dismiss();

                }
            });


            return v;
        }

    }

}
