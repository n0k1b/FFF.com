package com.simple.reaz.fffcom.SeeAll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.simple.reaz.fffcom.Home.HomeAdapter;
import com.simple.reaz.fffcom.Home.ModelCategories;
import com.simple.reaz.fffcom.R;

import java.util.ArrayList;
import java.util.List;

public class SeeAll extends AppCompatActivity {

    List<ModelCategories> cat_list= new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        recyclerView=findViewById(R.id.seeAllRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SeeAll.this, LinearLayoutManager.VERTICAL, false));

        cat_list = (List<ModelCategories>) getIntent().getSerializableExtra("category");
        if(cat_list!=null){
            recyclerView.setAdapter(new SeeAllAdapter(SeeAll.this, cat_list));
        }

    }

}
