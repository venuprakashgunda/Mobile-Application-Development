package com.example.newapplication;

public class Source {
    private String articleId, articleName, articleCat;

    public Source(String sourceId, String sourceName, String sourceCat) {
        this.articleId = sourceId;
        this.articleName = sourceName;
        this.articleCat = sourceCat;
    }

    //used to get the article ID
    public String getArticleId() {
        return articleId;
    }

    //used to get the article name
    public String getArticleName() {
        return articleName;
    }

    //used to set the article name
    public void setArticleName(String artName) {
        this.articleName = artName;
    }

    //used to get the article category
    public String getArticleCat() {
        return articleCat;
    }
}
