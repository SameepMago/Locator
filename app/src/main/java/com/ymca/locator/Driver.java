package com.ymca.locator;


public class Driver {
    private String name,contactno,experience,busnum;
    private int image;

    public Driver() {
    }

    public Driver(String name, String contactno, String experience, String busnum, int image) {
        this.name = name;
        this.contactno = contactno;
        this.image = image;
        this.experience=experience;
        this.busnum=busnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getExperience(){return experience;}

    public void setExperience(String experience){this.experience=experience;}

    public String getBusnum(){return busnum;}

    public void setBusnum(String busnum){this.busnum=busnum;}
}
