package com.rider.xenia;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
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
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebService;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult> {

    Button signinlink;
    ImageButton cancel;


    TextView facebookin;
    private JSONArray login_jsonarray;
    private JSONObject login_jsonobj;
    private String login_status;
    private String User_id;
    private String login_inputline;
    String WhoLogin;

    String firstName;
    String lastName;
    String pimg;
    String fb_id;

    Boolean Check_activity = false;

    ImageView loadimage;


    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.mobile_no)
    EditText mobileNo;
    @BindView(R.id.re_password)
    EditText repassword;
    @BindView(R.id.name)
    EditText name;

    //Google plus varibles
    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "RegisterActivity";

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

    private ConnectionResult mConnectionResult;

    String gpid, gproimg, gpersonGooglePlusProfile, gpemailid, gpfirstname;

    int count = 1;

    private CallbackManager callbackManager;


    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginManager loginManager;
    int profilecount = 0;
    private Controller controller;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        net_connection_check();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        controller = (Controller) getApplicationContext();
        progressDialog = new CustomProgressDialog(RegisterActivity.this);

        Facebook();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

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
        getFbKeyHash("com.driver.arcane_android");

        setContentView(R.layout.activity_register);


        getActionBar().hide();

        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        WhoLogin = "SingIn";


        cancel = (ImageButton) findViewById(R.id.cancel);


        facebookin = (TextView) findViewById(R.id.facebookbns);

        signinlink = (Button) findViewById(R.id.signinlink);

        signinlink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (net_connection_check()) {
                    /*Intent reset = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(reset);*/
                    finish();

                }

            }
        });


        facebookin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_activity = true;
//                LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile"));
                facebookSignUp();
            }
        });
//        googleplus = (Button) findViewById(R.id.googleplus);

        loadimage = (ImageView) findViewById(R.id.loadimage);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();


        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                email.setText(possibleEmail);
            }
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             /*   Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);*/
                finish();
            }
        });
    }

    boolean isCalling;

    @OnClick(R.id.reg_bt_join)
    public void joinButtonClicked(View view) {
        if (isCalling) {
            return;
        }

         if(Validations.isValidateRegister(RegisterActivity.this, email, mobileNo, password, repassword, name))


            progressDialog.showDialog();
            isCalling = true;
            final Map<String, String> params = new HashMap<String, String>();
            params.put("u_email", email.getText().toString());
            String userName = name.getText().toString();
            if (userName != null) {
                String names[] = userName.split(" ");
                if (names.length > 1) {
                    params.put("u_fname", names[0]);
                    params.put("u_lname", names[1]);
                } else {
                    params.put("u_fname", userName);
                }
            }
            params.put("u_password", password.getText().toString());
            params.put("u_phone", mobileNo.getText().toString());
            String deviceToken = getSaveDiceToken();
            if (deviceToken != null) {
                params.put("u_device_token", deviceToken);
                params.put("u_device_type", "Android");
            }
            System.out.println("Params : " + params);

        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_REGISTRATION, new WebService.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    progressDialog.showDialog();
                    isCalling = false;
                    if (isUpdate) {
                        login_Progress(data.toString());
                    } else {
                        if (error == null) {
                            Toast.makeText(RegisterActivity.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

    }







    private AccessToken accessToken;

    private void facebookSignUp() {
        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            List<String> permissionNeeds = Arrays.asList("user_photos", "email",
                    "user_birthday", "public_profile");
            LoginButton loginButton = new LoginButton(RegisterActivity.this);
            loginButton.setReadPermissions(permissionNeeds);

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    onLoginSuccess(loginResult.getAccessToken());

                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), R.string.on_cancel, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getApplicationContext(), getString(R.string.on_error) + error, Toast.LENGTH_LONG).show();
                }
            });
            loginButton.performClick();
        } else {
            onLoginSuccess(accessToken);
        }

    }

    public void onLoginSuccess(AccessToken accessToken) {
        this.accessToken = accessToken;


        GraphRequest request = GraphRequest.newMeRequest(
                this.accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        controller.setFacebookResponce(object);
                        Intent intent=new Intent(RegisterActivity.this, VerifyForFacebookLogin.class);
                        startActivity(intent);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }




    private String getSaveDiceToken() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(com.app.Config.SHARED_PREF, 0);
        return pref.getString("regId", null);
    }


    public int login_Progress(String loginResponse) {
        ParseJson parseJson = new ParseJson(RegisterActivity.this);
        DriverInfo responceLogin = parseJson.ParseLoginResponce(loginResponse.toString());
        controller.pref.saveUserLogin(loginResponse.toString());
        controller.pref.setUserApiKey(responceLogin.getApiKey());
        controller.pref.saveUserID(responceLogin.getUserId());
        controller.pref.saveEmail(email.getText().toString());
        controller.pref.savePassword(password.getText().toString());
        User_id = responceLogin.getUserId();
        controller.pref.saveIsLoginSucess(true);

        Intent main = new Intent(getApplicationContext(), MainScreenActivity.class);
        main.putExtra("userid", User_id);
        main.putExtra("whologin", WhoLogin);
        main.putExtra("password", password.getText().toString());
        startActivity(main);
        finish();
        return 100;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void onBackPressed() {
        super.onBackPressed();
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


    @Override
    public void onConnected(Bundle arg0) {
        if (!mSignInClicked) {
            signOutFromGplus();
        }
        mSignInClicked = false;

        getProfileInformation();
        if (count == 1) {
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
                //gpemailid = Plus.AccountApi.getAccountName(mGoogleApiClient);

                System.out.println("***********************************************Google plus Loginin ***********************************");
                System.out.println("GPID" + gpid);
                System.out.println("GPNAme" + gpfirstname);
                System.out.println("GP Profile image" + gpersonGooglePlusProfile);
                System.out.println("GP email ID" + gpemailid);
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.person_info_is_null, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.close();
            }
        } else {
            Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
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




    public void already_loggedin_alert() {
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        //  Setting Dialog Title
        builder.setTitle(R.string.warning);

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

    public void getFbKeyHash(String packageName) {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
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

            firstName = profile.getFirstName();
            lastName = profile.getLastName();
            pimg = profile.getProfilePictureUri(120, 120).toString();
            fb_id = profile.getId();
            loadimage.setVisibility(View.VISIBLE);
        }
    }



    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean net_connection_check() {
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
                        Toast.makeText(RegisterActivity.this, R.string.login_canceled_by_user, Toast.LENGTH_LONG).show();
                        System.out.println("Facebook Login failed!!");

                    }

                    @Override
                    public void onError(FacebookException e) {
                        Toast.makeText(RegisterActivity.this, R.string.login_unsuccessful, Toast.LENGTH_LONG).show();
                        System.out.println("Facebook Login failed!!");
                    }


                });

    }
}