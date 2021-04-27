package com.juniorgames.gap.tools;

public class Task {
    public String taskImagePath;
    public String taskStripImagePath;
    public boolean completed;
    public String taskName;

    public Task(String taskImagePath, String taskStripImagePath, boolean completed, String taskName) {
        this.taskImagePath = taskImagePath;
        this.taskStripImagePath = taskStripImagePath;
        this.completed = completed;
        this.taskName = taskName;
    }
}
