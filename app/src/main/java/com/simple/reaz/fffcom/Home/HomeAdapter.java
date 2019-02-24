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
import android.widget.Toast;

import com.simple.reaz.fffcom.R;
import com.simple.reaz.fffcom.Test;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mir Reaz Uddin on 12-Jan-19.
 */
public class HomeAdapter extends RecyclerView.Adapter{

    Button button;
    List<ModelCategories>cat_list;
    Context context;
    HomeAdapter.CategoryHolder categoryHolder;
    public HomeAdapter(Context context, List<ModelCategories> cat_list) {
        this.cat_list = cat_list;
        this.context = context;
    }


    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row=inflater.inflate(R.layout.model_categories, parent, false);
        return new HomeAdapter.CategoryHolder(row);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        categoryHolder = (HomeAdapter.CategoryHolder) holder;
        categoryHolder.catName.setText(cat_list.get(position).getCat_name());

        Picasso.with(context)
                .load("http://fff-bd.com/freelancing/"+ cat_list.get(position).getCat_image()).fit()
                .into(categoryHolder.catImage);

        categoryHolder.parentLayouyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Test.class);
                context.startActivity(intent);


            }
        });

    }
    @Override
    public int getItemCount() {
        return cat_list.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView catName;
        ImageView catImage;
        LinearLayout parentLayouyt;

        public CategoryHolder(View itemView) {
            super(itemView);
            parentLayouyt = itemView.findViewById(R.id.parentLaout);
            catName = itemView.findViewById(R.id.cat_name);
            catImage = itemView.findViewById(R.id.cat_image);
        }

    }
}
