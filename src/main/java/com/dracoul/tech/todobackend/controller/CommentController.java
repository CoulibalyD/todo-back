package com.dracoul.tech.todobackend.controller;

import com.dracoul.tech.todobackend.dto.CommentDto;
import com.dracoul.tech.todobackend.entity.Comment;
import com.dracoul.tech.todobackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/task/{taskId}")
    public List<Comment> byTask(@PathVariable Long taskId) {
        return commentService.getCommentsForTask(taskId);
    }

    @PostMapping
    public ResponseEntity<Comment> add(@RequestBody CommentDto dto) {
        return new ResponseEntity<>(commentService.addComment(dto), HttpStatus.CREATED);
    }
}

