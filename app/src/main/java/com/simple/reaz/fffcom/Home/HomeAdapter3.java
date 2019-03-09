package com.simple.reaz.fffcom.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simple.reaz.fffcom.JobPost.Model_Jobpost;
import com.simple.reaz.fffcom.R;
import com.simple.reaz.fffcom.Test;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mir Reaz Uddin on 12-Jan-19.
 */
public class HomeAdapter3 extends RecyclerView.Adapter{

    Button button;
    List<Model_Jobpost>cat_list;
    Context context;
    HomeAdapter3.CategoryHolder categoryHolder;
    public HomeAdapter3(Context context, List<Model_Jobpost> cat_list) {
        this.cat_list = cat_list;
        this.context = context;
    }


    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row=inflater.inflate(R.layout.model_jcategories3, parent, false);
        return new HomeAdapter3.CategoryHolder(row);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        categoryHolder = (HomeAdapter3.CategoryHolder) holder;
        categoryHolder.catName.setText(cat_list.get(position).getCategory());
        categoryHolder.location.setText(cat_list.get(position).getLocation());
        categoryHolder.budget.setText(cat_list.get(position).getPrice());

        Picasso.with(context)
                .load("http://fff-bd.com/freelancing/"+ cat_list.get(position).getImage()).fit()
                .into(categoryHolder.catImage);


    }
    @Override
    public int getItemCount() {
        return cat_list.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView catName,location,budget;
        ImageView catImage;
        LinearLayout parentLayouyt;

        public CategoryHolder(View itemView) {
            super(itemView);
            parentLayouyt = itemView.findViewById(R.id.parentLaout);
            catName = itemView.findViewById(R.id.cat_name);
            location = itemView.findViewById(R.id.location);
            budget = itemView.findViewById(R.id.budget);
            catImage = itemView.findViewById(R.id.cat_image);
        }
    }
}
