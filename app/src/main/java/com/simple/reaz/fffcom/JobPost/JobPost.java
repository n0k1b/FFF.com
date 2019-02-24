package com.simple.reaz.fffcom.JobPost;

import android.app.ProgressDialog;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


            }

        });

        job_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imlookingforString = imlookingfor.getText().toString().trim();
                if(!imlookingforString.isEmpty())
                job_post(mobile,imlookingforString, cat_, sub_cat, "", "","");
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
                         String image,
                         String price,
                         String location) {
        progressDoalog = new ProgressDialog(JobPost.this);
        progressDoalog.setMessage("Loading...");
        progressDoalog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Model_Jobpost>> call = apiInterface.job_post(mobile, description, category, sub_category, image, price,location);
        call.enqueue(new Callback<List<Model_Jobpost>>() {
            @Override
            public void onResponse(Call<List<Model_Jobpost>> call, Response<List<Model_Jobpost>> response) {
                Toast.makeText(JobPost.this, ""+response.body(), Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Model_Jobpost>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(JobPost.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

    }


}
