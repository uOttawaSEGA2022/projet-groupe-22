package com.example.mealer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealer.Meal;
import com.example.mealer.R;
import com.example.mealer.listener.ICartLoadListener;
import com.example.mealer.model.MealModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyMealAdapter extends RecyclerView.Adapter<MyMealAdapter.MyMealViewHolder> {

    private Context context;
    private List<Meal> mealModelList;
    private ICartLoadListener iCartLoadListener;

    public MyMealAdapter(Context context, List<Meal> mealModelList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.mealModelList = mealModelList;
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @Override
    public MyMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyMealViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_meals_list, parent, false));
    }
//not used
    @Override
    public void onBindViewHolder(@NonNull MyMealViewHolder holder, int position) {
        holder.textPrice.setText(new StringBuilder("$").append(mealModelList.get(position).getPrice()));
        holder.textViewGastronomyType.setText(new StringBuilder().append(mealModelList.get(position).getGastronomyType()));
        holder.textViewChefName.setText(new StringBuilder().append(mealModelList.get(position).getChefName()));
        holder.textViewMealName.setText(new StringBuilder().append(mealModelList.get(position).getMealName()));
        holder.textViewMealType.setText(new StringBuilder().append(mealModelList.get(position).getMealType()));
    }

    @Override
    public int getItemCount() {
        return mealModelList.size();
    }

    public class MyMealViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textViewGastronomyType)
        TextView textViewGastronomyType;
        @BindView(R.id.textViewChefName)
        TextView textViewChefName;
        @BindView(R.id.textViewMealType)
        TextView textViewMealType;
        @BindView(R.id.textViewMealName)
        TextView textViewMealName;
        @BindView(R.id.textPrice)
        TextView textPrice;

        private Unbinder unbinder;
        public MyMealViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
