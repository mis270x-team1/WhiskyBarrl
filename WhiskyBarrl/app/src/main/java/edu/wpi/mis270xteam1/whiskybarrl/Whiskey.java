package edu.wpi.mis270xteam1.whiskybarrl;

import java.lang.ref.SoftReference;

/**
 * Created by Anthony J. Ruffa on 2/5/2016.
 */
public class Whiskey {
    private String name;
    private String description;
    private int rating;
    private int proofLevel;
    private int age;
    private String location;


    public Whiskey() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getProofLevel() {
        return proofLevel;
    }

    public void setProofLevel(int proofLevel) {
        this.proofLevel = proofLevel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
