package main;

import response.Task;

import java.text.SimpleDateFormat;
import java.util.*;

public class Storage {
    private static int currentId = 1;
    private static Map<Integer, Task> tasks = new HashMap<>();

    public static List<Task> getAllTask() {
        return new ArrayList<Task>(tasks.values());
    }

    public static void addTask(Task task) {
        int id = currentId++;
        task.setId(id);
        task.setDate("Создано: " + getDate());
        tasks.put(id, task);
    }

    public static Task getTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        }
        return null;
    }

    public static Task delTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.remove(taskId);
        }
        return null;
    }

    public static Task updateTask(int taskId, String task) {
        if (tasks.containsKey(taskId)) {
            tasks.get(taskId).setTask(task);
            tasks.get(taskId).setDate("Изменено: " + getDate());
            return tasks.get(taskId);
        }
        return null;
    }

    private static String getDate() {
        return new SimpleDateFormat("d.M.yyyy ~ HH:mm").format(new Date());
    }
}
