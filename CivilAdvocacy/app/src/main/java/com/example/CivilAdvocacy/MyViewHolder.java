package com.example.CivilAdvocacy;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{

    TextView name;
    TextView officeName;

    public MyViewHolder(View itemView) {
        super(itemView);
        officeName = itemView.findViewById(R.id.textView_office);
        name = itemView.findViewById(R.id.textView_nameParty);
    }
}
