package main.controllers;

import main.model.Task;
import main.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping(value = "/tasks/")
    public ResponseEntity getTasks() {
        List<Task> tasks = (List<Task>) taskRepository.findAll();
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/tasks/")
    public ResponseEntity createTask(@RequestBody Task task) {
        task.setDate("Создано: " + getDate());
        return new ResponseEntity<>(taskRepository.save(task), HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{id}/")
    public ResponseEntity getTask(@PathVariable("id") int id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return new ResponseEntity<>(task.get(), HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}/")
    public ResponseEntity deleteTask(@PathVariable("id") int id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        taskRepository.deleteById(id);
        return new ResponseEntity<>(task.get(), HttpStatus.OK);
    }

    @DeleteMapping("/tasks/")
    public ResponseEntity deleteTaskAll() {
        List<Task> tasks = (List<Task>) taskRepository.findAll();
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        taskRepository.deleteAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/tasks/{id}/")
    public ResponseEntity updateTask(@PathVariable("id") int id, @RequestBody Task task) {
        Optional<Task> tasks = taskRepository.findById(id);
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        task.setId(id);
        task.setDate("Изменено: " + getDate());
        return new ResponseEntity<>(taskRepository.save(task), HttpStatus.OK);
    }

    private String getDate() {
        return new SimpleDateFormat("d.M.yyyy ~ HH:mm").format(new Date());
    }
}
