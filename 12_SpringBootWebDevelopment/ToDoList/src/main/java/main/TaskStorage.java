package main;

import response.Task;

import java.util.List;

public interface TaskStorage {

    Task createTask(Task task);

    Task getTask(int taskId);

    Task deleteTask(int taskId);

    Task updateTask(int taskId, Task task);

    List<Task> getAllTask();
}
