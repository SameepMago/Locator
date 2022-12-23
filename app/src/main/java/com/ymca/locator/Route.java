package com.ymca.locator;


public class Route {
    private String route,busnum;
    private int image;

    public Route() {
    }

    public Route(String busnum, String route, int image) {
        this.image = image;
        this.route=route;
        this.busnum=busnum;
    }

    public String getBusNum() {
        return busnum;
    }

    public void setBusNum(String name) {
        this.busnum = busnum;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getRoute(){return route;}

    public void setRoute(String route){this.route=route;}
}
