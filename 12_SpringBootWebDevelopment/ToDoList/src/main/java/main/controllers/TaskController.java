package main.controllers;

import main.Storage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response.Task;

import java.util.List;

@RestController
public class TaskController {

    @GetMapping(value = "/tasks/")
    public List<Task> list() {
        return Storage.getAllTask();
    }

    @PostMapping("/tasks/")
    public void addTask(Task task) {
        Storage.addTask(task);
    }

    @GetMapping("/tasks/{id}/")
    public ResponseEntity getTask(@PathVariable int id) {
        Task task = Storage.getTask(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}/")
    public ResponseEntity delTask(@PathVariable int id) {
        Task task = Storage.delTask(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return new ResponseEntity(task, HttpStatus.OK);
    }


    @PutMapping("/tasks/{id}/")
    public ResponseEntity updateTask(@PathVariable int id, @RequestBody String tasks) {
        Task task = Storage.updateTask(id, tasks);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return new ResponseEntity(task, HttpStatus.OK);
    }
}
