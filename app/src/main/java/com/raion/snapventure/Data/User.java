package com.raion.snapventure.Data;

public class User {

    String email, name;
    int energy, point;

    public User(String email, String name, int energy, int point) {
        this.email = email;
        this.name = name;
        this.energy = energy;
        this.point = point;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
