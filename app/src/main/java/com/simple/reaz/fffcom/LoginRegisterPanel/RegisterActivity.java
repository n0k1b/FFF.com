package com.simple.reaz.fffcom.LoginRegisterPanel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.opengl.Visibility;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.simple.reaz.fffcom.API.ApiClient;
import com.simple.reaz.fffcom.API.ApiInterface;
import com.simple.reaz.fffcom.Home.HomeActivity;
import com.simple.reaz.fffcom.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class RegisterActivity extends AppCompatActivity {
    private final static int REQUEST_CODE = 999;
    private boolean page = false;

    ProgressDialog progressDoalog;
    Button register, loginbtn;
    TextView signup_txt_btn, login_txt_btn;
    EditText mobile, password, register_username, register_mobile, register_pass, register_con_pass;
    RelativeLayout login_layout;
    RelativeLayout register_layout;
    String mobile_id, password_login, name_str, mobile_str, pass_str, con_pass_str;
    SharedPreferences sharePreferenceRead;
    SharedPreferences.Editor sharedPreferenceEditor;
    LinearLayout reg_layout;
    Button numb;
    final private static int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        register_layout = findViewById(R.id.register_layout);
        login_layout = findViewById(R.id.login_layout);
        //---log in layout---
        reg_layout = findViewById(R.id.reg_layout);
        numb = findViewById(R.id.numb);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginbtn);
        signup_txt_btn = findViewById(R.id.signup_txt_btn);
        //---Register layput---
        login_txt_btn = findViewById(R.id.login_txt_btn);
        register = findViewById(R.id.register_button);
        register_username = findViewById(R.id.register_username);
        register_mobile = findViewById(R.id.register_mobile);
        register_pass = findViewById(R.id.register_pass);
        register_con_pass = findViewById(R.id.register_con_pass);

        //---Register Status Checking---
        sharePreferenceRead = getSharedPreferences("user", MODE_PRIVATE);
        sharedPreferenceEditor = sharePreferenceRead.edit();

        final boolean status = sharePreferenceRead.getBoolean("status", false);
        if (status == true) {
            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            signup_txt_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    page = true;
                    login_layout.setVisibility(GONE);
                    register_layout.setVisibility(VISIBLE);
                }
            });
            login_txt_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login_layout.setVisibility(VISIBLE);
                    register_layout.setVisibility(GONE);
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    con_pass_str = register_con_pass.getText().toString().trim();
                    name_str = register_username.getText().toString().trim();
                    mobile_str = register_mobile.getText().toString().trim();
                    pass_str = register_pass.getText().toString().trim();
                    if (!name_str.equals("") && !mobile_str.equals("") && !pass_str.equals("") && !con_pass_str.equals("")) {
                        if (mobile_str.length() == 11) {
                            if (mobile_str.startsWith("018") || mobile_str.startsWith("016")
                                    || mobile_str.startsWith("013") || mobile_str.startsWith("017")
                                    || mobile_str.startsWith("015") || mobile_str.startsWith("011")
                                    || mobile_str.startsWith("019") || mobile_str.startsWith("014")) {

                                if (!pass_str.equals(con_pass_str)) {

                                    Toast.makeText(RegisterActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
                                } else
                                    registaration(mobile_str, name_str, pass_str);

                            } else {
                                Toast.makeText(RegisterActivity.this, "Please provide a valid number", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Please provide a valid number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Field must not empty", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            if (requestContactPermission()) {
                numb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startLoginPage(LoginType.PHONE);
                    }
                });
            } else requestContactPermission();

            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mobile_id = mobile.getText().toString().trim();
                    password_login = password.getText().toString().trim();

                    if (mobile_id.equals("") || password_login.equals("")) {

                        Toast.makeText(RegisterActivity.this, "Field must not empty..", Toast.LENGTH_SHORT).show();
                    } else {
                        log_in(mobile_id, password_login);
                    }


                }
            });
        }
    }

    public void registaration(String user_mobile, String user_name, String password) {
        progressDoalog = new ProgressDialog(RegisterActivity.this);
        progressDoalog.setMessage("Registration in progress....");
        progressDoalog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ModelUser> call = apiInterface.user_registration(user_mobile, user_name, password);
        call.enqueue(new Callback<ModelUser>() {
            @Override
            public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {

                if (response.body().getResponse().equals("ok")) {
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    register_username.setText("");
                    register_mobile.setText("");
                    register_pass.setText("");
                    register_con_pass.setText("");
                    login_layout.setVisibility(VISIBLE);
                    register_layout.setVisibility(GONE);

                } else if (response.body().getResponse().equals("exist")) {
                    Toast.makeText(RegisterActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("error")) {
                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<ModelUser> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });


    }


    private void log_in(final String mobile_id, String password_login) {

        progressDoalog = new ProgressDialog(RegisterActivity.this);
        progressDoalog.setMessage("checking...");
        progressDoalog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ModelUser> call = apiInterface.user_login(mobile_id, password_login);
        call.enqueue(new Callback<ModelUser>() {
            @Override
            public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {

                if (response.body().getResponse().equals("ok")) {
                    sharedPreferenceEditor.putBoolean("status", true);
                    sharedPreferenceEditor.putString("mobile", mobile_id);
                    sharedPreferenceEditor.commit();
                    Toast.makeText(RegisterActivity.this, "Loged in successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.body().getResponse().equals("failed")) {
                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<ModelUser> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(RegisterActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

    }

    protected void onResume() {
        super.onResume();
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (phoneNumber != null) {
                    mobile_str = phoneNumber.toString();
                    mobile_str = mobile_str.replaceAll("\\+88", "");
                    sharedPreferenceEditor.putString("mobile", mobile_str);
                    //  Toast.makeText(RegisterActivity.this, ""+mobileNo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
            }
        });
    }

    private boolean requestContactPermission() {
        int hasContactPermission = ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.RECEIVE_SMS);
        if (hasContactPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
        } else {

            //Toast.makeText(AddContactsActivity.this, "Contact Permission is already granted", Toast.LENGTH_LONG).show();
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Contact permission has now been granted. Showing result.");
                    Toast.makeText(this, "Contact Permission is Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("Permission", "Contact permission was NOT granted.");
                }
                break;
        }
    }

    public void startLoginPage(LoginType loginType) {
        if (loginType == LoginType.PHONE) {
            Intent intent = new Intent(this, AccountKitActivity.class);

            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                            AccountKitActivity.ResponseType.TOKEN);
            configurationBuilder.setReadPhoneStateEnabled(true);
            configurationBuilder.setReceiveSMS(true);
            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
            startActivityForResult(intent, REQUEST_CODE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null) {
                Toast.makeText(this, result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                return;
            } else if (result.wasCancelled()) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                return;
            } else {
                numb.setVisibility(GONE);
                reg_layout.setVisibility(VISIBLE);

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (page == true) {
            register_layout.setVisibility(GONE);
            login_layout.setVisibility(VISIBLE);
            page = false;

        } else {
            super.onBackPressed();
        }

    }
}




