

package com.test.recipeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class viewholder_favorites extends RecyclerView.Adapter<viewholder_favorites.MyViewHolder> {
    private Context mContext;
    private List<Recipe> list;

    viewholder_favorites(Context mContext, List<Recipe> _list) {
        this.mContext = mContext;
        this.list = _list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_favorite, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_recipe_title.setText(list.get(position).getTitle());
        if (list.get(position).getThumbnail().isEmpty()) {
        } else{
            Picasso.with(mContext).load(list.get(position).getThumbnail()).into(holder.img_recipe);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, activity_recipe_detail.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("title",list.get(position).getTitle());
                intent.putExtra("img",list.get(position).getThumbnail());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_recipe_title;
        ImageView img_recipe;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_recipe_title = itemView.findViewById(R.id.favorites_recipe_title);
            img_recipe = itemView.findViewById(R.id.favorites_recipe_img);
            cardView = itemView.findViewById(R.id.favorites_cardview);
        }
    }
}
