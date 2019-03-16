package com.simple.reaz.fffcom.SeeAll;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.simple.reaz.fffcom.Home.ModelCategories;
import com.simple.reaz.fffcom.JobPost.Model_Jobpost;
import com.simple.reaz.fffcom.R;

import java.util.ArrayList;
import java.util.List;

import co.gofynd.gravityview.GravityView;

public class SeeAll extends AppCompatActivity {

    List<ModelCategories> cat_list= new ArrayList<>();
    List<Model_Jobpost> job_post= new ArrayList<>();
    RecyclerView recyclerView;

    private ImageView imageView;
    private GravityView gravityView;
    private boolean suported =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        init();
        if(suported){
            gravityView.setImage(imageView,R.drawable.bg).center();
        }else {
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.bg);
            imageView.setImageBitmap(bitmap);

        }
        recyclerView=findViewById(R.id.seeAllRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SeeAll.this, LinearLayoutManager.VERTICAL, false));

        cat_list = (List<ModelCategories>) getIntent().getSerializableExtra("category");
        job_post = (List<Model_Jobpost>) getIntent().getSerializableExtra("job_list");
        if(cat_list!=null){
            recyclerView.setAdapter(new SeeAllAdapter(SeeAll.this, cat_list));
        }
        if(job_post!=null){
            recyclerView.setAdapter(new SeeAllAdapterJob(SeeAll.this, job_post));
        }

    }

    private void init() {
        this.imageView=findViewById(R.id.panaroma);
        this.gravityView=GravityView.getInstance(getBaseContext());
        this.suported=gravityView.deviceSupported();
    }
    @Override
    protected void onResume() {
        super.onResume();
        gravityView.registerListener();
    }
    @Override
    protected void onStop() {
        super.onStop();
        gravityView.unRegisterListener();
    }
}
