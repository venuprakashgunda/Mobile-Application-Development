package com.example.newapplication;

public class Article {
    private String strURL,strAuth, strDt;

    private String strHd, strIURL ,strDes ;

    // used to get the article description
    public String getArtDes()
    {
        return strDes;
    }

    //used to get the article image URL
    public String getArtIURL()
    {
        return strIURL;
    }

    // used to get the article URL
    public String getArtUrl()
    {
        return strURL;
    }

    //used to get the article author name
    public String getArtAuth()
    {
        return strAuth;
    }

    //used to get the article date
    public String getArtDt()
    {
        return strDt;
    }

    //used to get the article headline
    public String getArtHead()
    {
        return strHd;
    }

    //used to set the article description
    public void setArtDes(String description) {
        this.strDes = description;
    }

    public void setArtAuth(String strAuth)
    {
        this.strAuth = strAuth;
    }

    //used to set the article date
    public void setArtDt(String strDt)
    {
        this.strDt = strDt;
    }

    //used to the set the article image URL
    public void setArtIURL(String urlToImage)
    {
        this.strIURL = urlToImage;
    }

    //used to the set the article URL
    public void setArtUrl(String strArtURL)
    {
        this.strURL = strArtURL;
    }

    //used to set the article headline
    public void setArtHead(String strHd)
    {
        this.strHd = strHd;
    }





















}
