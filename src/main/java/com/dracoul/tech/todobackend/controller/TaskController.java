package com.dracoul.tech.todobackend.controller;

import com.dracoul.tech.todobackend.dto.*;
import com.dracoul.tech.todobackend.entity.Task;
import com.dracoul.tech.todobackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;

//    public TaskController(TaskService taskService) {
//        this.taskService = taskService;
//    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequestDTO dto) {
        log.info("POST /create - Creating new task with title: {}", dto.getTitle());
        log.debug("Full task creation DTO: {}", dto);

        Task created = taskService.createTask(dto);

        log.info("Task created successfully with ID: {}", created.getId());
        log.debug("Created task details: {}", created);

        return ResponseEntity.ok(created);
    }

    @GetMapping
    public List<Task> all() {
        log.info("GET / - Fetching all tasks");
        List<Task> tasks = taskService.getAllTasks();

        log.info("Returning {} tasks", tasks.size());
        log.debug("Task list contents: {}", tasks);

        return tasks;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> get(@PathVariable Long id) {
        log.info("GET /{} - Fetching task by ID", id);

        return taskService.getTask(id)
                .map(task -> {
                    log.info("Found task with ID: {}", id);
                    log.debug("Task details: {}", task);
                    return ResponseEntity.ok(task);
                })
                .orElseGet(() -> {
                    log.warn("Task with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /{} - Deleting task", id);

        try {
            taskService.deleteTask(id);
            log.info("Successfully deleted task with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting task with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<TaskRequestDTO>> getTasksForCurrentUser() {
        log.info("GET /my - Fetching tasks for current user");

        List<TaskRequestDTO> tasks = taskService.getTasksForCurrentUser();

        log.info("Found {} tasks for current user", tasks.size());
        log.debug("User tasks details: {}", tasks);

        return ResponseEntity.ok(tasks);
    }

}