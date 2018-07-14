package com.android.myshopapp.model;

public class ProductsData {

    long dbid = 0;
    int id = 0, userid = 0;
    String title = "";
    boolean completed = false;

    public long getDbid() {
        return dbid;
    }

    public void setDbid(long dbid) {
        this.dbid = dbid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public ProductsData( int id, int userid, String title, boolean completed) {

        this.id = id;
        this.userid = userid;
        this.title = title;
        this.completed = completed;
    }
}
