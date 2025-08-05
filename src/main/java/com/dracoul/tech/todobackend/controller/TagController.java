package com.dracoul.tech.todobackend.controller;

import com.dracoul.tech.todobackend.dto.TagDto;
import com.dracoul.tech.todobackend.entity.Tag;
import com.dracoul.tech.todobackend.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tags")
@RequiredArgsConstructor
public class  TagController {
    private final TagService tagService;

    @GetMapping
    public List<Tag> all() {
        return tagService.getAllTags();
    }

    @PostMapping
    public ResponseEntity<Tag> create(@RequestBody TagDto dto) {
        return new ResponseEntity<>(tagService.createTag(dto), HttpStatus.CREATED);
    }
}

