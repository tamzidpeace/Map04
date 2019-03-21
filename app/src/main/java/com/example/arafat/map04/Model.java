package com.example.arafat.map04;

public class Model {

    String dist, country;

    public Model(String dist, String country) {
        this.dist = dist;
        this.country = country;
    }



    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
