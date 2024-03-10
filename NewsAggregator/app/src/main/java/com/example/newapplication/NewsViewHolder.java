package com.example.newapplication;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.*;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    TextView artHead;
    TextView artDate;
    TextView artPgNo;
    TextView artDes;
    ImageView artImg;
    TextView artAuth;

    //assigning the data from the sources to the text views in the front end
    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        artDes = itemView.findViewById(R.id.description);
        artPgNo = itemView.findViewById(R.id.page_numbers);
        artImg = itemView.findViewById(R.id.news_image);
        artAuth = itemView.findViewById(R.id.author);
        artDate = itemView.findViewById(R.id.date_time);
        artHead = itemView.findViewById(R.id.headline);

        this.artDes.setMovementMethod(new ScrollingMovementMethod());
    }
}
