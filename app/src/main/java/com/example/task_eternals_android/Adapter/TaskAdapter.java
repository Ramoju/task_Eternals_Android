package com.example.task_eternals_android.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_eternals_android.AddNewTask;
import com.example.task_eternals_android.MainActivity;
import com.example.task_eternals_android.Model.TaskModel;
import com.example.task_eternals_android.R;
import com.example.task_eternals_android.TaskActivity;
import com.example.task_eternals_android.Utilities.DBHelper;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskModel> mList;
    private TaskActivity activity;
    private DBHelper myDB;

    public  TaskAdapter(DBHelper myDB, TaskActivity activity){
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card , parent , false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        final TaskModel item = mList.get(position);
        holder.mCheckBox.setText(item.getTitle());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    myDB.updateTaskStatus(item.getIdTask(),1);
                }else {
                    myDB.updateTaskStatus(item.getIdTask(),0);
                }
            }
        });
    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTask(List<TaskModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        TaskModel item = mList.get(position);
        myDB.deleteTask(item.getIdTask());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        TaskModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getIdTask());
        bundle.putString("task" , item.getTitle());
        bundle.putString("date" , item.getDate());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
