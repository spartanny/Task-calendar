package com.example.taskcalendar;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskcalendar.Adapter.ItemTouchHelperCallback;
import com.example.taskcalendar.Adapter.ToDoAdapter;
import com.example.taskcalendar.Model.ToDoModel;
import com.example.taskcalendar.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ToDoAdapter taskAdapter;
    private RecyclerView tasksRecyclerView;
    private List<ToDoModel> taskList;
    private FloatingActionButton fab;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //INIT DATABASE
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.openDatabase();

        fab = findViewById(R.id.floatingActionButton);
        //initialize taskList
        taskList = new ArrayList<>();
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new ToDoAdapter(databaseHandler, taskList);
        ItemTouchHelperCallback touchHelperCallback = new ItemTouchHelperCallback(taskAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(tasksRecyclerView);
        tasksRecyclerView.setAdapter(taskAdapter);

        // Add new tasks using FAB widget
        fab.setOnClickListener(view -> {
            Intent i = new Intent(this, NewTaskActivity.class);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                }
            }).run();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //GET DATA FROM DATABASE
        taskList = databaseHandler.getTaskList();
        //  WONT SHOW ANY DATA UNTIL WE POPULATE THE ADAPTER
        taskAdapter.setTasks(taskList);
    }
}
