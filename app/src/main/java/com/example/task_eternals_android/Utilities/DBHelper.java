package com.example.task_eternals_android.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.task_eternals_android.Model.CategoryModel;
import com.example.task_eternals_android.Model.ImageModel;
import com.example.task_eternals_android.Model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

        private SQLiteDatabase db;

        private static final String DB_NAME = "TASK_DATABASE";

        private static final String TABLE_CATEGORIES = "CATEGORIES";
        private static final String CAT_NAME = "NAME";
        private static final String CAT_STATUS = "STATUS";
        private static final String CAT_ID = "ID";

        private static final String TASK_CAT_ID = "CAT_TASK_ID";
        private static final String TABLE_TASKS = "TASKS";
        private static final String TASK_NAME = "NAME";
        private static final String TASK_STATUS = "STATUS";
        private static final String TASK_DUEDATE = "DUE_DATE";
        private static final String TASK_DUETIME = "DUE_TIME";
        private static final String TASK_DESC = "DESCRIPTION";
        private static final String TASK_CATEGORY = "CATEGORY_NAME";
        private static final String TASK_AUDIO = "AUDIO";
        private static final String TASK_ID = "ID";

        private static final String TABLE_IMAGES = "IMAGES";
        private static final String IMAGE_ID = "ID";
        private static final String IMAGE_URI = "URI";
        private static final String IMG_TASK_NAME = "TASK_ID";

    public DBHelper(@Nullable Context context) {
            super(context, DB_NAME, null, 5);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "(" + CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CAT_NAME + " TEXT," + CAT_STATUS + " INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + "(" + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TASK_NAME + " TEXT," + TASK_STATUS + " TEXT," + TASK_DESC + " TEXT," + TASK_CATEGORY + " TEXT," + TASK_DUEDATE + " TEXT,"+ TASK_DUETIME + " TEXT," + TASK_AUDIO + " BLOB," + TASK_CAT_ID + " INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_IMAGES + "(" + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + IMG_TASK_NAME + " TEXT," + IMAGE_URI + " TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);

            onCreate(db);
        }

        public void insertCategory(CategoryModel category){
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CAT_NAME, category.getCategoryName());
            values.put(CAT_STATUS, category.getStatus());
            db.insert(TABLE_CATEGORIES,null, values);
        }

        public void insertTask(TaskModel task){
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TASK_NAME, task.getTitle());
            values.put(TASK_STATUS, task.getStatus());
            values.put(TASK_DESC, task.getDescription());
            values.put(TASK_CATEGORY, task.getCategory());
            values.put(TASK_DUEDATE, task.getDate());
            values.put(TASK_DUETIME, task.getTime());
            values.put(TASK_CAT_ID, task.getCat_task_id());
            db.insert(TABLE_TASKS,null, values);
        }

        public void deleteCategory(int id){
            db = this.getWritableDatabase();
            db.delete(TABLE_CATEGORIES,"ID=?", new String[]{String.valueOf(id)});
        }

        public void deleteTask(int id){
            db = this.getWritableDatabase();
            db.delete(TABLE_TASKS, "ID=?", new String[]{String.valueOf(id)});
        }

        public void updateCategory(int id, String catName){
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CAT_NAME, catName);
            db.update(TABLE_CATEGORIES , values , "ID=?" , new String[]{String.valueOf(id)});
        }
        public void updateTask(int id, String taskTitle, String taskDescription, String dueDate){
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TASK_NAME, taskTitle);
            values.put(TASK_DESC, taskDescription);
            values.put(TASK_DUEDATE, dueDate);
            db.update(TABLE_TASKS, values , "ID=?" , new String[]{String.valueOf(id)});
        }

    public void insertImage(ImageModel image){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE_URI, image.getImageFilePath());
        values.put(IMG_TASK_NAME, image.getCategoryTaskID());
        db.insert(TABLE_IMAGES,null, values);
    }

        public void updateStatus(int id, int status){
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CAT_STATUS, status);
            db.update(TABLE_CATEGORIES , values , "ID=?" , new String[]{String.valueOf(id)});
        }

        public void updateTaskStatus(int id, int status){
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TASK_STATUS, status);
            db.update(TABLE_TASKS , values , "ID=?" , new String[]{String.valueOf(id)});
        }


        public List<CategoryModel> getAllCategories() {
            db = this.getWritableDatabase();
            Cursor cursor = null;
            List<CategoryModel> categories = new ArrayList<>();

            db.beginTransaction();
            try {
                cursor = db.query(TABLE_CATEGORIES, null, null, null, null, null, null);
                if (cursor != null){
                    if (cursor.moveToFirst()){
                        do {
                            CategoryModel category = new CategoryModel();
                            category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CAT_ID)));
                            category.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(CAT_NAME)));
                            category.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(CAT_STATUS)));
                            categories.add(category);
                        }while (cursor.moveToNext());
                    }
                }
            } finally {
                db.endTransaction();
                cursor.close();
            }
            return categories;
        }

        public List<TaskModel> getAllTasks(int categoryId){
            db = this.getWritableDatabase();
            Cursor cursor = null;
            String selection = TASK_CAT_ID + "=" + "?";
            String[] selectionArgs = {String.valueOf(categoryId)};
            List<TaskModel> tasks = new ArrayList<>();
            db.beginTransaction();
            try {
                cursor = db.query(TABLE_TASKS, null, selection, selectionArgs,null,null,null);
                if (cursor != null){
                    if (cursor.moveToFirst()){
                        do {
                            TaskModel task = new TaskModel();
                            task.setIdTask(cursor.getInt(cursor.getColumnIndexOrThrow(TASK_ID)));
                            task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TASK_NAME)));
                            task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TASK_DESC)));
                            task.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(TASK_CATEGORY)));
                            task.setDate(cursor.getString(cursor.getColumnIndexOrThrow(TASK_DUEDATE)));
                            task.setTime(cursor.getString(cursor.getColumnIndexOrThrow(TASK_DUETIME)));
                            task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(TASK_STATUS)));
                            task.setCat_task_id(cursor.getInt(cursor.getColumnIndexOrThrow(TASK_CAT_ID)));
                            tasks.add(task);
                        }while (cursor.moveToNext());
                    }
                }
            } finally {
                db.endTransaction();
                cursor.close();
            }
            return tasks;
        }
    public boolean getCategoryCompleteTasks(String categoryName){
        boolean hasInCompleteTask = false;
        db = this.getWritableDatabase();
        Cursor cursor = null;
        String selection = TASK_CATEGORY + "=" + "?" + " AND " + TASK_STATUS + "<>" + "?";
        String[] selectionArgs = {categoryName,"1"};
        db.beginTransaction();
        try{
            cursor = db.query(TABLE_TASKS, null, selection, selectionArgs,null,null,null);
            if (cursor != null){
                System.out.println(cursor.getCount() +"in db helper");
                hasInCompleteTask = true;
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return hasInCompleteTask;

    }

    public List<ImageModel> getAllImages() {
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ImageModel> images = new ArrayList<>();
        db.beginTransaction();
        try {
            cursor = db.query(TABLE_IMAGES, null, null, null, null, null, null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        ImageModel image = new ImageModel();
                        image.setId(cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE_ID)));
                        image.setImageFilePath(cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_URI)));
                        image.setCategoryTaskID(cursor.getString(cursor.getColumnIndexOrThrow(IMG_TASK_NAME)));
                        images.add(image);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }

        return  images;
    }
}
