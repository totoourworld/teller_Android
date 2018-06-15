package com.grepix.grepixutils;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import com.grepix.grepixutils.R;


/**
 * Created by devin on 2017-10-12.
 */

public class GrepixUtils {

    Context context;

    public static  void showSettingsAlert(final Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });


        // Showing Alert Message
        alertDialog.show();
    }

    public static boolean net_connection_check(Context context) {
        ConnectionManager cm = new ConnectionManager(context);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {
            dialogcalling(context);
        }
        return connection;
    }


    public static void dialogcalling(Context context) {
        final Dialog dialog = new Dialog(context,
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
