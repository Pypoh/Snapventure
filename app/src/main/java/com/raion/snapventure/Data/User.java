package com.raion.snapventure.Data;

public class User {

    String email, name;
    int energy, point, stages_unlocked;

    public User(String email, String name, int energy, int point, int stages_unlocked) {
        this.email = email;
        this.name = name;
        this.energy = energy;
        this.point = point;
        this.stages_unlocked = stages_unlocked;
    }

    public int getStages_unlocked() {
        return stages_unlocked;
    }

    public void setStages_unlocked(int stages_unlocked) {
        this.stages_unlocked = stages_unlocked;
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
