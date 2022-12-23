package com.ymca.locator;



public class Notify {
    private String name,stamp;
    public Notify() {
    }

    public Notify(String name,String time) {
        this.name = name;
        this.stamp=time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

}
