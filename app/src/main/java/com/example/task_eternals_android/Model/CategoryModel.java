package com.example.task_eternals_android.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryModel implements Parcelable {
    private String categoryName;
    private int status;
    private int id;

    public CategoryModel(String categoryName, int status, int id) {
        this.categoryName = categoryName;
        this.status = status;
        this.id = id;
    }



    protected CategoryModel(Parcel in) {
        categoryName = in.readString();
        status = in.readInt();
        id = in.readInt();
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    public CategoryModel() {

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categoryName);
        parcel.writeInt(status);
        parcel.writeInt(id);
    }
}
