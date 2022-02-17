package com.example.task_eternals_android;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_eternals_android.Adapter.ToDoAdapter;
import com.example.task_eternals_android.Model.CategoryModel;
import com.example.task_eternals_android.Utilities.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener{

    private RecyclerView mRecyclerView;
    private FloatingActionButton addCategory;
    private DBHelper db;
    private List<CategoryModel> categoriesList;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview);
        addCategory = findViewById(R.id.addcategory);
        db = new DBHelper(MainActivity.this);
        categoriesList = new ArrayList<>();
        adapter = new ToDoAdapter(db,MainActivity.this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

//        db.getAllCategories();
//        categoriesList = db.getAllCategories();
//        for(int i = 0; i<categoriesList.size();i++){
//            categoriesList.get(i).getCategoryName();
//            if(!db.getCategoryCompleteTasks(categoriesList.get(i).getCategoryName())){
//                categoriesList.get(i).setStatus(1);
//                //db.updateStatus(categoriesList.get(i).getId(),1);
//                db.updateCategory(categoriesList.get(i).getId(),"gggggg");
//                System.out.println(categoriesList.get(i).getId()+"Lllllll");
//                adapter.notifyDataSetChanged();
//            }
//        }
        categoriesList = db.getAllCategories();
        Collections.reverse(categoriesList);
        adapter.setCategories(categoriesList);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewCategory.newInstance().show(getSupportFragmentManager(), AddNewCategory.TAG);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        categoriesList = db.getAllCategories();
        Collections.reverse(categoriesList);
        adapter.setCategories(categoriesList);
        adapter.notifyDataSetChanged();
    }
}