package com.dracoul.tech.todobackend.service;

import com.dracoul.tech.todobackend.dto.TagDto;
import com.dracoul.tech.todobackend.entity.Tag;
import com.dracoul.tech.todobackend.repo.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag createTag(TagDto dto) {
        Tag tag = new Tag();
        tag.setName(dto.getName());
        return tagRepository.save(tag);
    }
}
