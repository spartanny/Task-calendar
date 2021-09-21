package com.example.taskcalendar.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskcalendar.Fragments.DialogFragment;
import com.example.taskcalendar.Model.EditTextViewModel;
import com.example.taskcalendar.Model.ToDoModel;
import com.example.taskcalendar.R;
import com.example.taskcalendar.Utils.DatabaseHandler;
import com.example.taskcalendar.Utils.ItemTouchHelperAdapter;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<ToDoModel> todoList;
    private DatabaseHandler db;
    private DialogFragment dialogFragment;
    private Bundle bundle;
    private FragmentTransaction ft;
    private Context context;
    private EditTextViewModel model ;

    public ToDoAdapter(DatabaseHandler db, List<ToDoModel> taskList, DialogFragment dialogFragment, Context context, EditTextViewModel model) {
        this.db = db;
        this.todoList = taskList;
        this.dialogFragment = dialogFragment;
        this.context = context;
        this.model = model;
        this.bundle = new Bundle();
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
        Log.d("ItemDismiss Position", String.valueOf(task.getId()));
        todoList.remove(position);
        notifyItemRemoved(position);
        deleteTask(task.getId());
    }

    @Override
    public void onItemEdit(int position) {
        ToDoModel list = todoList.get(position);
        bundle.putString("text",list.getTaskDescription());
        bundle.putInt("position",list.getId());
        dialogFragment.setArguments(bundle);
        this.ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        dialogFragment.show(ft, "Dialog");

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
