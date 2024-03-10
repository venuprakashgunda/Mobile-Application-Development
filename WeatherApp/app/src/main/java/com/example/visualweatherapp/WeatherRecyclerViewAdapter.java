package com.example.visualweatherapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualweatherapp.domain.Hourly;

import java.util.ArrayList;


public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Hourly> hourlyArrayList;
    private MainActivity mainActivity;

    public WeatherRecyclerViewAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setHourlyArrayList(ArrayList<Hourly> hourlyArrayList) {
        this.hourlyArrayList = hourlyArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weatherlist, parent, false);
        view.setOnClickListener(mainActivity);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hourly hourly = hourlyArrayList.get(position);
        holder.dayName.setText(hourly.getDayName());
        holder.time.setText(hourly.getTime());
        holder.temp.setText(hourly.getTemp());
        holder.tempDesc.setText(hourly.getDesc());
        holder.imageView.setImageResource(mainActivity.getIcon(hourly.getIcon()));
    }

    @Override
    public int getItemCount() {
        if (hourlyArrayList == null) {
            return 0;
        }
        return hourlyArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dayName;
        TextView time;
        TextView temp;
        TextView tempDesc;
        ImageView imageView;


        //public ViewHolder(View iV) {
        //  super(iV)
        //  dayName = iV.findViewById(R.id.dayName);
        //  time = iV.findViewById(R.id.time);
        //  temp = iV.findViewById(R.id.temp);
        //}


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.dayName);
            time = itemView.findViewById(R.id.time);
            temp = itemView.findViewById(R.id.temp);
            tempDesc = itemView.findViewById(R.id.tempdesc);
            imageView = itemView.findViewById(R.id.hourIcon);
        }

    }
}
