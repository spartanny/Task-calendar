package com.example.taskcalendar;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskcalendar.Model.ToDoModel;
import com.example.taskcalendar.Utils.DatabaseHandler;

import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity {

    private DatabaseHandler databaseHandler;
    private Button saveTaskButton;
    private EditText newTaskText;
    private TimePicker timePicker;
    private Calendar mCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        saveTaskButton = findViewById(R.id.saveTaskButton);
        newTaskText = findViewById(R.id.newTaskText);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minutes) {
                mCalendar.set(Calendar.HOUR_OF_DAY,hour);
                mCalendar.set(Calendar.MINUTE,minutes);
                mCalendar.set(Calendar.SECOND,0);
            }
        });

        databaseHandler = new DatabaseHandler(this);
        databaseHandler.openDatabase();

        saveTaskButton.setOnClickListener(view -> {
            saveTask();
            finish();
        });
    }

    private void saveTask() {
        ToDoModel sampleData = new ToDoModel();
        sampleData.setTaskDescription(newTaskText.getText().toString());
        sampleData.setStatus(0);
        sampleData.setScheduleTime(mCalendar.getTimeInMillis());
        Log.d("TIME FOR ALARM",String.valueOf(mCalendar.getTimeInMillis()));
        sampleData.setCreationTime(Calendar.getInstance().getTimeInMillis());
        databaseHandler.insertTask(sampleData);
        Log.d("LIST SIZE", String.valueOf(databaseHandler.getTaskList().size()));
    }
}