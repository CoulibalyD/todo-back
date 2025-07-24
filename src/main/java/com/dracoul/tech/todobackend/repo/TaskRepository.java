package com.dracoul.tech.todobackend.repo;

import com.dracoul.tech.todobackend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {}

