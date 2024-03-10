package com.example.visualweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualweatherapp.domain.Daily;

import java.util.ArrayList;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.ViewHolder> {

    private ArrayList<Daily> dailyArrayList;
    private DailyForeCastActivity dailyForeCastActivity;

    public DailyForecastAdapter(ArrayList<Daily> dailyArrayList, DailyForeCastActivity dailyForeCastActivity) {
        this.dailyArrayList = dailyArrayList;
        this.dailyForeCastActivity = dailyForeCastActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailyforecastlist, parent, false);
        return new ViewHolder(view);
    }

    public int getIcon(String name) {
        return dailyForeCastActivity.getResources().getIdentifier("_"+name, "drawable", dailyForeCastActivity.getPackageName());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Daily daily = dailyArrayList.get(position);
        holder.dayTime.setText(daily.getDayTime());
        holder.tempminmax.setText(daily.getMax()+"/"+daily.getMin());
        holder.description.setText(daily.getDesc());
        holder.pop.setText("(" + daily.getPop() + "%precip.)");
        holder.uvi.setText("uvi: " + daily.getuvi());
        holder.time8am.setText(daily.getMon());
        holder.time1pm.setText(daily.getDay());
        holder.time5pm.setText(daily.getEve());
        holder.time11pm.setText(daily.getNight());
        holder.imageView.setImageResource(getIcon(daily.getIcon()));
    }

    @Override
    public int getItemCount() {
        return dailyArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTime;
        TextView tempminmax;
        TextView description;
        TextView pop;
        TextView uvi;
        TextView time8am;
        TextView time1pm;
        TextView time5pm;
        TextView time11pm;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTime = itemView.findViewById(R.id.daytime);
            //popup = itemView.findViewById(R.id.popup)
            tempminmax = itemView.findViewById(R.id.tempminmax);
            description = itemView.findViewById(R.id.description);
            pop = itemView.findViewById(R.id.pop);
            uvi = itemView.findViewById(R.id.uvi);
            time8am = itemView.findViewById(R.id.time8am);
            time1pm = itemView.findViewById(R.id.time1pm);
            //universal = itemView.findViewById(R.id.universal);
            time5pm = itemView.findViewById(R.id.time5pm);
            time11pm = itemView.findViewById(R.id.time11pm);
            imageView = itemView.findViewById(R.id.dailyicon);
        }

    }
}
