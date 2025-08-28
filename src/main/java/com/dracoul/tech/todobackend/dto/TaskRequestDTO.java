
package com.dracoul.tech.todobackend.dto;

import com.dracoul.tech.todobackend.enums.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskRequestDTO {
    @NotBlank
    private String title;

    private String description;

    private TaskPriority priority = TaskPriority.MEDIUM;

    @FutureOrPresent
    private LocalDateTime dueDate;

    private Long projectId;

    private List<Long> tagIds;
}

