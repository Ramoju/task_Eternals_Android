package com.example.task_eternals_android.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class TaskModel implements Parcelable {
    private String title, description, date, time, category;
    private int idTask, status,cat_task_id;

    public int getCat_task_id() {
        return cat_task_id;
    }

    public void setCat_task_id(int cat_task_id) {
        this.cat_task_id = cat_task_id;
    }

    protected TaskModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        date = in.readString();
        time = in.readString();
        category = in.readString();
        idTask = in.readInt();
        status = in.readInt();
        cat_task_id = in.readInt();
    }

    public TaskModel(String title, String description, String date, String time, int idTask, int status, String category) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.idTask = idTask;
        this.status = status;
        this.category = category;
    }

    public static final Creator<TaskModel> CREATOR = new Creator<TaskModel>() {
        @Override
        public TaskModel createFromParcel(Parcel in) {
            return new TaskModel(in);
        }

        @Override
        public TaskModel[] newArray(int size) {
            return new TaskModel[size];
        }
    };

    public TaskModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(category);
        parcel.writeInt(idTask);
        parcel.writeInt(status);
        parcel.writeInt(cat_task_id);
    }

    public static Comparator<TaskModel> DateAscendingComparator = new Comparator<TaskModel>() {
        @Override
        public int compare(TaskModel t1, TaskModel t2) {
            return t1.getDate().compareTo(t2.getDate());
        }
    };


    public static Comparator<TaskModel> DateDescendingComparator = new Comparator<TaskModel>() {
        @Override
        public int compare(TaskModel t1, TaskModel t2) {
            return t2.getDate().compareTo(t1.getDate());
        }
    };

    public static Comparator<TaskModel> titleAZComparator = new Comparator<TaskModel>() {
        @Override
        public int compare(TaskModel t1, TaskModel t2) {
            return t1.getTitle().compareTo(t2.getTitle());
        }
    };

    public static Comparator<TaskModel> titleZAComparator = new Comparator<TaskModel>() {
        @Override
        public int compare(TaskModel t1, TaskModel t2) {
            return t2.getTitle().compareTo(t1.getTitle());
        }
    };

}
