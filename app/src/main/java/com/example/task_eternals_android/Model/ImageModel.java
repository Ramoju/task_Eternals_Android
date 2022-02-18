package com.example.task_eternals_android.Model;

public class ImageModel {
    private int id;
    private String CategoryTaskID;
    private String imageFilePath;

    public int getId() {
        return id;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryTaskID() {
        return CategoryTaskID;
    }

    public void setCategoryTaskID(String categoryTaskID) {
        CategoryTaskID = categoryTaskID;
    }
}
