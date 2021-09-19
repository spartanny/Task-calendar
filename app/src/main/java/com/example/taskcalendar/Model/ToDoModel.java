package com.example.taskcalendar.Model;

public class ToDoModel {

    private int id, status;
    private String taskDescription;
    private long creationTime;
    private long scheduleTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setCreationTime(long creationTime){
        this.creationTime = creationTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
}
