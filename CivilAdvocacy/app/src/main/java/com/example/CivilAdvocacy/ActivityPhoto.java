package com.example.CivilAdvocacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ActivityPhoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setTitle("Civil Advocacy App");

        TextView locationTextView = findViewById(R.id.location_PA);
        TextView officeTextView = findViewById(R.id.office_PA);
        TextView nameTextView = findViewById(R.id.name_PA);
        ImageView photoView = findViewById(R.id.photo_PA);
        ImageView partyView = findViewById(R.id.partyLogo_PA);

        Intent intent = getIntent();
        Official official = (Official) intent.getSerializableExtra("official");
        CharSequence ch = intent.getCharSequenceExtra("location");
        Picasso picasso = Picasso.get();

        if (ch == null)
            Log.d("ch", "null");
        else
            Log.d("ch", ch.toString());
        locationTextView.setText(intent.getCharSequenceExtra("location"));

        officeTextView.setText(official.getOfficeName());
        nameTextView.setText(official.getName());

        if (official.getParty() != null) {

            if (official.getParty().contains("Republican")) {
                getWindow().getDecorView().setBackgroundColor(Color.RED);
                partyView.setImageResource(R.drawable.rep_logo);
            } else if (official.getParty().contains("Democratic")) {
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                partyView.setImageResource(R.drawable.dem_logo);
            } else {
                getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                partyView.setVisibility(View.INVISIBLE);
            }

            if (official.getPhotoUrl() != null) {
                picasso.load(official.getPhotoUrl().replace("http:", "https:")).error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder).into(photoView);
            } else {
                picasso.load(official.getPhotoUrl()).error(R.drawable.brokenimage)
                        .placeholder(R.drawable.missing).into(photoView);
            }
        }
    }
}