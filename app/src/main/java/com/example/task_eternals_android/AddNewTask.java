package com.example.task_eternals_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.task_eternals_android.Model.CategoryModel;
import com.example.task_eternals_android.Model.TaskModel;
import com.example.task_eternals_android.Utilities.DBHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";

    private DatePickerDialog datePickerDialog;
    private TextView title, description, tvTimer, tvDates;
    int tHour, tMinute;
    private Button mSave;
    DatePickerDialog.OnDateSetListener setListener;
    private DBHelper myDB;
    String categoryName;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add_new_task, container, false);
        categoryName = getArguments().getString("category-name");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.addTitle);
        description = view.findViewById(R.id.addDescription);
        tvTimer = view.findViewById(R.id.tvDate);
        tvDates = view.findViewById(R.id.tvTime);
        mSave = view.findViewById(R.id.saveTask);

        myDB = new DBHelper(getActivity());

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle  != null){
            if (bundle.getBoolean("edit-task")){
                isUpdate = true;
                title.setText(bundle.getString("task"));
                description.setText(bundle.getString("task-description"));
            }
        }

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    mSave.setEnabled(false);
                    mSave.setBackgroundColor(Color.GRAY);

                }else{
                    mSave.setEnabled(true);
                    mSave.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = title.getText().toString();
                String text1 = description.getText().toString();

                if (finalIsUpdate){
                    myDB.updateTask(bundle.getInt("id"), text, text1, null, null, text);
                }else {
                    TaskModel item = new TaskModel();
                    item.setTitle(text);
                    item.setDescription(text1);
                    item.setStatus(0);
                    item.setCategory(categoryName);
                    myDB.insertTask(item);
                }
                dismiss();
            }
        });
        //dateTime();

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_new_task);
//        tvTimer = findViewById(R.id.tvTime);
//        tvDates = findViewById(R.id.tvDate);

//        // ******** DOUBT ********
////        myDB = new DBHelper(this);
////        Boolean isUpdate = false;


//    }

//    private void dateTime() {
//        Calendar calendar = Calendar.getInstance();
//        final int year = calendar.get(Calendar.YEAR);
//        final int month = calendar.get(Calendar.MONTH);
//        final int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//
//        tvDates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        AddNewTask.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
//                        ,setListener,year,month,day);
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                datePickerDialog.show();
//            }
//        });
//        setListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//                month = month+1;
//                String date = dayOfMonth+"/"+month+"/"+year;
//                tvDates.setText(date);
//            }
//        };
//        tvTimer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TimePickerDialog timePickerDialog = new TimePickerDialog(
//                        AddNewTask.this,
//                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                        new TimePickerDialog.OnTimeSetListener(){
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                tHour = hourOfDay;
//                                tMinute = minute;
//                                String time = tHour +":"+ tMinute;
//                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
//                                try {
//                                    Date date = f24Hours.parse(time);
//                                    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
//                                    tvTimer.setText(f12Hours.format(date));
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },12,0,false
//                );
//                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                timePickerDialog.updateTime(tHour,tMinute);
//                timePickerDialog.show();
//            }
//        });
//        }
}