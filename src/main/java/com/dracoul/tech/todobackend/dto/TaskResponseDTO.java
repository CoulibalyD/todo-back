package com.dracoul.tech.todobackend.dto;

import com.dracoul.tech.todobackend.entity.Task;
import com.dracoul.tech.todobackend.enums.TaskPriority;
import com.dracoul.tech.todobackend.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private boolean completed;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String assignedToUsername;
    private String projectName;

    public static TaskResponseDTO fromEntity(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.isCompleted(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null,
                task.getProject() != null ? task.getProject().getName() : null
        );
    }

    // Conversion rapide si on part d’un RequestDTO
    public static TaskResponseDTO fromRequestDto(TaskRequestDTO dto) {
        return new TaskResponseDTO(
                null,
                dto.getTitle(),
                dto.getDescription(),
                dto.getPriority(),
                TaskStatus.TO_DO,
                false,
                dto.getDueDate(),
                null,
                null,
                null,
                null
        );
    }
}
