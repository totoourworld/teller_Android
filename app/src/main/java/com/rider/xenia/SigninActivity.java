package com.rider.xenia;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.app.Config;
import com.custom.CustomProgressDialog;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.messaging.FirebaseMessaging;
import com.grepix.grepixutils.GrepixUtils;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebService;
import com.util.NotificationUtils;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SigninActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener {

    @BindView(R.id.signin_email)
    EditText Email;
    @BindView(R.id.signin_password)
    EditText Psw;

    Button googleplusbns;

    TextView facebookin, forgotpsw, signuplink;
    ImageView loadimage;
    int profilecount = 0;


    public static String User_id;
    public static final String PREFS_NAME = "MyPrefsFile";

    String Get_email, Get_password;

    protected static final String TAG = "SigninActivity";

    String login_status;

    JSONObject login_jsonobj, login_Error;
    String status;

    String firstName;
    String lastName;
    String pimg;
    String fb_id;


    Boolean Check_activity = false;

    public String WhoLogin;

    int count = 1;

    //Google plus varibles
    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG1 = "RegisterActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    private ConnectionResult mConnectionResult;

    String gpid, gproimg, gpersonGooglePlusProfile, gpemailid, gpfirstname;

    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginManager loginManager;
    private Controller controller;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        progressDialog=new CustomProgressDialog(SigninActivity.this);
        GrepixUtils.net_connection_check(SigninActivity.this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        controller = (Controller) getApplicationContext();
        Facebook();


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("my tokn=====1", "brod");
                // checking for   intent filteri
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    Log.d("my tokn=====1", "");
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    Log.d("my tokn=====5", "");
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), getString(R.string.push_notification) + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }
            }
        };
        String deviceToken = getSaveDiceToken();
        Log.d(TAG, "Device Token :" + deviceToken);


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Toast.makeText(getApplicationContext(), getString(R.string.facebook_access_token) + newToken, Toast.LENGTH_LONG).show();
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                if (profilecount == 0) {
                    displayMessage(newProfile);
                }
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
//        getFbKeyHash("com.driver.arcane_android");


        getActionBar().hide();


        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        signuplink = (TextView) findViewById(R.id.signuplink);

        facebookin = (TextView) findViewById(R.id.facebookbns);
        facebookin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GrepixUtils.net_connection_check(SigninActivity.this)){
                    Check_activity = true;
                    callbackManager = CallbackManager.Factory.create();
                    accessToken = AccessToken.getCurrentAccessToken();
//                LoginManager.getInstance().logInWithReadPermissions(SigninActivity.this, Arrays.asList("public_profile"));
                    if (accessToken == null) {
                        List<String> permissionNeeds = Arrays.asList("user_photos", "email",
                                "user_birthday", "public_profile");
                        LoginButton loginButton = new LoginButton(SigninActivity.this);
                        loginButton.setReadPermissions(permissionNeeds);

                        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                onLoginSuccess(loginResult.getAccessToken());

                            }

                            @Override
                            public void onCancel() {
                                // Toast.makeText(getApplicationContext(), R.string.on_cancel, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(FacebookException error) {
                                // Toast.makeText(getApplicationContext(), getString(R.string.on_error) + error, Toast.LENGTH_LONG).show();
                            }
                        });
                        loginButton.performClick();
                    } else {
                        onLoginSuccess(accessToken);
                    }
                }


            }
        });
        googleplusbns = (Button) findViewById(R.id.googleplus);
        forgotpsw = (TextView) findViewById(R.id.forgotpsw);
        loadimage = (ImageView) findViewById(R.id.loadimage);

        WhoLogin = "SignIn";


        forgotpsw.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                    Intent reset = new Intent(getApplicationContext(), Resetpassword.class);
                    startActivity(reset);

            }
        });


        signuplink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                    Intent reset = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(reset);

            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        googleplusbns.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // fbLoginButton.setEnabled(false);
                Email.setError(null);
                Psw.setError(null);
                signInWithGplus();
                loadimage.setVisibility(View.VISIBLE);
            }
        });


    }

    AccessToken accessToken;

    public void onLoginSuccess(AccessToken accessToken) {
        this.accessToken = accessToken;
        progressDialog.showDialog();
        GraphRequest request = GraphRequest.newMeRequest(
                this.accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        try {
                            facebookLogin(object);

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields",
                "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void facebookLogin(final JSONObject object) {
        String facebookId = null;
        try {
            facebookId = object.getString("id");

            progressDialog.showDialog();
            DeviceTokenService.loginWithFacebook(controller, facebookId, new DeviceTokenService.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isLogin, VolleyError error) {
                    progressDialog.dismiss();
                    if (isLogin) {
                        String response = data.toString();
                        System.out.println("Login 1" + response);
                        ParseJson parseJson = new ParseJson(SigninActivity.this);
                        DriverInfo responceLogin = parseJson.ParseLoginResponce(response.toString());
                        controller.pref.saveUserLogin(response.toString());
                        controller.pref.setUserApiKey(responceLogin.getApiKey());
                        controller.pref.saveUserID(responceLogin.getUserId());
                        User_id = responceLogin.getUserId();
                        controller.pref.saveIsLoginSucess(true);
                        updateprofile(data.toString());
                        WhoLogin = "SignIn";
                    } else {
                        progressDialog.dismiss();
                        controller.setFacebookResponce(object);
                        Intent intent=new Intent(SigninActivity.this, VerifyForFacebookLogin.class);
                        startActivity(intent);;
                    }

                }
            });

        } catch (JSONException e) {

            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    public void facebookRegister(JSONObject jsonObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.showDialog();
            }
        });
        try {
            Map<String, String> params = new HashMap<String, String>();

            if (jsonObject.has("email")) {
                params.put("u_email", jsonObject.getString("email"));
            }
            if (jsonObject.has("name")) {
                String name = jsonObject.getString("name");
                if (name != null) {
                    String names[] = name.split(" ");
                    if (names.length > 1) {
                        params.put("u_fname", names[0]);
                        params.put("u_lname", names[1]);
                    } else {
                        params.put("u_fname", name);
                    }
                }
            }
            params.put("u_fbid", jsonObject.getString("id"));
            params.put("u_password", "test");
            String deviceToken = getSaveDiceToken();
            if (deviceToken != null) {
                params.put("u_device_token", deviceToken);
                params.put("u_device_type", "Android");
            }
            System.out.println("Params : " + params);
            WebService.excuteRequest(controller, params, Constants.Urls.URL_USER_REGISTRATION, new WebService.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    progressDialog.dismiss();
                    if (isUpdate) {
                        login_Progress(data.toString());
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, getString(R.string.response_error) + e, Toast.LENGTH_LONG).show();
        }

    }


    @OnClick(R.id.signin_dones)
    public void signinDone(View view) {
        if (GrepixUtils.net_connection_check(SigninActivity.this)) {
            googleplusbns.setEnabled(false);
            done();
        }

    }


    private String getSaveDiceToken() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        return pref.getString("regId", null);
    }

    private void displayFirebaseRegId() {
        String regId = getSaveDiceToken();

        Log.e(TAG, "Firebase reg id:=== " + regId);
        try {
            //sendDeviceToken(regId);
        } catch (Exception e) {

        }
        if (!TextUtils.isEmpty(regId)) {
            // Toast.makeText(MainActivity.this,"Firebase Reg Id: " + regId,Toast.LENGTH_LONG).show();;
            // txtRegId.setText("Firebase Reg Id: " + regId);
        } else {
            // Toast.makeText(MainActivity.this,"Firebase Reg Id is not received yet!",Toast.LENGTH_LONG).show();;
            // txtRegId.setText("Firebase Reg Id is not received yet!");
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(SigninActivity.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(SigninActivity.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(SigninActivity.this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


//    public boolean isValidFrom() {
//        boolean isValid = true;
//        if (Email.getText().toString().length() == 0) {
//            Email.setError("Required");
//            isValid = false;
//        } else {
//            Email.setError("");
//        }
//        if (Psw.getText().toString().length() == 0) {
//            Psw.setError("Required");
//        } else {
//            Psw.setError("");
//        }
//        return isValid;
//    }

    public void done() {


        Get_email = Email.getText().toString();
        Get_email = Get_email.toLowerCase();
        System.out.println("GMAIL After Convert Lower case" + Get_email);
        Get_password = Psw.getText().toString();
        if(Validations.isValdateSignIn(SigninActivity.this,Email,Psw)){
            controller.pref.saveEmail(Get_email);
            controller.pref.savePassword(Get_password);
            progressDialog.showDialog();

            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.Keys.EMAIL_KEY, Get_email);
            params.put(Constants.Keys.PASSWORD_KEY, Get_password);

            WebService.excuteRequest(this, params,Constants.Urls.URL_USER_SIGN_IN, new WebService.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    progressDialog.showDialog();
                    if (isUpdate) {
                        controller.pref.saveUserLogin(data.toString());
                        updateprofile(data.toString());
                    } else {
                        progressDialog.dismiss();
                        if (error == null) {
                            Toast.makeText(SigninActivity.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        }
    }

    private void updateprofile(final String loginResponse) {
        String deviceToken = getSaveDiceToken();
        System.out.println("Login 1 dt" + deviceToken);
        if (deviceToken != null) {
            DeviceTokenService.sendDeviceTokenToServer(controller, deviceToken, new DeviceTokenService.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    if (data != null) {
                        login_Progress(data.toString());
                    } else {
                        login_Progress(loginResponse);
//                        Toast.makeText(SigninActivity.this, "Internet Connection Failed", Toast.LENGTH_LONG).show();
                    }

                    progressDialog.dismiss();
                }
            });
            // when device token is exist;
        } else {
            progressDialog.dismiss();
            //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
            login_Progress(loginResponse);
            // when device token does not exist;
        }
    }

    public void Facebook() {


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        if (Profile.getCurrentProfile() == null) {
                            profileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                    Log.v("facebook - profile2", profile2.getFirstName());
                                    displayMessage(profile2);
                                    profileTracker.stopTracking();
                                }
                            };
                            profileTracker.startTracking();
                        } else {
                            Profile profile = Profile.getCurrentProfile();
                            displayMessage(profile);
                            Log.v("facebook - profile", profile.getFirstName());
                        }

                        String Facebook_Token = loginResult.getAccessToken().getToken();
                        GraphRequestAsyncTask request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                user.optString("email");
                                System.out.println("email from aysnctask" + user.optString("email"));

                            }
                        }).executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SigninActivity.this, R.string.login_canceled_by_user, Toast.LENGTH_LONG).show();
                        System.out.println("Facebook Login failed!!");

                    }

                    @Override
                    public void onError(FacebookException e) {
                        Toast.makeText(SigninActivity.this, R.string.login_unsuccessful, Toast.LENGTH_LONG).show();
                        System.out.println("Facebook Login failed!!");
                    }


                });

    }

    public int login_Progress(String loginResponse) {
        ParseJson parseJson = new ParseJson(SigninActivity.this);
        DriverInfo responceLogin = parseJson.ParseLoginResponce(loginResponse.toString());
        controller.pref.saveUserLogin(loginResponse.toString());
        controller.pref.setUserApiKey(responceLogin.getApiKey());
        controller.pref.saveUserID(responceLogin.getUserId());
        User_id = responceLogin.getUserId();
        controller.pref.saveIsLoginSucess(true);
        System.out.println("Login 1 last");
        Intent main = new Intent(getApplicationContext(), MainScreenActivity.class);
        main.putExtra("userid", User_id);
        main.putExtra("whologin", WhoLogin);
        main.putExtra("password", Get_password);
        startActivity(main);
        finish();
        return 100;
    }


    private void loadSavedPreferences() {
        System.out.println("User has successfully logged in, save this information");
        // We need an Editor object to make preference changes.

        SharedPreferences settings = getSharedPreferences(SigninActivity.PREFS_NAME, 1);
        SharedPreferences.Editor editor = settings.edit();

        //Set "hasLoggedIn" to true
        editor.putBoolean("hasLoggedIn", true);
        editor.putString("euser", User_id);
        editor.putString("whologin", WhoLogin);
        editor.putString("fbuserproimg", gproimg);
        System.out.println("Signin Load Saved Perferencde WhoLogin" + WhoLogin);
        // Commit the edits!
        editor.commit();

    }
    //******************************************** Google plus functions **********************************

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            loadimage.setVisibility(View.INVISIBLE);
            mGoogleApiClient.disconnect();
        } else {
            accessTokenTracker.stopTracking();
            profileTracker.stopTracking();
        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            System.out.println("OnConnectionFailed Result is" + result);
            System.out.println("OnConnectionFailed Result Code is" + result.getErrorCode());

            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }


    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;

                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(x);
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
            callbackManager.onActivityResult(requestCode, responseCode, intent);

        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        if (!mSignInClicked) {
            signOutFromGplus();
        }
        mSignInClicked = false;

        // Get user's information
        getProfileInformation();
        if (count == 1) {
            checkgoogleplusid();
        }
        count++;


    }


    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);

                gpid = currentPerson.getId();
                gpfirstname = currentPerson.getDisplayName();
                gproimg = currentPerson.getImage().getUrl();
                gpersonGooglePlusProfile = currentPerson.getUrl();
               // gpemailid = Plus.AccountApi.getAccountName(mGoogleApiClient);


            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.person_info_is_null, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Sign-in into google
     */
    private void signInWithGplus() {
        signOutFromGplus();
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }


    /**
     * Sign-out from google
     */
    public void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            //updateUI(false);
        }
    }


    @SuppressLint("NewApi")
    public void checkgoogleplusid() {
//        final String url = Liveurl + "gpid_check?gp_id=" + gpid + "&regid=" + regid + "&imei=" + imei;
//        System.out.println("URL is" + url);
//        // Creating volley request obj
//        JsonArrayRequest movieReq = new JsonArrayRequest(url,
//                new Response.Listener<JSONArray>() {
//                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                    @SuppressLint("NewApi")
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//
//                        // Parsing json
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//
//                                login_jsonobj = response.getJSONObject(i);
//                                login_status = login_jsonobj.getString("status");
//                                Log.d("OUTPUT IS", login_status);
//                                if (login_status.matches("Success")) {
//                                    User_id = login_jsonobj.getString("id");
//
//                                }
//
//
//                                if (login_status.matches("Success")) {
//
//
//                                    signOutFromGplus();
//                                    WhoLogin = "GooglePlus";
//                                    loadSavedPreferences();
//
//                                    Intent fblogin = new Intent(getApplicationContext(), SlideMainActivity.class);
//                                    fblogin.putExtra("userid", User_id);
//                                    fblogin.putExtra("fbuserproimg", gproimg);
//                                    System.out.println("USerDetailsFb Profile imag" + gproimg);
//                                    fblogin.putExtra("whologin", WhoLogin);
//                                    System.out.println("Whologin insite the user details activity Login" + WhoLogin);
//
//                                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                                    startActivity(fblogin, bndlanimation);
//                                    finish();
//
//
//                                } else if (login_status.matches("empty_Nonce")) {
//                                    User_id = login_jsonobj.getString("id");
//                                    WhoLogin = "GooglePlus";
//
//                                    loadSavedPreferences();
//                                    signOutFromGplus();
//                                    Intent fblogin = new Intent(SigninActivity.this, Card_details.class);
//                                    fblogin.putExtra("userid", User_id);
//                                    fblogin.putExtra("fbuserproimg", pimg);
//                                    System.out.println("USerDetailsFb Profile imag" + pimg);
//                                    fblogin.putExtra("whologin", WhoLogin);
//                                    System.out.println("Whologin insite the user details activity Login" + WhoLogin);
//                                    //Toast.makeText(getApplicationContext(), " Success Id"+ User_id, Toast.LENGTH_LONG).show();
//                                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                                    startActivity(fblogin, bndlanimation);
//                                    finish();
//
//
//                                } else if (login_status.matches("Invalid Id")) {
//                                    signOutFromGplus();
//                                    WhoLogin = "GooglePlus";
//
//                                    Intent fbsignup = new Intent(getApplicationContext(), SignUpActivity.class);
//                                    fbsignup.putExtra("fbuserid", gpid);
//
//                                    fbsignup.putExtra("fbuserfname", gpfirstname);
//                                    fbsignup.putExtra("fbuseremail", gpemailid);
//                                    fbsignup.putExtra("fbuserproimg", gproimg);
//                                    System.out.println("USerDetailsFb Profile imag" + gproimg);
//                                    fbsignup.putExtra("whologin", WhoLogin);
//                                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                                    startActivity(fbsignup, bndlanimation);
//
//                                } else if (login_status.matches("Duplicate")) {
//                                    signOutFromGplus();
//                                    already_loggedin_alert();
//
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//
//
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(movieReq);
    }


    public void already_loggedin_alert() {
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        //  Setting Dialog Title
        builder.setTitle("Warning!");

        // Setting Dialog Message
        builder.setMessage(R.string.you_have_already_logged_in_another_device);

        // Setting Icon to Dialog

        // Setting Positive "Yes" Btn
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);

                    }
                });

        // Showing Alert Dialog
        builder.show();
    }

    public void getFbKeyHash(String packageName) {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("YourKeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }


    private void displayMessage(Profile profile) {
        System.out.println("Insite display message" + profile);

        if (profile != null) {
            try {
                profilecount = 1;
                firstName = profile.getFirstName();
                lastName = profile.getLastName();
                pimg = profile.getProfilePictureUri(120, 120).toString();
                fb_id = profile.getId();
                fb_check();
                loadimage.setVisibility(View.VISIBLE);
            } catch (Exception e) {

            }

        }
    }

    public void fb_check() {
//        final String url = Liveurl + "fbid_check1?fb_id=" + fb_id + "&regid=" + regid + "&imei=" + imei;
//        System.out.println("URL is" + url);
//        loadimage.setVisibility(View.VISIBLE);
//        // Creating volley request obj
//        JsonArrayRequest movieReq = new JsonArrayRequest(url,
//                new Response.Listener<JSONArray>() {
//                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                    @SuppressLint("NewApi")
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//
//                        // Parsing json
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//
//                                login_jsonobj = response.getJSONObject(i);
//                                login_status = login_jsonobj.getString("status");
//                                Log.d("OUTPUT IS", login_status);
//                                if (login_status.matches("Success")) {
//                                    User_id = login_jsonobj.getString("id");
//                                    loginManager.getInstance().logOut();
//
//
//                                }
//
//                                if (login_status.matches("Success")) {
//
//
//                                    loadimage.setVisibility(View.VISIBLE);
//                                    // loginManager.getInstance().logOut();
//                                    WhoLogin = "FaceBook";
//                                    loadSavedPreferences();
//
//                                    Intent fblogin = new Intent(SigninActivity.this, SlideMainActivity.class);
//                                    fblogin.putExtra("userid", User_id);
//                                    fblogin.putExtra("fbuserproimg", pimg);
//                                    System.out.println("USerDetailsFb Profile imag" + pimg);
//                                    fblogin.putExtra("whologin", WhoLogin);
//                                    System.out.println("Whologin insite the user details activity Login" + WhoLogin);
//                                    //Toast.makeText(getApplicationContext(), " Success Id"+ User_id, Toast.LENGTH_LONG).show();
//                                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                                    startActivity(fblogin, bndlanimation);
//                                    finish();
//
//                                } else if (login_status.matches("empty_Nonce")) {
//                                    WhoLogin = "FaceBook";
//                                    User_id = login_jsonobj.getString("id");
//
//                                    loadSavedPreferences();
//                                    Intent fblogin = new Intent(SigninActivity.this, Card_details.class);
//                                    fblogin.putExtra("userid", User_id);
//                                    fblogin.putExtra("fbuserproimg", pimg);
//                                    System.out.println("USerDetailsFb Profile imag" + pimg);
//                                    fblogin.putExtra("whologin", WhoLogin);
//                                    System.out.println("Whologin insite the user details activity Login" + WhoLogin);
//                                    //Toast.makeText(getApplicationContext(), " Success Id"+ User_id, Toast.LENGTH_LONG).show();
//                                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                                    startActivity(fblogin, bndlanimation);
//                                    finish();
//
//
//                                } else if (login_status.matches("Invalid Id")) {
//                                    WhoLogin = "FaceBook";
//                                    loadimage.setVisibility(View.VISIBLE);
//                                    // loginManager.getInstance().logOut();
//                                    Intent fbsignup = new Intent(SigninActivity.this, SignUpActivity.class);
//                                    fbsignup.putExtra("fbuserid", fb_id);
//
//                                    fbsignup.putExtra("fbuserfname", firstName);
//                                    fbsignup.putExtra("fbuserlname", lastName);
//
//                                    fbsignup.putExtra("fbuserproimg", pimg);
//                                    System.out.println("USerDetailsFb Profile imag" + pimg);
//                                    fbsignup.putExtra("whologin", WhoLogin);
//                                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                                    startActivity(fbsignup, bndlanimation);
//                                } else if (login_status.matches("Duplicate")) {
//                                    already_loggedin_alert();
//                                }
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            loadimage.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//
//                        VolleyLog.d(TAG, "Error: " + error.getMessage());
//
//
//                    }
//                });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(movieReq);
    }








}
