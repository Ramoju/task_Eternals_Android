package com.example.task_eternals_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.task_eternals_android.Adapter.TaskAdapter;
import com.example.task_eternals_android.Model.CategoryModel;
import com.example.task_eternals_android.Model.TaskModel;
import com.example.task_eternals_android.Utilities.DBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements OnDialogCloseListener, AdapterView.OnItemSelectedListener {
    private RecyclerView mRecyclerView;
    private Button addTask;
    private DBHelper myDB;
    private List<TaskModel> mList;
    private TaskAdapter adapter;
    SearchView searchView;
    CategoryModel category;
    private TextView categoryName;
    private static FragmentManager fragmentManager;
    AddNewTask addNewTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        addTask = findViewById(R.id.addTaskBtn);
        searchView = findViewById(R.id.searchBar);
        mRecyclerView = findViewById(R.id.tasksRecyclerView);
        categoryName = findViewById(R.id.txtViewTasks);
        myDB = new DBHelper(TaskActivity.this);
        mList = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();


        //for spinner to sort
        Spinner spinner = findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.sorting, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(this);

        category = getIntent().getParcelableExtra("category");
        categoryName.setText(category.getCategoryName());

        adapter = new TaskAdapter(myDB,TaskActivity.this);
        mList = myDB.getAllTasks(category.getCategoryName());
        Collections.reverse(mList);
        adapter.setTask(mList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        mList = myDB.getAllTasks(category.getCategoryName());
        System.out.println(mList.size());
        Collections.reverse(mList);
        adapter.setTask(mList);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putString("category-name", category.getCategoryName());
                addNewTask = AddNewTask.newInstance();
                addNewTask.setArguments(data);
                System.out.println("In main activity on click of save");
                addNewTask.show(getSupportFragmentManager() , AddNewTask.TAG);
                System.out.println("After showing Add new task fragment");
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewHelperTask(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

    }

    private void filter(String newText) {
        List<TaskModel> filteredList = new ArrayList<>();
        for (TaskModel item : mList){
            if(item.getTitle().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks(category.getCategoryName());
        Collections.reverse(mList);
        adapter.setTask(mList);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

        if(position == 1 ){
            Collections.sort(mList, TaskModel.titleAZComparator);
            adapter.notifyDataSetChanged();
        }
        if(position == 2 ){
            Collections.sort(mList, TaskModel.titleZAComparator);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}