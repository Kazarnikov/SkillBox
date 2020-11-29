package main.controllers;

import main.TaskStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response.Task;

import java.util.List;

@RestController
public class TaskController {
    @Autowired
    TaskStorage taskStorage;

    @GetMapping(value = "/tasks/")
    public ResponseEntity getTasks() {
        List<Task> task = taskStorage.getAllTask();
        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @PostMapping("/tasks/")
    public ResponseEntity addTask(@RequestBody Task task) {
        return new ResponseEntity(taskStorage.createTask(task), HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}/")
    public ResponseEntity getTask(@PathVariable("id") int id) {
        Task task = taskStorage.getTask(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}/")
    public ResponseEntity delTask(@PathVariable("id") int id) {
        Task task = taskStorage.deleteTask(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @PutMapping("/tasks/{id}/")
    public ResponseEntity updateTask(@PathVariable("id") int id, @RequestBody Task tasks) {
        Task task = taskStorage.updateTask(id, tasks);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return new ResponseEntity(task, HttpStatus.OK);
    }
}
