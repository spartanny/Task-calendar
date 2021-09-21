package com.example.taskcalendar.Model;

import androidx.lifecycle.ViewModel;

public class EditTextViewModel extends ViewModel {

    private String taskDescription;

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}
