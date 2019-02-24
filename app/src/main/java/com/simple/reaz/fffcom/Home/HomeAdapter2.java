package com.simple.reaz.fffcom.Home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simple.reaz.fffcom.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mir Reaz Uddin on 12-Jan-19.
 */
public class HomeAdapter2 extends RecyclerView.Adapter{

    List<ModelCategories>cat_list;
    Context context;
    HomeAdapter2.CategoryHolder categoryHolder;
    public HomeAdapter2(Context context, List<ModelCategories> cat_list) {
        this.cat_list = cat_list;
        this.context = context;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row=inflater.inflate(R.layout.model_categories2, parent, false);
        return new HomeAdapter2.CategoryHolder(row);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        categoryHolder = (HomeAdapter2.CategoryHolder) holder;
        categoryHolder.catName.setText(cat_list.get(position).getCat_name());

        Picasso.with(context)
                .load("http://fff-bd.com/freelancing/"+ cat_list.get(position).getCat_image()).fit()
                .into(categoryHolder.catImage);

    }
    @Override
    public int getItemCount() {
        return cat_list.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView catName;
        ImageView catImage;

        public CategoryHolder(View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.cat_name);
            catImage = itemView.findViewById(R.id.cat_image);
        }
    }
}
