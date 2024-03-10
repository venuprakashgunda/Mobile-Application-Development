package com.example.CivilAdvocacy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private final List<Official> officialList;
    private final MainActivity mainAct;

    OfficialAdapter(List<Official> offList, MainActivity ma) {
        this.officialList = offList;
        this.mainAct = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_entry, parent, false);

        itemView.setOnClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Official official = officialList.get(position);
        if (official.getParty() == null)
            holder.name.setText(official.getName());
        else
            holder.name.setText(official.getName()+'('+official.getParty()+')');

        holder.officeName.setText(official.getOfficeName());
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
