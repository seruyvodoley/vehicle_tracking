package com.example.vehicletracking.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vehicletracking.R;
import com.example.vehicletracking.models.CarModel;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private final List<CarModel> carList;
    private final Context context;
    private final OnCarClickListener listener;

    public CarAdapter(List<CarModel> carList, Context context, OnCarClickListener listener) {
        this.carList = carList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_layout, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CarModel car = carList.get(position);
        holder.carName.setText(car.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCarClick(car);
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView carName;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.txtcarName);
        }
    }
}
