package com.example.task_eternals_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.task_eternals_android.Adapter.TaskAdapter;
import com.example.task_eternals_android.Adapter.ToDoAdapter;
import com.example.task_eternals_android.Model.TaskModel;
import com.example.task_eternals_android.Utilities.DBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements OnDialogCloseListener {
    private RecyclerView mRecyclerView;
    private Button addTask;
    private DBHelper myDB;
    private List<TaskModel> mList;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        addTask = findViewById(R.id.addTaskBtn);
        mRecyclerView = findViewById(R.id.tasksRecyclerView);
        myDB = new DBHelper(TaskActivity.this);
        mList = new ArrayList<>();

        adapter = new TaskAdapter(myDB,TaskActivity.this);
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTask(mList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        myDB.getAllTasks();
        mList = myDB.getAllTasks();
        System.out.println(mList.size());
        Collections.reverse(mList);
        adapter.setTask(mList);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager() , AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewHelperTask(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTask(mList);
        adapter.notifyDataSetChanged();

    }
}