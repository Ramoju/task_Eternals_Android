package com.example.task_eternals_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_eternals_android.AddNewCategory;
import com.example.task_eternals_android.MainActivity;
import com.example.task_eternals_android.Model.CategoryModel;
import com.example.task_eternals_android.R;
import com.example.task_eternals_android.TaskActivity;
import com.example.task_eternals_android.Utilities.DBHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder>{

private List<CategoryModel> categories;
private MainActivity activity;
private DBHelper mDb;

public ToDoAdapter(DBHelper db, MainActivity activity){
        this.activity = activity;
        this.mDb = db;
        }

@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        return new MyViewHolder(v);
        }

@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final CategoryModel item = categories.get(position);
        holder.categoryName.setText(item.getCategoryName());
        holder.categoryStatus.setChecked(toBoolean(item.getStatus()));
        holder.categoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent = new Intent(activity, TaskActivity.class);
                        getContext().startActivity(intent);
                }
        });
        }

public boolean toBoolean(int num){
        return num!=0;
        }

@Override
public int getItemCount() {
        return categories.size();
        }

public Context getContext(){
        return activity;
        }

public void setCategories(List<CategoryModel> mList){
        this.categories = mList;
        notifyDataSetChanged();
        }

public void deleteCategory(int position) {
        CategoryModel cat = categories.get(position);
        mDb.deleteCategory(cat.getId());
        categories.remove(position);
        notifyItemRemoved(position);
        }

public void editCategory(int position){
        CategoryModel cat = categories.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", cat.getId());
        bundle.putString("categoryname", cat.getCategoryName());

        AddNewCategory category = new AddNewCategory();
        category.setArguments(bundle);
        category.show(activity.getSupportFragmentManager(), category.getTag());
        }

public static class MyViewHolder extends RecyclerView.ViewHolder{

    CheckBox categoryStatus;
    TextView categoryName;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryName = itemView.findViewById(R.id.categoryname);
        categoryStatus = itemView.findViewById(R.id.mcheckbox);
    }
}
}
