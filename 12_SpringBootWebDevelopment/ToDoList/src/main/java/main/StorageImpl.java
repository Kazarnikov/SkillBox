package main;

import org.springframework.stereotype.Component;
import response.Task;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class StorageImpl implements TaskStorage{
    private final AtomicInteger currentId = new AtomicInteger();
    private final Map<Integer, Task> tasks = new  ConcurrentHashMap<>();

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<Task>(tasks.values());
    }
    @Override
    public Task createTask(Task task) {
        int id = currentId.incrementAndGet();
        task.setId(id);
        task.setDate("Создано: " + getDate());
        tasks.put(id, task);
        return tasks.get(id);
    }
    @Override
    public Task getTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        }
        return null;
    }
    @Override
    public Task deleteTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.remove(taskId);
        }
        return null;
    }
    @Override
    public Task updateTask(int taskId, Task task) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            task.setId(taskId);
            task.setDate("Изменено: " + getDate());
            tasks.put(taskId, task);
            return tasks.get(taskId);
        }
        return null;
    }

    private String getDate() {
        return new SimpleDateFormat("d.M.yyyy ~ HH:mm").format(new Date());
    }
}
