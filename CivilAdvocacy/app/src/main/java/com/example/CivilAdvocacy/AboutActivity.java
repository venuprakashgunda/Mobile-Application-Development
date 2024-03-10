package com.example.CivilAdvocacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    String apiLink = "https://developers.google.com/civic-information/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("Civil Advocacy");
    }

    public void apiClicked(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(apiLink));
        if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
    }
}