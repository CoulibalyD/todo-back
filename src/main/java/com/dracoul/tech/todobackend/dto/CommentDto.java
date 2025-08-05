package com.dracoul.tech.todobackend.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private Long taskId;
    private Long authorId;
}

