package com.example.taskcalendar.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.taskcalendar.MainActivity;
import com.example.taskcalendar.Model.ToDoModel;
import com.example.taskcalendar.R;
import com.example.taskcalendar.Utils.DatabaseHandler;
import com.example.taskcalendar.Utils.ItemTouchHelperAdapter;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<ToDoModel> todoList;
    private DatabaseHandler db ;

    public ToDoAdapter(DatabaseHandler db, List<ToDoModel> taskList) {
        this.db = db;
        this.todoList = taskList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);

        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTaskDescription());
        holder.task.setChecked(intToBoolean(item.getStatus()));
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        ToDoModel task = todoList.get(position);
        Log.d("Itemdismiss Position", String.valueOf(task.getId()));
        todoList.remove(position);
        notifyItemRemoved(position);
        deleteTask(task.getId());
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteTask(int id) {
        this.db.deleteTask(id);
    }

    private boolean intToBoolean(int n) {
        return n >= 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckbox);
        }
    }
}
