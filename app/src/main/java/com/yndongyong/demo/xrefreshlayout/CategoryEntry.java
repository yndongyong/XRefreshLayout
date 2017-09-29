package com.yndongyong.demo.xrefreshlayout;


/**
 * Created by dongzhiyong on 2017/5/29.
 */

public class CategoryEntry {

    private String url;
    private String description;
    private int type = 1;

    public CategoryEntry(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public CategoryEntry(String url, String description, int type) {
        this.url = url;
        this.description = description;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }
}
