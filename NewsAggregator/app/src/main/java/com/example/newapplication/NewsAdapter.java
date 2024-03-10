package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.time.*;
import android.view.*;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private final MainActivity mainActivity;
    private final List<Article> list;

    public NewsAdapter(MainActivity mainActivity, List<Article> articleDetailsList) {
        this.mainActivity = mainActivity;
        this.list = articleDetailsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_display, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article sourceArt = list.get(position);

        String chHead = sourceArt.getArtHead();
        while (chHead != null) {
            String artHead = sourceArt.getArtHead();
            holder.artHead.setText(artHead);

//            if (url != null)
//            {
//                Intent itnt = new Intent(Intent.ACTION_VIEW);
//                itnt.setData(Uri.parse(url));
//                if (intent.resolveActivity(mainActivity.getPackageManager()) != null)
//                {
//                    mainActivity.startActivity(intent);
//                }
//             }

            holder.artHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String thisURL = sourceArt.getArtUrl();
                    intent.setData(Uri.parse(thisURL));
                    mainActivity.startActivity(intent);
                }
            });
            break;
        }

        boolean bol = sourceArt.getArtAuth().isEmpty();
        String nullString = "null";
        if(bol || sourceArt.getArtAuth().equals(nullString)) {
            holder.artAuth.setVisibility(View.GONE);
        }
        else {
            String authN = sourceArt.getArtAuth();
            holder.artAuth.setText(authN);
        }
//        if (content != null)
//            textView.setText(content);
//            textView.setVisibility(View.VISIBLE);

        String artBol = sourceArt.getArtDt();
        if(artBol != null) {
            String date = getArticleDate(sourceArt.getArtDt());
            holder.artDate.setText(date);
        }

        String artURL = sourceArt.getArtUrl();
        if (artURL == null) {
            holder.artImg.setImageResource(R.drawable.noimage);
        }
        else {
            Picasso picasso = Picasso.with(mainActivity);
            picasso.setLoggingEnabled(true);
            picasso.load(sourceArt.getArtIURL())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.brokenimage)
                    .into(holder.artImg);

//            if (article.getUrlToImage() != null)
//                Picasso.get().load(article.getUrlToImage()).placeholder(R.drawable.loading)
//                        .error(R.drawable.brokenimage).into(holder.image);

            holder.artImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(sourceArt.getArtUrl()));
                    mainActivity.startActivity(intent);
                }
            });
        }

        boolean bolDes = sourceArt.getArtAuth().isEmpty();
        String artDes = sourceArt.getArtDes();
        if(bolDes || artDes.equals("null")){
            holder.artDes.setVisibility(View.GONE);
        }
//        if (article.getPublishedAt() != null)
//            holder.date.setText(NewsDownloader.formatDateTime(article.getPublishedAt()));
//            holder.date.setVisibility(View.VISIBLE);
        else {
            holder.artDes.setText(artDes);
            holder.artDes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(sourceArt.getArtUrl()));
                    mainActivity.startActivity(intent);
                }
            });
        }
        int  pgSt = position + 1;
        int pgLt = list.size();
        String pageNumbers = String.format("%d of %d", pgSt, pgLt);
        holder.artPgNo.setText(pageNumbers);
    }

    @Override
    public int getItemCount() {
        int length = list.size();
        return length;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getArticleDate(String date) {
        String APIDATE = "";
        try{
            DateTimeFormatter timeFormat = DateTimeFormatter.ISO_INSTANT;
            TemporalAccessor tempAcs = timeFormat.parse(date);
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
            LocalDateTime locDatTim = LocalDateTime.ofInstant(Instant.from(tempAcs), ZoneId.systemDefault());
            APIDATE = locDatTim.format(dateTimeFormat);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        boolean check = APIDATE.isEmpty();
        if(check){
            try{
                DateTimeFormatter timeFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                TemporalAccessor tempAcs = timeFormat.parse(date);
                DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
                LocalDateTime locDatTim = LocalDateTime.ofInstant(Instant.from(tempAcs), ZoneId.systemDefault());
                APIDATE = locDatTim.format(dateTimeFormat);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        String dtFormat = APIDATE;
        return dtFormat;
    }

}
