
package com.test.recipeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class viewholder_recipes extends RecyclerView.Adapter<viewholder_recipes.MyViewHolder> {

    private Context mContext ;
    private List<Recipe> list ;

    public viewholder_recipes(Context mContext, List<Recipe> _list) {
        this.mContext = mContext;
        this.list = _list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.recipes_list_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_recipe_title.setText(list.get(position).getTitle());
        holder.tv_amount_of_dishes.setText(Integer.toString(list.get(position).getAmountOfDishes()) );
        holder.tv_ready_in_mins.setText( Integer.toString(list.get(position).getReadyInMins()) );
        if (list.get(position).getThumbnail().isEmpty()) {
            holder.img_recipe_thumbnail.setImageResource(R.drawable.ic_launcher_background);
        } else{
           // Picasso.get().load(list.get(position).getThumbnail()).into(holder.img_recipe_thumbnail);
            Picasso.with(mContext).load(list.get(position).getThumbnail()).into(holder.img_recipe_thumbnail);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,activity_recipe_detail.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("title",list.get(position).getTitle());
                intent.putExtra("img",list.get(position).getThumbnail());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_recipe_title,tv_amount_of_dishes,tv_ready_in_mins;
        ImageView img_recipe_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_recipe_title = (TextView) itemView.findViewById(R.id.recipe_title_id) ;
            img_recipe_thumbnail = (ImageView) itemView.findViewById(R.id.recipe_img_id);
            tv_amount_of_dishes = (TextView) itemView.findViewById(R.id.servingTvLeft);
            tv_ready_in_mins = (TextView) itemView.findViewById(R.id.readyInTvRight);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}
