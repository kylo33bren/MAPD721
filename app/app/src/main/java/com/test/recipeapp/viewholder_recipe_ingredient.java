
package com.test.recipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class viewholder_recipe_ingredient extends RecyclerView.Adapter<viewholder_recipe_ingredient.MyViewHolder> {
    private Context mContext;
    private List<Ingredient> mData;
    public static List<String> ingredientsList;

    viewholder_recipe_ingredient(Context mContext, List<Ingredient> mData) {
        this.mContext = mContext;
        this.mData = mData;
        ingredientsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.list_layout_ingredients, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_ingredient_name.setText(mData.get(position).getName());
        Picasso.with(mContext).load(mData.get(position).getThumbnail()).into(holder.img_ingredient_thumbnail);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ingredient_name;
        ImageView img_ingredient_thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_ingredient_name = itemView.findViewById(R.id.recipe_ingredient_name);
            img_ingredient_thumbnail = itemView.findViewById(R.id.recipe_ingredient_img);
        }
    }

}
