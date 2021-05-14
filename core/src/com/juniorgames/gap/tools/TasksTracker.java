package com.juniorgames.gap.tools;

import com.juniorgames.gap.GapGame;

import java.util.ArrayList;

public class TasksTracker {

    public ArrayList<Task> tasks;
    private GapGame game;

    private String[] taskNames = {"tenLevelsComplete", "twentyLevelsComplete", "allLevelsComplete", "fiveEnemiesKilled",
            "tenEnemiesKilled", "twentyEnemiesKilled", "warpedThirtyTimes", "warpedSixtyTimes", "warped120Times",
            "diedFiveTimes", "diedTenTimes", "diedTwentyTimes"};

    public TasksTracker(GapGame game) {
        this.game = game;
        tasks = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            tasks.add(new Task("tasks/task" + (i + 1) + ".png", "tasks/task-strip" + (i + 1) + ".png", false, taskNames[i]));
        }
    }//constructor

    public void reset() {
        for (Task task : tasks) {
            task.completed = false;
        }
    }//reset

    public void update(SavedGame s) {
        if (s.completed >= 10 && !tasks.get(0).completed) {
            tasks.get(0).completed = true;
            game.currentTask = tasks.get(0);
        }//tenLevelsComplete

        if (s.completed >= 20 && !tasks.get(1).completed) {
            tasks.get(1).completed = true;
            game.currentTask = tasks.get(1);
        }//twentyLevelsComplete

        if (s.completed >= 30 && !tasks.get(2).completed) {
            tasks.get(2).completed = true;
            game.currentTask = tasks.get(2);
        }//allLevelsComplete

        if (s.killed >= 5 && !tasks.get(3).completed) {
            tasks.get(3).completed = true;
            game.currentTask = tasks.get(3);
        }//fiveEnemiesKilled

        if (s.killed >= 10 && !tasks.get(4).completed) {
            tasks.get(4).completed = true;
            game.currentTask = tasks.get(4);
        }//tenEnemiesKilled

        if (s.killed >= 20 && !tasks.get(5).completed) {
            tasks.get(5).completed = true;
            game.currentTask = tasks.get(5);
        }//twentyEnemiesKilled

        if (s.wrapped >= 30 && !tasks.get(6).completed) {
            tasks.get(6).completed = true;
            game.currentTask = tasks.get(6);
        }//warpedThirtyTimes

        if (s.wrapped >= 60 && !tasks.get(7).completed) {
            tasks.get(7).completed = true;
            game.currentTask = tasks.get(7);
        }//warpedSixtyTimes

        if (s.wrapped >= 120 && !tasks.get(8).completed) {
            tasks.get(8).completed = true;
            game.currentTask = tasks.get(8);
        }//warped120Times

        if (s.died >= 5 && !tasks.get(9).completed) {
            tasks.get(9).completed = true;
            game.currentTask = tasks.get(9);
        }//diedFiveTimes

        if (s.died >= 10 && !tasks.get(10).completed) {
            tasks.get(10).completed = true;
            game.currentTask = tasks.get(10);
        }//diedTenTimes

        if (s.died >= 20 && !tasks.get(11).completed) {
            tasks.get(11).completed = true;
            game.currentTask = tasks.get(11);
        }//diedTwentyTimes
    }//update
}
