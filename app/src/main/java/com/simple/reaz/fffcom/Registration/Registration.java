package com.simple.reaz.fffcom.Registration;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.simple.reaz.fffcom.Home.HomeActivity;
import com.simple.reaz.fffcom.LoginRegisterPanel.RegisterActivity;
import com.simple.reaz.fffcom.OnboardingScreen.PreferenceManager;
import com.simple.reaz.fffcom.OnboardingScreen.WelcomeActivity;
import com.simple.reaz.fffcom.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Registration extends AppCompatActivity {
    private CardView loginWithNumber;
    private final static int REQUEST_CODE = 999;
    String mobile_str;
    SharedPreferences sharePreferenceRead;
    SharedPreferences.Editor sharedPreferenceEditor;
    final private static int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //---Register Status Checking---
        sharePreferenceRead = getSharedPreferences("user", MODE_PRIVATE);
        sharedPreferenceEditor = sharePreferenceRead.edit();

        //printKeyHash();
        final boolean status = sharePreferenceRead.getBoolean("status", false);
        if (status == true) {
            Intent intent = new Intent(Registration.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        loginWithNumber = findViewById(R.id.loginWithNumber);

        if (requestContactPermission()) {
            loginWithNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoginPage(LoginType.PHONE);
                }
            });
        } else requestContactPermission();

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
                    sharedPreferenceEditor.commit();
                    Toast.makeText(Registration.this, ""+mobile_str, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
            }
        });
    }

    private boolean requestContactPermission() {
        int hasContactPermission = ActivityCompat.checkSelfPermission(Registration.this, Manifest.permission.RECEIVE_SMS);
        if (hasContactPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Registration.this, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
        } else {

            Toast.makeText(Registration.this, "Contact Permission is already granted", Toast.LENGTH_LONG).show();
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

                Intent intent =new Intent(Registration.this,HomeActivity.class);
                intent.putExtra("mobile",mobile_str);
                sharedPreferenceEditor.putBoolean("status", true);
                sharedPreferenceEditor.commit();
                startActivity(intent);
                finish();
            }
        }
    }

    private void printKeyHash() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.simple.reaz.fffcom",
                    PackageManager.GET_SIGNATURES);

            for(Signature signature:info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHashh", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
