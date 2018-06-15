package com.rider.xenia;


public interface Config {

	// CONSTANTS
	//static final String YOUR_SERVER_URL =  "http://127.0.0.1/gcm_server_files/register.php";  // No Need to give Server URL
	// YOUR_SERVER_URL : Server url where you have placed your server files
    // Google project id

    static final String GOOGLE_SENDER_ID = "391226754417";  // Place here your Google project id

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.rider.arcane_android.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

}
