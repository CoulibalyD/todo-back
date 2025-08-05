package com.dracoul.tech.todobackend.service;

import com.dracoul.tech.todobackend.dto.CommentDto;
import com.dracoul.tech.todobackend.entity.Comment;
import com.dracoul.tech.todobackend.entity.Task;
import com.dracoul.tech.todobackend.repo.CommentRepository;
import com.dracoul.tech.todobackend.repo.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public List<Comment> getCommentsForTask(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public Comment addComment(CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        comment.setTask(task);
        // L'auteur peut être géré via auth / userService si tu as une entité User
        return commentRepository.save(comment);
    }
}

