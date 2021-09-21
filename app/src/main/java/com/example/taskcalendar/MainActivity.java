package com.example.taskcalendar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskcalendar.Adapter.ItemTouchHelperCallback;
import com.example.taskcalendar.Adapter.ToDoAdapter;
import com.example.taskcalendar.Fragments.DialogFragment;
import com.example.taskcalendar.Model.EditTextViewModel;
import com.example.taskcalendar.Model.ToDoModel;
import com.example.taskcalendar.Utils.DatabaseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ToDoAdapter taskAdapter;
    private RecyclerView tasksRecyclerView;
    private List<ToDoModel> taskList;
    private FloatingActionButton fab;
    private DatabaseHandler databaseHandler;
    private DialogFragment dialogFragment;
    private FragmentTransaction ft;
    private BottomNavigationView navbar;
    private EditTextViewModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //INIT DATABASE AND OTHER RESOURCES
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.openDatabase();

        dialogFragment = new DialogFragment();
        fab = findViewById(R.id.floatingActionButton);
        navbar = findViewById(R.id.bottom_navigation);
        model = new ViewModelProvider(this).get(EditTextViewModel.class);
        createNotificationChannel();
        //INITIALIZE TASK LIST

        taskList = new ArrayList<>();
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new ToDoAdapter(databaseHandler, taskList, dialogFragment, this, model);
        ItemTouchHelperCallback touchHelperCallback = new ItemTouchHelperCallback(taskAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(tasksRecyclerView);
        tasksRecyclerView.setAdapter(taskAdapter);

        // SET EVENT LISTENERS
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
        // NavBar
        navbar.findViewById(R.id.created).setOnClickListener(view -> {
            if (taskList.size() == 0)
                Toast.makeText(this, "No Tasks ;)", Toast.LENGTH_SHORT).show();
            else {
                taskAdapter.setTasks(databaseHandler.queryTime(1));
            }
        });
        navbar.findViewById(R.id.upcoming).setOnClickListener(view -> {
            if (taskList.size() == 0)
                Toast.makeText(this, "No Tasks ;)", Toast.LENGTH_SHORT).show();
            else {
                taskList = databaseHandler.queryTime(2);
                taskAdapter.setTasks(taskList);
            }
        });
        navbar.findViewById(R.id.defaultList).setOnClickListener(view -> {
            if (taskList.size() == 0)
                Toast.makeText(this, "No Tasks ;)", Toast.LENGTH_SHORT).show();
            else {
                taskList = databaseHandler.getTaskList();
                taskAdapter.setTasks(taskList);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTask();
    }

    public void updateTask() {
        //GET DATA FROM DATABASE
        taskList = databaseHandler.getTaskList();
        taskAdapter.setTasks(taskList);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TaskReminder";
            String description = "Allows you to be informed about tasks at the time";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("CHANNEL_ID", name, importance);
            notificationChannel.setDescription(description);

            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }
}
