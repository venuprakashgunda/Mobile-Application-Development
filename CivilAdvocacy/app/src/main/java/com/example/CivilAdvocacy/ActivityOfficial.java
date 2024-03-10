package com.example.CivilAdvocacy;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;


public class ActivityOfficial extends AppCompatActivity {

    private TextView locationTextView;

    private Official official;

    private static final String dem = "https://democrats.org";
    private static final String rep = "https://www.gop.com";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        setTitle("Civil Advocacy App");

        locationTextView = findViewById(R.id.locationOfficial);
        TextView officeTextView = findViewById(R.id.officialOffice);
        TextView nameTextView = findViewById(R.id.officialName);
        TextView partyTextView = findViewById(R.id.officialParty);
        TextView addressTextView = findViewById(R.id.officialAddress);
        TextView phoneTextView = findViewById(R.id.officialPhone);
        TextView emailTextView = findViewById(R.id.officialEmail);
        TextView emailText = findViewById(R.id.emailText);
        TextView webTextView = findViewById(R.id.officialWebsite);
        ImageView photoImageView = findViewById(R.id.officialImage);
        ImageView partyLogoImageView = findViewById(R.id.partyLogo);

        ImageView facebookImageView = findViewById(R.id.facebookLogo);
        ImageView twitterImageView = findViewById(R.id.twitterLogo);
        ImageView youtubeImageView = findViewById(R.id.youtubeLogo);


        Intent intent = getIntent();
        locationTextView.setText(intent.getCharSequenceExtra("location"));
        official = (Official) intent.getSerializableExtra("official");
        officeTextView.setText(official.getOfficeName());
        nameTextView.setText(official.getName());

        Picasso picasso = Picasso.get();

        if (official.getParty() != null) {

            if (official.getParty().contains("Republican")){
                getWindow().getDecorView().setBackgroundColor(Color.RED);
                partyTextView.setText('('+official.getParty()+')');
                partyLogoImageView.setImageResource(R.drawable.rep_logo);
            }
            else if (official.getParty().contains("Democratic")) {
                partyTextView.setText('('+official.getParty()+')');
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                partyLogoImageView.setImageResource(R.drawable.dem_logo);
            }
            else {
                getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                partyLogoImageView.setVisibility(View.INVISIBLE);
            }
        } else getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        if (official.getAddress() != null) addressTextView.setText(official.getAddress());
        if (official.getPhone() != null) phoneTextView.setText(official.getPhone());
        if (official.getEmail() != null) {
            emailTextView.setText(official.getEmail());
        }
        else {
            emailText.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
        }
        if (official.getUrl() != null) webTextView.setText(official.getUrl());

        if (official.getPhotoUrl() != null){
            picasso.load(official.getPhotoUrl().replace("http:", "https:")) .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder) .into(photoImageView);
        }else {
            picasso.load(official.getPhotoUrl()) .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missing) .into(photoImageView);
        }

        if (official.getChannel() == null) {
            facebookImageView.setVisibility(View.INVISIBLE);
            youtubeImageView.setVisibility(View.INVISIBLE);
            twitterImageView.setVisibility(View.INVISIBLE);
        }else{
            if (official.getChannel().getFacebookId() == null)
                facebookImageView.setVisibility(View.INVISIBLE);
            if (official.getChannel().getYoutubeId() == null)
                youtubeImageView.setVisibility(View.INVISIBLE);
            if (official.getChannel().getTwitterId() == null)
                twitterImageView.setVisibility(View.INVISIBLE);
        }

        Linkify.addLinks(webTextView, Linkify.ALL);
        Linkify.addLinks(addressTextView, Linkify.MAP_ADDRESSES);
        Linkify.addLinks(emailTextView, Linkify.ALL);
        Linkify.addLinks(phoneTextView, Linkify.ALL);
    }
    public void twitterClicked(View v) {
        Intent intent;
        String name = official.getChannel().getTwitterId();
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + official.getChannel().getFacebookId();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                //older versions of fb app
                urlToUse = "fb://page/" + official.getChannel().getFacebookId();
            }     } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
            //normal web url
            }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void youTubeClicked(View v) {
        String name = official.getChannel().getYoutubeId();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void photoClick(View v){
        if (official.getPhotoUrl() == null){
            return;
        }
        Intent intent = new Intent(this, ActivityPhoto.class);
        intent.putExtra("official", official);
        intent.putExtra("location", locationTextView.getText());
        startActivity(intent);
    }

    public void logoClicked(View v){
        String party = official.getParty();
        String urlToUse = rep;
        if(party.contains("Republican"))
            urlToUse = rep;
        else if (party.contains("Democratic"))
            urlToUse = dem;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlToUse));

        if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
    }
}