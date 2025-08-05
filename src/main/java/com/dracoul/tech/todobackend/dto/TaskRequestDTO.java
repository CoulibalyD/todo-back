
package com.dracoul.tech.todobackend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private String priority;
    private LocalDateTime dueDate;
    private Long projectId;
    private List<Long> tagIds;
}
