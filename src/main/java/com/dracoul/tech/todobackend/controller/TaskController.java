

package com.dracoul.tech.todobackend.controller;

import com.dracoul.tech.todobackend.dto.*;
import com.dracoul.tech.todobackend.entity.Task;
import com.dracoul.tech.todobackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;

    // 🔹 CREATE
    @PostMapping("/create")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO dto) {
        log.info("POST /task/create - Creating new task with title: {}", dto.getTitle());
        log.debug("Full task creation DTO: {}", dto);

        Task created = taskService.createTask(dto);

        log.info("Task created successfully with ID: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponseDTO.fromEntity(created));
    }

    // 🔹 READ ALL
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> all() {
        log.info("GET /task - Fetching all tasks");
        List<Task> tasks = taskService.getAllTasks();

        log.info("Returning {} tasks", tasks.size());
        return ResponseEntity.ok(tasks.stream().map(TaskResponseDTO::fromEntity).toList());
    }

    // 🔹 READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> get(@PathVariable Long id) {
        log.info("GET /task/{} - Fetching task by ID", id);

        return taskService.getTask(id)
                .map(task -> {
                    log.info("Found task with ID: {}", id);
                    return ResponseEntity.ok(TaskResponseDTO.fromEntity(task));
                })
                .orElseGet(() -> {
                    log.warn("Task with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // 🔹 UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO dto) {
        log.info("PUT /task/{} - Updating task", id);

        try {
            Task updated = taskService.updateTask(id, dto);
            log.info("Task updated successfully with ID: {}", id);
            return ResponseEntity.ok(TaskResponseDTO.fromEntity(updated));
        } catch (Exception e) {
            log.error("Error updating task with ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /task/{} - Deleting task", id);

        try {
            taskService.deleteTask(id);
            log.info("Successfully deleted task with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting task with ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 GET TASKS FOR CURRENT USER
    @GetMapping("/my")
    public ResponseEntity<List<TaskResponseDTO>> getTasksForCurrentUser() {
        log.info("GET /task/my - Fetching tasks for current user");

        List<TaskRequestDTO> tasks = taskService.getTasksForCurrentUser();

        log.info("Found {} tasks for current user", tasks.size());
        return ResponseEntity.ok(
                tasks.stream()
                        .map(TaskResponseDTO::fromRequestDto)
                        .toList()
        );
    }
}
