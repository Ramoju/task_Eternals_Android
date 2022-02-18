package com.example.task_eternals_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.task_eternals_android.Model.CategoryModel;
import com.example.task_eternals_android.Model.TaskModel;
import com.example.task_eternals_android.Utilities.DBHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class AddNewCategory extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewCategory";

    private EditText mEditText;
    private Button mSaveButton;

    private DBHelper mDB;
    List<TaskModel> tasks;

    public static AddNewCategory newInstance(){
        return new AddNewCategory();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_new_category, container, false);
        tasks = new ArrayList<>();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.catname);
        mSaveButton = view.findViewById(R.id.button_save);

        mDB = new DBHelper(getActivity());

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String categoryname = bundle.getString("categoryname");
            mEditText.setText(categoryname);

            if (categoryname.length() > 0){
                mSaveButton.setEnabled(false);
            }
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.CYAN);
                }else {
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final boolean finalisUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEditText.getText().toString();
                if (text.isEmpty()) {
                    mEditText.setError("category cannot be empty");
                    mEditText.requestFocus();
                    return;
                }
                if(finalisUpdate){
                    mDB.updateCategory(bundle.getInt("id"), text);
//                    tasks = mDB.getAllTasks(bundle.getInt("id"));
//                    for (int i=0; i<tasks.size(); i++){
//                        mDB.updateTask(tasks.get(i).getIdTask(), tasks.get(i).getTitle(), tasks.get(i).getDescription(), tasks.get(i).getDate());
//                    }
                }else {
                    CategoryModel item = new CategoryModel();
                    item.setCategoryName(text);
                    item.setStatus(0);
                    mDB.insertCategory(item);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();

        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}
