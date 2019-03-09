package com.simple.reaz.fffcom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.simple.reaz.fffcom.Home.ModelCategories;

import java.util.ArrayList;
import java.util.List;

public class SeeAll extends AppCompatActivity {

    List<ModelCategories> cat_list= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);


        cat_list = (List<ModelCategories>) getIntent().getSerializableExtra("category");

        if(cat_list!=null)
        Toast.makeText(this, ""+cat_list.get(0).getCat_image()+"\n"+
                cat_list.get(1).getCat_image(), Toast.LENGTH_SHORT).show();

    }

}
