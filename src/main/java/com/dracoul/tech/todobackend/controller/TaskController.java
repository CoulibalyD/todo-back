package com.dracoul.tech.todobackend.controller;

import com.dracoul.tech.todobackend.entity.Task;
import com.dracoul.tech.todobackend.repo.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Task> all() { return repo.findAll(); }
    @PostMapping
    public Task save(@RequestBody Task t) { return repo.save(t); }
    @PutMapping("/{id}") public Task update(@PathVariable Long id, @RequestBody Task t) {
        t.setId(id); return repo.save(t);
    }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { repo.deleteById(id); }
}

