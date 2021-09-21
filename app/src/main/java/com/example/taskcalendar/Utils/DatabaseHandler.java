package com.example.taskcalendar.Utils;

import static android.content.ContentValues.TAG;
import static java.util.Collections.reverse;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.taskcalendar.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "todoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK_DESCRIPTION = "taskDescription";
    private static final String STATUS = "status";
    private static final String CREATION_TIME = "creationTime";
    private static final String SCHEDULE_TIME = "scheduleTime";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TASK_DESCRIPTION + " TEXT, "
            + STATUS + " INTEGER, "
            + SCHEDULE_TIME + " INTEGER, "
            + CREATION_TIME + " INTEGER )";

    private SQLiteDatabase db;

    //Constructor
    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    @SuppressLint("Range")
    public void insertTask(ToDoModel task) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(STATUS, task.getStatus());
            cv.put(TASK_DESCRIPTION, task.getTaskDescription());
            cv.put(CREATION_TIME, task.getCreationTime());
            cv.put(SCHEDULE_TIME, task.getScheduleTime());
            db.insertOrThrow(TODO_TABLE, null, cv);
            Log.d("insertTask", String.valueOf(cv.get(TASK_DESCRIPTION)));
        } catch (Exception e) {
            Log.d("insertError", "error while inserting :" + e);
        }
    }

    @SuppressLint("Range")
    public List<ToDoModel> getTaskList() {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        //ADD CODE IN BETWEEN TO PRESERVE DB STATE
        try {
//            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            cur = db.rawQuery("SELECT * FROM " + TODO_TABLE, null);
            if (cur.moveToFirst()) {
                do {
                    ToDoModel task = new ToDoModel();
                    task.setId(cur.getInt(cur.getColumnIndex(ID)));
                    task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                    task.setTaskDescription(cur.getString(cur.getColumnIndex(TASK_DESCRIPTION)));
                    task.setCreationTime((cur.getLong(cur.getColumnIndex(CREATION_TIME))));
                    taskList.add(task);
                } while (cur.moveToNext());
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        reverse(taskList);
        return taskList;
    }

    //update task description method
    public void updateTaskDescription(int id, String taskDescription) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_DESCRIPTION, taskDescription);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "=?", new String[]{String.valueOf(id)});
    }

    @SuppressLint("Range")
    public List<ToDoModel> queryTime(int id) {
        String parameter = null;
        List<ToDoModel> queryList = new ArrayList<>();

        if (id == 1)
            parameter = CREATION_TIME;
        else if (id == 2)
            parameter = SCHEDULE_TIME;

        db.beginTransaction();
        Cursor cur = null;
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, parameter + " ASC");
            if (cur.moveToFirst()) {
                do {
                    ToDoModel task = new ToDoModel();
                    task.setId(cur.getInt(cur.getColumnIndex(ID)));
                    task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                    task.setTaskDescription(cur.getString(cur.getColumnIndex(TASK_DESCRIPTION)));
                    task.setCreationTime((cur.getLong(cur.getColumnIndex(parameter))));
                    queryList.add(task);
                } while (cur.moveToNext());
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        Log.d(TAG, "queryTime: Size of queryList is : "+ queryList.size());
        return queryList;
    }

}
