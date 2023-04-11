package com.test.recipeapp;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class viewholder_nutritions extends RecyclerView.Adapter<viewholder_nutritions.MyViewHolder> {
    private Context mContext;
    private List<Nutritions> mData;

    viewholder_nutritions(Context mContext, List<Nutritions> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.list_layout_nutritions, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txt_name.setText(mData.get(position).getTitle());
        holder.txt_amount.setText(mData.get(position).getAmount());
        holder.txt_percent.setText(mData.get(position).getPercentOfDailyNeeds()+"%");


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name,txt_amount,txt_percent;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_amount = itemView.findViewById(R.id.amount);

            txt_percent = itemView.findViewById(R.id.txt_percent);
        }
    }

}

