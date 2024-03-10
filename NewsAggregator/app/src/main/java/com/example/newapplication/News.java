package com.example.newapplication;

import java.util.*;

public class News {
    private String strStat;
    private List<Source> sources;

    //assigning the status and sources info in the news constructor
    public News(String strStat, List<Source> scrs)
    {
        this.sources = scrs;
        this.strStat = strStat;
    }
    public List<Source> getSources()
    {
        return sources;
    }
}
