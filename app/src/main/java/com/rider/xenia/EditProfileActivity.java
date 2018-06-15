package com.rider.xenia;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.app_controller.AppController;
import com.custom.CustomProgressDialog;
import com.grepix.grepixutils.GrepixUtils;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebService;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity {


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;


    protected static final String TAG = "EditProfileActivity";


    String User_id;
    String fbuserproimg;


    Bitmap editBitmap;

    String WhoLogin;
    String checkpassword;


    String accept;
    boolean isPasswordChange;


    private static final int RESULT_LOAD_IMAGE = 3;
    private static final int CAMERA_REQUEST = 1;
    private long lastClickTime = 0;
    private static final int PICK_FROM_CAMERA = 1;



    @BindView(R.id.edit_switch)
    Switch editSwitch;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    @BindView(R.id.edit_first_name)
    EditText firstName;
    @BindView(R.id.edit_last_name)
    EditText lastName;
    @BindView(R.id.edit_email)
    TextView email;
    @BindView(R.id.edit_mobile)
    EditText mobile;
    @BindView(R.id.edit_old_password)
    EditText oldPassword;
    @BindView(R.id.edit_new_password)
    EditText newPassword;
    @BindView(R.id.edit_confirm_password)
    EditText confirmPassword;
    @BindView(R.id.edit_change_password_layout)
    LinearLayout cahnegPasswordLayout;

    CircularImageView1 profileImage;

    private AppController controller;
    private DriverInfo userInfo;
    private CustomProgressDialog progressDialog;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        progressDialog=new CustomProgressDialog(this);
        //  profileImage = (CircularImageView1) findViewById(R.id.edit_profile_image);
        profileImage = (CircularImageView1) findViewById(R.id.edit_profile_image);
        ButterKnife.bind(this);

        cahnegPasswordLayout.setVisibility(View.GONE);
        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cahnegPasswordLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                isPasswordChange=isChecked?true:false;
            }
        });
        controller = (AppController) getApplication();
        ParseJson parseJson = new ParseJson(this);
        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        setUserInfo();
        email.setClickable(false);
    }


    public void setUserInfo() {
        firstName.setText(userInfo.getUFname());
        lastName.setText(userInfo.getULname());
        email.setText(userInfo.getUEmail());
        mobile.setText(userInfo.getUPhone());
        profileImage.setImageUrl(Constants.Urls.IMAGE_BASE_URL + userInfo.getUProfileImagePath(), imageLoader);
        profileImage.setScaleType(ImageView.ScaleType.MATRIX);
        // profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }


    @OnClick(R.id.recancel)
    public void onCancel(View view) {
        onBackPressed();
    }

    @OnClick(R.id.edit_bt_save)
    public void saveProfile(View view) {
        if (Validations.isVaildateEditProfile(EditProfileActivity.this,firstName,lastName,mobile,oldPassword,newPassword,confirmPassword,controller.pref.getPassword(),isPasswordChange)) {
        updateProfile();
        }
    }

    private void updateProfile() {
        progressDialog.showDialog();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.U_FName, firstName.getText().toString());
        params.put(Constants.Keys.U_LName, lastName.getText().toString());
        params.put(Constants.Keys.U_PHONE, mobile.getText().toString());
        if (editSwitch.isChecked()) {
            params.put("u_password",newPassword.getText().toString());

        }
        params.put("user_id", controller.pref.getUserID());
        params.put("is_send_email", String.valueOf(1));
        WebService.excuteRequest(this, params,Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                progressDialog.dismiss();
                if (isUpdate) {
                    if(editSwitch.isChecked()){
                        controller.pref.savePassword(newPassword.getText().toString());
                        newPassword.setText("");
                        oldPassword.setText("");
                        confirmPassword.setText("");
                    }
                    controller.pref.saveUserLogin(data.toString());
                    ParseJson parseJson = new ParseJson(EditProfileActivity.this);
                    userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
                    setUserInfo();
                    Toast.makeText(getApplication(), R.string.profile_updated, Toast.LENGTH_LONG).show();
                } else {
                    if (error == null) {
                        Toast.makeText(EditProfileActivity.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    @OnClick(R.id.edit_profile_image)

    public void onProfileImageClick(View view) {
        try {
            android.support.v7.app.AlertDialog.Builder builder =
                    new android.support.v7.app.AlertDialog.Builder(EditProfileActivity.this, R.style.AppCompatAlertDialogStyle);
            builder.setMessage(R.string.please_select_a_profile_image);

            builder.setNegativeButton(R.string.camera, new DialogInterface.OnClickListener() {



                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(cameraIntent, CAMERA_REQUEST);


                }
            });
            builder.setNeutralButton(R.string.gallery, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {


                    Intent i = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);


                }
            });
            builder.show();
        }catch (Exception e) {
            Toast.makeText(controller, "please check your permission", Toast.LENGTH_SHORT).show();
        }
    }






    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.pback:
//                cancel();
                return true;
            case R.id.done1:
//                done1();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            editBitmap = photo;
            profileImage.setImageBitmap(photo);
            updateProfileImage(editBitmap);
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                    null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            editBitmap = BitmapFactory.decodeFile(picturePath);
            profileImage.setImageBitmap(editBitmap);
            updateProfileImage(editBitmap);
        }
    }


    private void updateProfileImage(Bitmap bitmap) {
        if (GrepixUtils.net_connection_check(EditProfileActivity.this)) {
            return;
        }
        showProgress();
        final String bitmap64 = convert(bitmap);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put("user_image", bitmap64);
        params.put("image_type", "jpg");
        params.put("user_id", controller.pref.getUserID());
        WebService.excuteRequest(this, params,Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    hideProgress();
                    if (data != null) {
                        controller.pref.saveUserLogin(data.toString());
                        ParseJson parseJson = new ParseJson(EditProfileActivity.this);
                        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
                        setUserInfo();
                    } else {
                        Toast.makeText(getApplication(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void showProgress() {
        progressDialog.showDialog();
    }

    private void hideProgress() {
        progressDialog.dismiss();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void onBackPressed() {
        finish();
    }




    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}