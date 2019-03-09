package com.simple.reaz.fffcom.JobPost;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.simple.reaz.fffcom.API.ApiClient;
import com.simple.reaz.fffcom.API.ApiInterface;
import com.simple.reaz.fffcom.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobPost extends AppCompatActivity {

    private EditText imlookingfor;
    private String imlookingforString, cat_, sub_cat;
    private Spinner category, subcategory;
    private ImageView image_job;
    private Button attachphoto, job_post;
    ProgressDialog progressDoalog;
    SharedPreferences sharePreferenceRead;
    SharedPreferences.Editor sharedPreferenceEditor;
    String mobile;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private static final int IMG_REQUEST = 1;
    Bitmap bitmap;

    String[] categorySpinner = {"Choose category", "Nakshekhata", "Ornaments", "Mini-Pallet", "Maxres", "Other"};
    String[] subcategorySpinner = {"Choose subcategory", "Nakshekhata", "Ornaments", "Mini-Pallet", "Maxres", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post);

        imlookingfor = findViewById(R.id.imlookingfor);
        category = findViewById(R.id.category);
        subcategory = findViewById(R.id.subcategory);
        job_post = findViewById(R.id.job_post);
        attachphoto = findViewById(R.id.attachphoto);
        image_job = findViewById(R.id.image_job);

        sharePreferenceRead = getSharedPreferences("user", MODE_PRIVATE);
        sharedPreferenceEditor = sharePreferenceRead.edit();
        mobile=getIntent().getStringExtra("mobile");
        mobile = sharePreferenceRead.getString("mobile", mobile);
        Toast.makeText(JobPost.this, ""+mobile, Toast.LENGTH_SHORT).show();


        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);


        ArrayAdapter c = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorySpinner);
        category.setAdapter(c);
        ArrayAdapter sc = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subcategorySpinner);
        subcategory.setAdapter(sc);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat_ = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_cat = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // ----attach photo-----

        attachphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();

            }

        });

        job_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imlookingforString = imlookingfor.getText().toString().trim();
                if(!imlookingforString.isEmpty())
                job_post(mobile,imlookingforString, cat_, sub_cat, "", "");
                else {
                    Toast.makeText(JobPost.this, "Description must not empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void job_post(String mobile,
                         String description,
                         String category,
                         String sub_category,
                         String price,
                         String location) {
        progressDoalog = new ProgressDialog(JobPost.this);
        progressDoalog.setMessage("Loading...");
        progressDoalog.show();

        String jobImage = imageToString();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Model_Jobpost> call = apiInterface.job_post(mobile, description, category, sub_category, jobImage, price,location);
        call.enqueue(new Callback<Model_Jobpost>() {
            @Override
            public void onResponse(Call<Model_Jobpost> call, Response<Model_Jobpost> response) {
                Model_Jobpost model_jobpost=  response.body();
                Toast.makeText(JobPost.this, ""+model_jobpost.getResponse(), Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_Jobpost> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(JobPost.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);

    }

    private String imageToString(){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,30,byteArrayOutputStream);
        byte[] imagebyte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imagebyte,Base64.DEFAULT);

    }

    // Check Permission for Camera
    private void checkPermission() {
        if(ActivityCompat.checkSelfPermission(JobPost.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(JobPost.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(JobPost.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(JobPost.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(JobPost.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(JobPost.this,permissionsRequired[2])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(JobPost.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(JobPost.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(JobPost.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", JobPost.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(JobPost.this, "Go to Permissions to Grant  Camera", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(JobPost.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

            Toast.makeText(JobPost.this, "Permissions Required", Toast.LENGTH_LONG).show();

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.putBoolean(permissionsRequired[1],true);
            editor.putBoolean(permissionsRequired[2],true);
            editor.commit();
        } else {

            selectImage();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){

            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                image_job.setImageBitmap(bitmap);
                image_job.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
