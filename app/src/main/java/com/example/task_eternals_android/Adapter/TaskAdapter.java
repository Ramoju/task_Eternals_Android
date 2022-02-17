package com.example.task_eternals_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_eternals_android.AddNewTask;
import com.example.task_eternals_android.MainActivity;
import com.example.task_eternals_android.Model.TaskModel;
import com.example.task_eternals_android.R;
import com.example.task_eternals_android.TaskActivity;
import com.example.task_eternals_android.TaskDetailsActivity;
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
        TaskModel item = mList.get(holder.getAdapterPosition());
        System.out.println(position + "adapter@@@@@@@@@@@@@@@");
        holder.taskname.setText(item.getTitle());
        holder.duedate.setText(item.getDate() + " | " + item.getDescription());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.taskname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TaskDetailsActivity.class);
                intent.putExtra("task", item);
                getContext().startActivity(intent);
            }
        });
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    myDB.updateTaskStatus(item.getIdTask(),1);
                    System.out.println(item.getIdTask() + "@@@@@@@@@@@@@@@");
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
        System.out.println(item.getIdTask()+"@@@@");
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        TaskModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getIdTask());
        bundle.putString("task" , item.getTitle());
        bundle.putString("task-description", item.getDescription());
        bundle.putString("date" , item.getDate());
        bundle.putBoolean("edit-task", true);

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
        TextView taskname;
        TextView duedate;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.taskcheckbox);
            taskname = itemView.findViewById(R.id.tasknamelabel);
            duedate = itemView.findViewById(R.id.dueDate);
        }
    }
    public void filterList(List<TaskModel> filteredList){
        mList = filteredList;
        notifyDataSetChanged();
    }
}
