package com.example.foodapp.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.model.PromoDish;

import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewAdapter.ViewHolder> {

    private List<PromoDish> promoDishList;

    public HomeViewAdapter(List<PromoDish> promoDishList) {
        this.promoDishList = promoDishList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.todaypromo_recyclerview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + promoDishList.get(position).getDishTitle());
        PromoDish promoDish = promoDishList.get(position);
        int img = promoDish.getDishImage();
        String title = promoDish.getDishTitle();
        String description = promoDish.getDishDescription();
        String price = promoDish.getDishPrice();

        holder.setDishImageView(img);
        holder.setDishName(title);
        holder.setDishDescription(description);
        holder.setDishPrice(price);
    }

    @Override
    public int getItemCount() {
        return promoDishList.size();
    }

    public LiveData<String> getText() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView dishImageView;
        public TextView dishName;
        public TextView dishDescription;
        public TextView dishPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishImageView = itemView.findViewById(R.id.dishImageView);
            dishName = itemView.findViewById(R.id.dishTitleName);
            dishDescription = itemView.findViewById(R.id.dishDescription);
            dishPrice = itemView.findViewById(R.id.dishPrice);

        }

        public void setDishImageView(int resource){
            dishImageView.setImageResource(resource);
        }

        public void setDishName(String name) {
            dishName.setText(name);
        }

        public void setDishDescription(String description) {
            dishDescription.setText(description);
        }

        public void setDishPrice(String price) {
            dishPrice.setText(String.format("Rs %s", price));
        }
    }
}