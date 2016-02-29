package edu.wpi.mis270xteam1.whiskybarrl;

/**
 * Created by Anthony J. Ruffa on 2/5/2016.
 */
public class Whiskey {
    private int id;
    private String name;
    private String description;
    private String imgPath;
    private float rating;
    private int proofLevel;
    private int age;
    private String location;
    private int whiskeyUserId;
    private String whiskeyUsername;

    public Whiskey() {}

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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
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

    public int getWhiskeyUserId() {
        return whiskeyUserId;
    }

    public void setWhiskeyUserId(int whiskeyUserId) {
        this.whiskeyUserId = whiskeyUserId;
    }

    public String getWhiskeyUsername() {
        return whiskeyUsername;
    }

    public void setWhiskeyUsername(String whiskeyUsername) {
        this.whiskeyUsername = whiskeyUsername;
    }
}
