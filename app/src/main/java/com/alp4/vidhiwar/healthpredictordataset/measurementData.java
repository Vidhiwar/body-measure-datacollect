package com.alp4.vidhiwar.healthpredictordataset;

public class measurementData {

    String dataID;
    int height;
    int shoulders;
    int waist;
    int hip;
    int arms;
    int wrist;
    int thigh;
    int age;
    int weight;
    String sex;
    String bodyType;
    String imagePath;
    String imageUrl;

    public measurementData() {
    }

    public measurementData(String imageUrl ,String imagePath, String dataID, int height, int shoulders, int waist, int hip, int arms, int wrist, int thigh, int age, int weight, String sex, String bodyType) {
        this.imageUrl = imageUrl;
        this.imagePath = imagePath;
        this.dataID = dataID;
        this.height = height;
        this.shoulders = shoulders;
        this.waist = waist;
        this.hip = hip;
        this.arms = arms;
        this.wrist = wrist;
        this.thigh = thigh;
        this.age = age;
        this.weight = weight;
        this.sex = sex;
        this.bodyType = bodyType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDataID() {
        return dataID;
    }

    public int getHeight() {
        return height;
    }

    public int getShoulders() {
        return shoulders;
    }

    public int getWaist() {
        return waist;
    }

    public int getHip() {
        return hip;
    }

    public int getArms() {
        return arms;
    }

    public int getWrist() {
        return wrist;
    }

    public int getThigh() {
        return thigh;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public String getSex() {
        return sex;
    }

    public String getBodyType() {
        return bodyType;
    }

    public String getImagePath() {
        return imagePath;
    }
}
