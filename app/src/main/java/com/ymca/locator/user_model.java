package com.ymca.locator;

public class user_model {
    public String username;
    public String email;
    public String password;
    public String busnum;
    public String busstop;
    public user_model() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public user_model(String username, String email, String password,String busnum,String busstop) {
        this.username = username;
        this.email = email;
        this.password=password;
        this.busnum=busnum;
        this.busstop=busstop;
    }

}
