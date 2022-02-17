package com.example.task_eternals_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
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
import java.util.Locale;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";

    private DatePickerDialog datePickerDialog;
    private TextView title, description, tvTimer, tvDates;
    int tHour, tMinute;
    private Button mSave;
    DatePickerDialog.OnDateSetListener setListener;
    private DBHelper myDB;
    String categoryName, date;
    TextView dateBtn;

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
        //ss
        initDatePicker();
        title = view.findViewById(R.id.addTitle);
        description = view.findViewById(R.id.addDescription);
        dateBtn = view.findViewById(R.id.btn_date);
        dateBtn.setText(getTodayDate());
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
                String text2 = dateBtn.getText().toString();

                if (finalIsUpdate){
                    myDB.updateTask(bundle.getInt("id"), text, text1, null, null, text);
                }else {
                    TaskModel item = new TaskModel();
                    item.setTitle(text);
                    item.setDescription(text1);
                    item.setStatus(0);
                    item.setCategory(categoryName);
                    item.setDate(text2);
                    myDB.insertTask(item);
                }
                dismiss();
            }
        });
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance(Locale.CANADA);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    //
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date = makeDateString(day, month, year);
                dateBtn.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance(Locale.CANADA);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(),style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + ", " + year;
    }
    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        return "JAN";
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}