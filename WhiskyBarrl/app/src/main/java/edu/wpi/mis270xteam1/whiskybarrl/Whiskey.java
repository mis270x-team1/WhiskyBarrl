package edu.wpi.mis270xteam1.whiskybarrl;

/**
 * Created by Anthony J. Ruffa on 2/5/2016.
 */
public class Whiskey {
    private int id;
    private String name;
    private String description;
    private float rating;
    private int proofLevel;
    private int age;
    private String location;

    public Whiskey() {}

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
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
